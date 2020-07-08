// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
// import com.google.api.client.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import com.google.api.services.urlshortener.Urlshortener;

// tryAppEngineCredentials();
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.auth.Credentials;
import com.google.auth.appengine.AppEngineCredentials; // https://github.com/googleapis/google-auth-library-java/blob/master/appengine/java/com/google/auth/appengine/AppEngineCredentials.java

/* TODO: comment */
@WebServlet("/cal")
public class OAuthServlet extends HttpServlet {

  @Override
  public void init() {

  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // do stuff
    System.out.println("/cal: OAuthServlet.doGet()");

    // CalendarQuickstart calendar = new CalendarQuickstart();
    // try {
    //   // calendar.main();
    // } catch (Exception e) {
    //   System.out.println("there was in error in OAuthServlet.doGet() when accessing CalendarQuickstart");
    //   e.printStackTrace();
    // }

    tryAppEngineCredentials();
    // tryGoogleCredential();
    // tryAppIdentityCredential();

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  // https://github.com/googleapis/google-auth-library-java#google-auth-library-appengine
  private void tryAppEngineCredentials() {
    AppIdentityService appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
    Collection<String> scopes = new ArrayList<String>();
    scopes.add("https://www.googleapis.com/auth/calendar");

    Credentials credentials =
        AppEngineCredentials.newBuilder()
            // .setScopes(...)
            .setScopes(scopes) // Collection<String>
            .setAppIdentityService(appIdentityService)
            .build();
    System.out.println(credentials.toString());
  }

  // https://developers.google.com/api-client-library/java/google-api-java-client/oauth2#googlecredential
  private void tryGoogleCredential() {
    // GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
    // Plus plus = new Plus.builder(new NetHttpTransport(),
    //                             JacksonFactory.getDefaultInstance(),
    //                             credential)
    //     .setApplicationName("Google-PlusSample/1.0")
    //     .build();
  }

  // https://developers.google.com/api-client-library/java/google-api-java-client/oauth2#gae-id
  // private Urlshortener tryAppIdentityCredential() {
    // AppIdentityCredential credential =
    //     new AppIdentityCredential(
    //         Collections.singletonList(UrlshortenerScopes.URLSHORTENER));
    // return new Urlshortener.Builder(new UrlFetchTransport(),
    //                                 JacksonFactory.getDefaultInstance(),
    //                                 credential)
    //     .build();
  // }

}

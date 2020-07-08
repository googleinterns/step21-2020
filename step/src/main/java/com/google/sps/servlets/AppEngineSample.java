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

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/*
  Uses the currently loggeg-in Google Account user??
    https://googleapis.dev/java/google-oauth-client/latest/com/google/api/client/extensions/appengine/auth/oauth2/AbstractAppEngineAuthorizationCodeServlet.html
*/

@WebServlet("/auth")
public class AppEngineSample extends AbstractAppEngineAuthorizationCodeServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // do stuff
    
    System.out.println("AppEngineSample.doGet()");
  }

  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/oauth2callback");
    return url.build();
  }

  @Override
  protected AuthorizationCodeFlow initializeFlow() throws IOException {
    return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
        new UrlFetchTransport(),
        new JacksonFactory(),
        new GenericUrl("https://server.example.com/token"),
        new BasicAuthentication("s6BhdRkqt3", "7Fjfp0ZBr1KtDRbnfVdmIw"),
        "s6BhdRkqt3",
        // "https://server.example.com/authorize").setCredentialStore( // setCredentialStore deprecated
        "https://server.example.com/authorize").setCredentialDataStore(
            StoredCredential.getDefaultDataStore(AppEngineDataStoreFactory.getDefaultInstance()))
        .build();

    // AppEngineDataStoreFactory.getDefaultInstance() returns a AppEngineDataStoreFactory object.
      // AppEngineDataStoreFactory extends AbstractDataStoreFactory
        // AbstractDataStoreFactory implements DataStoreFactory
          // DataStoreFactory is an interface
    // StoredCredential.getDefaultDataStore(DataStoreFactory) returns a DataStore<StoredCredential>
  }

  // @Override
  // protected abstract String getUserId(HttpServletRequest req)
  //     throws javax.servlet.ServletException, IOException {
  //   return "adam"; // TODO idk what should go here, fix it.
  // }
}

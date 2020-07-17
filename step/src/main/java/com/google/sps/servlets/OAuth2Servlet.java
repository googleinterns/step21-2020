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

// Generic imports
import java.util.List;
import java.util.Arrays;
import java.io.FileReader;
import java.io.FileNotFoundException;

// Servlet imports
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Authorization imports
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;

// Users API imports
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

// Google Calendar API imports
import com.google.api.services.calendar.CalendarScopes;

@WebServlet("/oauth2")
public class OAuth2Servlet extends HttpServlet {

  private GoogleAuthorizationCodeFlow authFlow;
  List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);
  private static String AUTH_REDIRECT_URI = // TODO find a way to get rid of this constant
    "https://8080-7dc48ed0-1a8b-4df6-ba73-e55ccd2fb9ed.us-central1.cloudshell.dev/oauth2";
    // "http://brenda-ding-pod-step-20.uc.r.appspot.com/oauth2";
    // "https://brenda-ding-pod-step-20.ue.r.appspot.com/oauth2";
  private static String CLIENT_SECRETS_JSON_PATH = "./WEB-INF/classes/client_secrets.json";

  @Override
  public void init() {
    // https://googleapis.dev/java/google-api-client/latest/com/google/api/client/googleapis/auth/oauth2/GoogleClientSecrets.html

    GoogleClientSecrets clientSecret = null;
    try {
      clientSecret =
        GoogleClientSecrets.load(
          new JacksonFactory(),
          new FileReader(CLIENT_SECRETS_JSON_PATH)
        );
    } catch (FileNotFoundException e) { // thrown by FileReader constructor
      printError("Unable to find client_secrets.json at " + CLIENT_SECRETS_JSON_PATH);
    } catch (IOException e) { // thrown by GoogleClientSecrets.load()
      printError("Unable to create the GoogleClientSecrets");
    }
    // catch (Exception e) {
      // TODO abort oauth process as authFlow won't be able to be created properly
      // return; // TODO error handle
    // }

    authFlow = new GoogleAuthorizationCodeFlow.Builder(
      new NetHttpTransport(),
      new JacksonFactory(),
      clientSecret,
      SCOPES
    ).build();

    if(authFlow == null) {
      // TODO error handle
      printError("authentication flow was unable to be initialized");
    }
  }

  /**
   * Authorization code flow attempt: https://developers.google.com/api-client-library/java/google-api-java-client/oauth2#authorization_code_flow
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // If authCode is not null, the user has been redirected from Google's authorization server.
    // Else, this is a normal GET request.
    String authCode = request.getParameter("code");
    if(authCode == null) {
      System.out.println("Beginning authorization code flow...");

      // Authorization code flow:
      // 1) End user logins into your app. Generate a user ID that is unique for your app
      UserService userService = UserServiceFactory.getUserService();
      com.google.appengine.api.users.User userServiceUser = userService.getCurrentUser();
      String userId = userServiceUser.getUserId();
      if(userId == null) {
        // TODO error handle
        printError("Unable to obtain userId");
      }

      // 2) Call AuthorizationCodeFlow.loadCredential(String userId) to check if the end-user's
      //    credentials are already known. If so, we're done.
      Credential credential = authFlow.loadCredential(userId);
      if(credential != null) { // If the user's credentials were found,
        return; // we're done.
      }

      // 3) Call AuthorizationCodeFlow.newAuthoirzationUrl()
      //    direct the end-user's broweser to an authorization page so they can grant the app
      //    access to their protected data. Also need to specify the url to redirect back to app
      GoogleAuthorizationCodeRequestUrl authCodeRequestUrl = authFlow.newAuthorizationUrl();
      authCodeRequestUrl.setRedirectUri(AUTH_REDIRECT_URI);
      String authRedirectUrl = authCodeRequestUrl.build();
      System.out.println("authCodeRequestUrl: " + authCodeRequestUrl.toString());
      response.sendRedirect(authRedirectUrl);
      return;

    } else {
      System.out.println("authCode: " + authCode);

      // 4) The Google authorization server will redirect the user back to your app, along with a
      //    code query parameter. Use the code query param to request an access token by calling
      //    AuthorizationCodeFlow.newTokenRequest(String authcode)
      // AuthorizationCodeTokenRequest authCodeTokenResponse = authFlow.newTokenRequest(authCode);
      TokenResponse tokenResponse = authFlow
          .newTokenRequest(authCode)
          .setRedirectUri(AUTH_REDIRECT_URI)
          .execute();
      System.out.println("after newTokenRequest");
      System.out.println("tokenResponse: " + tokenResponse);

      // 5) Use AuthorizationCodeFlow.createAndStoreCredential(TokenResponse, String) to store and
      //    obtain a credential for accessing protected services.
      //    Use Credential.setAccessToken(String accessToken) ??
      UserService userService = UserServiceFactory.getUserService();
      com.google.appengine.api.users.User userServiceUser = userService.getCurrentUser();
      String userId = userServiceUser.getUserId();
      // TODO(adamsamuelson): might be a deprecated method, look at setDataStoreFactory() in .Builder
      Credential credential = authFlow.createAndStoreCredential(tokenResponse, userId);
      System.out.println("credential: " + credential);
      System.out.println("Authorization code flow complete.");

      // test the calendar event being created
      CalendarManager.createTestGCalEvent(credential);

      // Done.
      response.sendRedirect("/profile.jsp");
    }
    
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
  }

  private static void printError(String errorMessage) {
    System.err.println("ERROR: " + errorMessage);
  }

}

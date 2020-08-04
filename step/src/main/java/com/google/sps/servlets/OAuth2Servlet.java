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

import java.util.List;
import java.util.Arrays;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.IllegalStateException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.sps.OAuth2Utilities;

@WebServlet("/oauth2")
public class OAuth2Servlet extends HttpServlet {

  private final static String AUTH_REDIRECT_URI = OAuth2Utilities.AUTH_REDIRECT_URI;
  private final static String DONE_REDIRECT_URL = "/ChatButton?request-type=request-type-match";
  private GoogleAuthorizationCodeFlow authFlow;

  @Override
  public void init() {
    try {
      authFlow = OAuth2Utilities.getAuthFlow();
    } catch (Exception e) {
      printError(e.getMessage());
      printError("Unable to initialize authFlow. User won't be able to be authenticated");
    }
  }

  /**
   * Preconditions to this GET request:
   * - authFlow in init() must have been able to be initialized; AND
   * - a user must currently be logged in
   *
   * GET requests to this servlet are interpreted as requests to authenticate the currently logged
   * in user and will redirect the browser to our application's OAuth consent screen with Google.
   * The GoogleAuthroizationCodeFlow used in this method follows the Authorization Code Grant flow
   * as described here: https://tools.ietf.org/html/rfc6749#section-4.1
   * This authorization code flow follows the GoogleAuthorizationCodeFlow example found here:
   * https://developers.google.com/api-client-library/java/google-api-java-client/oauth2#authorization_code_flow
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (authFlow == null) {
      throw new IllegalStateException("The user cannot be authenticated without authFlow "
                                      + "being initialized.");
    }

    String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
    if (userId == null) {
      throw new IllegalStateException("Unable to obtain userId while authenticating. "
                                      + "/oauth2 may only be called while a user is logged in.");
    }

    // TODO(adamsamuelson): handle when the user rejects oauth request
    // If authCode is null, the user is beginning the oauth process.
    // If authCode isn't null, the user has been redirected from Google's authorization server.
    String authCode = request.getParameter("code");
    if (authCode == null) {
      System.out.println("Beginning authorization code flow...");

      // Redirect the user to Google's OAuth consent screen for our application.
      GoogleAuthorizationCodeRequestUrl authCodeRequestUrl = authFlow.newAuthorizationUrl();
      authCodeRequestUrl.setRedirectUri(AUTH_REDIRECT_URI);
      response.sendRedirect(authCodeRequestUrl.build());

      // Authorization code flow continues in the following else statement.
      
    } else {
      // Upon user's consent, the user redirects to us with a code query parameter that we use
      // to request an access token from Google. This token will authorize our GCal API requests.
      TokenResponse tokenResponse = authFlow
          .newTokenRequest(authCode)
          .setRedirectUri(AUTH_REDIRECT_URI)
          .execute();

      // Use the access token to store and obtain a credential to auth our GCal API requests.
      Credential credential = authFlow.createAndStoreCredential(tokenResponse, userId);

      System.out.println("Authorization code flow complete.");
      response.sendRedirect(DONE_REDIRECT_URL);
    }
    
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {}

  private static void printError(String errorMessage) {
    System.err.println("ERROR: " + errorMessage);
  }

}

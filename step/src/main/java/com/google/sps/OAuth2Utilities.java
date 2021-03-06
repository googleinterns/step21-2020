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

package com.google.sps;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;

public class OAuth2Utilities {

  public final static String CREDENTIAL_DATASTORE_ID = "credential_datastore";
  private final static String CLIENT_SECRETS_JSON_PATH = "./WEB-INF/classes/client_secrets.json";
  public final static String AUTH_REDIRECT_URI = // TODO find a way to get rid of this constant
    // "https://8080-7dc48ed0-1a8b-4df6-ba73-e55ccd2fb9ed.us-central1.cloudshell.dev/oauth2";
    "https://brenda-ding-pod-step-20.ue.r.appspot.com/oauth2";


  public static GoogleAuthorizationCodeFlow getAuthFlow()
      throws FileNotFoundException, IOException, RuntimeException {

    DataStore<StoredCredential> credentialDataStore;
    try {
      credentialDataStore = getCredentialDataStore();
    } catch (IOException e) {
      throw new IOException("Unable to get the credential datastore.");
    }

    GoogleAuthorizationCodeFlow authFlow;
    try {
      authFlow = new GoogleAuthorizationCodeFlow.Builder(
        new NetHttpTransport(),
        new JacksonFactory(),
        GoogleClientSecrets.load( // Load the client id and secret from client_secrets.json
          new JacksonFactory(),
          new FileReader(CLIENT_SECRETS_JSON_PATH)),
        CalendarManager.getScopes()
      ).setCredentialDataStore(
        credentialDataStore
      ).build();
    } catch (FileNotFoundException e) { // thrown by FileReader constructor
      throw new FileNotFoundException("Unable to find client_secrets.json at " + CLIENT_SECRETS_JSON_PATH);
    } catch (IOException e) { // thrown by GoogleClientSecrets.load()
      throw new IOException("Unable to load the GoogleClientSecrets.");
    }

    if (authFlow == null) {
      throw new RuntimeException("Unable to initialize GoogleAuthorizationCodeFlow.");
    }

    return authFlow;
  }

  /**
   * @param userId the userId of the user whose credential will be returned.
   * @return if the given user has a credential, return the credential. Otherwise return null.
   * @throws RuntimeException when this method is unable to get the user's credential.
   */
  public static Credential getUserCredential(String userId) {
    GoogleAuthorizationCodeFlow authFlow;
    try {
      authFlow = getAuthFlow();
      return authFlow.loadCredential(userId);
    } catch (Exception e) { // thrown by getAuthFlow()
      printError(e.getMessage());
      throw new RuntimeException("There was an exception while loading the user's credential.");
    }
  }

  public static boolean isUserAuthenticated(String userId) {
    return getUserCredential(userId) != null;
  }

  private static DataStore<StoredCredential> getCredentialDataStore() throws IOException {
    return new AppEngineDataStoreFactory().getDataStore(CREDENTIAL_DATASTORE_ID);
  }

  private static void printError(String errorMessage) {
    System.err.println("ERROR: " + errorMessage);
  }
}

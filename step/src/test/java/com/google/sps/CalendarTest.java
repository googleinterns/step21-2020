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

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.googleapis.testing.services.MockGoogleClient;
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;

@RunWith(JUnit4.class)
public final class CalendarTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
        new LocalDatastoreServiceTestConfig(),
        new LocalUserServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void tempCalTest() {
    Assert.assertEquals("hello", "hello");
  }

  // @Test
  public void testDateTimeStringCreation() {
    String actual = CalendarManager.dateTimeString(2020, 7, 16, 9, 0);
    String expected = "2020-07-16T09:00:00-05:00";
    
    Assert.assertEquals(actual, expected);
  }

  @Test
  public void testGoogleClient() {
    MockGoogleClient client = newMockClient(newMockCredential());
  }

  @Test
  public void testGoogleCredential() {
    MockGoogleCredential credential = newMockCredential();
  }

  // Side list of mocks: https://googleapis.dev/java/google-api-client/1.23.0/index.html?com/google/api/client/googleapis/auth/oauth2/GoogleAuthorizationCodeFlow.Builder.html

  // MockGoogleClientRequest<T> https://googleapis.dev/java/google-api-client/1.23.0/com/google/api/client/googleapis/testing/services/MockGoogleClientRequest.html

  // https://googleapis.dev/java/google-api-client/latest/com/google/api/client/googleapis/testing/services/MockGoogleClient.html
  private MockGoogleClient newMockClient(MockGoogleCredential credential) {
    String serviceRootUrl = "https://www.googleapis.com/"; // ex: https://www.googleapis.com/
    String servicePath = "auth/calendar"; // ex: tasks/v1/

    return new MockGoogleClient.Builder(
        new NetHttpTransport(), // HttpTransport transport
        serviceRootUrl, // String rootUrl
        servicePath, // String servicePath
        null, // ObjectParser ObjectParser
        credential // HttpRequestInitializer httpRequestInitializer
      ).build();
  }

  // https://googleapis.dev/java/google-api-client/1.23.0/com/google/api/client/googleapis/testing/auth/oauth2/MockGoogleCredential.html
  private MockGoogleCredential newMockCredential() {
    return new MockGoogleCredential.Builder().build();
  }
}
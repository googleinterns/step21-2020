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

import java.lang.IllegalStateException;
import java.util.List;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Builder;
import com.google.api.services.calendar.model.Event;
import com.google.api.client.googleapis.testing.services.MockGoogleClient;
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import org.mockito.ArgumentMatchers;


@RunWith(MockitoJUnitRunner.class)
public final class CalendarTest {

  // private final LocalServiceTestHelper helper =
  //     new LocalServiceTestHelper(
  //       new LocalDatastoreServiceTestConfig(),
  //       new LocalUserServiceTestConfig());

  @Mock
  List<String> mockList;

  @Before
  public void setUp() {
    // helper.setUp();
  }

  @After
  public void tearDown() {
    // helper.tearDown();
  }

  @Test
  public void tempCalTest() {
    // when(new Calendar.Builder(
    //     any(HttpTransport.class), // new NetHttpTransport(),
    //     any(JsonFactory.class), // new JacksonFactory(),
    //     any(Credential.class) // user.getCredential()
    // ));
    // .thenReturn();

    // Calendar mockCalendar = mock(Calendar.class);
    // when(
    //   new Calendar.Builder(
    //     new NetHttpTransport(),
    //     new JacksonFactory(),
    //     newMockCredential()
    //   )
    //   .build()
    // ).thenReturn(mockCalendar);

    // Calendar mockCalendar = mock(Calendar.class);
    // Calendar.Builder mockCalendarBuilder = mock(Calendar.Builder.class);
    // when(any(Calendar.Builder.class).build()).thenReturn(mockCalendar);

    // when(CalendarManager.getCalendar(any(User.class)));

    // User mockHostUser = mock(User.class);
    // when(mockHostUser.isAuthenticated()).thenReturn(true);
    // when(mockHostUser.getCredential()).thenReturn(newMockCredential());
    // User mockGuestUser = mock(User.class);
    // Calendar mockCalendar = mock(Calendar.class);
    // Event mockEvent = mock(Event.class);
    // try {
    //   when(mockCalendar.events().insert(anyString(), mockEvent).execute())
    //     .thenThrow(IllegalStateException.class);
    //   CalendarManager.createMatchEvent(mockHostUser, mockGuestUser);
    // } catch (Exception e) {
    //   System.out.println("Caught exception during test");
    //   Assert.assertEquals("a", "b");
    // }

    // MockGoogleCredential mockCredential = newMockCredential();
    // when(mockUser.getCredential()).thenReturn("adam");

  /* Brenda:
    when(Calendar.Builder(
      any(HttpTransport.class),
      any(JsonFactory.class),
      any(HttpRequestInitializer.class))
    )).thenReturn (Your own calendar object);

    Calendar.newBuilder.().build();
  */

    Assert.assertEquals("hello", "hello");
  }

  // @Test
  public void tempMockitoTest() {
    mockList.add("one");
    Mockito.verify(mockList).add("one");
    Assert.assertEquals(0, mockList.size());

    Mockito.when(mockList.size()).thenReturn(100);
    Assert.assertEquals(100, mockList.size());

    ArrayList arrayList = mock(ArrayList.class);
    when(arrayList.get(0)).thenReturn("first");
    Assert.assertEquals(arrayList.get(0), "first");
  }

  @Test
  public void testUserNotAuthenticated() {
    User mockUser = mock(User.class);
    Assert.assertFalse(mockUser.isAuthenticated());
  }

  // @Test
  public void testUserIsAuthenticated() {
    User mockUser = mock(User.class);
    // when(OAuth2Utilities.isUserAuthenticated(anyString())).thenReturn(true);
    // Assert.assertTrue(mockUser.isAuthenticated());
    Assert.assertEquals("hello", "hello");
  }

  // Test that createMatchEvent() throws IllegalStateException if the host user isn't authenticated
  @Test(expected = IllegalStateException.class)
  public void testCreateEventHostNotAuthenticated() {
    User mockHostUser = mock(User.class);
    User mockGuestUser = mock(User.class);
    CalendarManager.createMatchEvent(mockHostUser, mockGuestUser, 0, 0, 0, 0, 0);
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
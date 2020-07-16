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
import java.util.Arrays;
import java.util.List;

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

// createGCalEvent();
// gcal java api javadoc: https://developers.google.com/resources/api-libraries/documentation/calendar/v3/java/latest/
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.client.http.BasicAuthentication; // https://googleapis.dev/java/google-http-client/latest/index.html?com/google/api/client/http/HttpRequestInitializer.html
import com.google.api.client.http.HttpTransport; // https://googleapis.dev/java/google-http-client/latest/com/google/api/client/http/HttpTransport.html
  import com.google.api.client.extensions.appengine.http.UrlFetchTransport;

// https://github.com/pschuette22/Zeppa-AppEngine/blob/44e15520a3b08f507ba331a5651e3871fc454f4e/zeppa-api/src/main/java/com/zeppamobile/api/googlecalendar/GoogleCalendarUtils.java#L42
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential; // https://googleapis.dev/java/google-api-client/latest/com/google/api/client/googleapis/extensions/appengine/auth/oauth2/package-frame.html
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential.AppEngineCredentialWrapper;
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential; // https://googleapis.dev/java/google-api-client/latest/com/google/api/client/googleapis/testing/auth/oauth2/MockGoogleCredential.html
// import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential.Builder; // couldn't find maven dependency to make it compile

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

    CalendarQuickstart calendar = new CalendarQuickstart();
    try {
      // calendar.main();
    } catch (Exception e) {
      System.out.println("there was in error in OAuthServlet.doGet() when accessing CalendarQuickstart");
      e.printStackTrace();
    }

    createGCalEvent(); System.out.println("createGCalEvent()");
    // tryAppEngineCredentials();
    // tryGoogleCredential();
    // tryAppIdentityCredential();

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  // Sample code from: https://developers.google.com/calendar/create-events
  // Refer to the Java quickstart on how to setup the environment:
  // https://developers.google.com/calendar/quickstart/java
  // Change the scope to CalendarScopes.CALENDAR and delete any stored
  // credentials.
  private void createGCalEvent() {
    Event event = new Event()
        .setSummary("Google I/O 2015")
        .setLocation("800 Howard St., San Francisco, CA 94103")
        .setDescription("A chance to hear more about Google's developer products.");

    DateTime startDateTime = new DateTime("2020-07-09T09:00:00-07:00");
    EventDateTime start = new EventDateTime()
        .setDateTime(startDateTime)
        .setTimeZone("America/Los_Angeles");
    event.setStart(start);

    DateTime endDateTime = new DateTime("2020-07-09T17:00:00-07:00");
    EventDateTime end = new EventDateTime()
        .setDateTime(endDateTime)
        .setTimeZone("America/Los_Angeles");
    event.setEnd(end);

    // String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
    // event.setRecurrence(Arrays.asList(recurrence));

    EventAttendee[] attendees = new EventAttendee[] {
        new EventAttendee().setEmail("lpage@example.com"),
        new EventAttendee().setEmail("sbrin@example.com"),
    };
    event.setAttendees(Arrays.asList(attendees));

    // EventReminder[] reminderOverrides = new EventReminder[] {
    //     new EventReminder().setMethod("email").setMinutes(24 * 60),
    //     new EventReminder().setMethod("popup").setMinutes(10),
    // };
    // Event.Reminders reminders = new Event.Reminders()
    //     .setUseDefault(false)
    //     .setOverrides(Arrays.asList(reminderOverrides));
    // event.setReminders(reminders);

    /*  public Calendar(com.google.api.client.http.HttpTransport transport,
                    com.google.api.client.json.JsonFactory jsonFactory,
                    com.google.api.client.http.HttpRequestInitializer httpRequestInitializer)   */
    // String dummyUsername = "adam";
    // String dummyPassword = "1234";
    // Calendar service = new Calendar(new UrlFetchTransport(), // TODO fill this in
    //     new JacksonFactory(),
    //     new BasicAuthentication(dummyUsername, dummyPassword)); // HttpRequestInitializer is an interface

    // FROYO tech help - https://github.com/pschuette22/Zeppa-AppEngine/blob/44e15520a3b08f507ba331a5651e3871fc454f4e/zeppa-api/src/main/java/com/zeppamobile/api/googlecalendar/GoogleCalendarUtils.java#L42
    JsonFactory FACTORY = new JacksonFactory();
    HttpTransport TRANSPORT = new NetHttpTransport();
    // AppIdentityCredential credential = makeServiceAccountCredential();
    GoogleCredential credential = null;
    try {
       credential = AppEngineCredentialWrapper.getApplicationDefault();
      //  if(credential.createScopeRequired()) {
      //     List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);
      //     credential = credential.createScoped(scopes);
      //  }
    } catch (Exception e) {
      System.out.println("There was a problem with GoogleCredential");
      e.printStackTrace();
    }
    Calendar service = new Calendar.Builder(TRANSPORT, FACTORY, credential).setApplicationName("Zeppa").build();

    // Try MockGoogleCredential
    // credential = new MockGoogleCredential(new MockGoogleCredential.Builder.build());
    // Calendar service = new Calendar.Builder(TRANSPORT, FACTORY, credential).setApplicationName("Zeppa").build();

    // Sample code from: https://developers.google.com/calendar/v3/reference/calendars/insert
    // Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
    //     .setApplicationName("applicationName").build();

    try {
      String calendarId = "primary";
      event = service.events().insert(calendarId, event).execute();
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (Exception e) {
      System.out.println("there was in error in OAuthServlet.doGet() -> createGCalEvent()");
      e.printStackTrace();
    }
  }

  private static AppIdentityCredential makeServiceAccountCredential() {
    List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);
		AppIdentityCredential credential = new AppIdentityCredential(SCOPES);
		return credential;
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

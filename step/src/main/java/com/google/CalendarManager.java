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
import java.util.ArrayList;

// Google Calendar API imports
import com.google.api.services.calendar.Calendar;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.CalendarScopes;

// Authorization imports
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.auth.oauth2.Credential;

/*
  Usage:
    General:
      CalendarManager calendar = new CalendarManager();
      calendar.setUser(String userId);

    Creating a match event:
      // CalendarManager calendar = new CalendarManager();
      CalendarManager.createMatchEvent(String userId1, String userId2);

    Remove a match event (for later implementation):
      CalendarManager calendar = new CalendarManager();
      calendar.removeMatchEvent(String userId1, String userId2);
*/

public class CalendarManager {
  private CalendarManager() {}

  // TODO: method to return required scope to OAuth2Servlet
  // TODO: method to return true/false is user is authenticated (javascript support too)
  // TODO: delete extra maven imports

  public static void createMatchEvent(String userId1, String userId2) {
    // Check if user1 is authenticated
    // Check if user2 is authenticated
  }

  public static void createTestGCalEvent(Credential googleCalendarCredential) {
    System.out.println("CalendarManager.createTestGCalEvent()");

    Event event = new Event()
        .setSummary("Google I/O 2015")
        .setLocation("800 Howard St., San Francisco, CA 94103")
        .setDescription("A chance to hear more about Google's developer products.");

    DateTime startDateTime = new DateTime("2020-07-16T09:00:00-07:00");
    EventDateTime start = new EventDateTime()
        .setDateTime(startDateTime)
        .setTimeZone("America/Los_Angeles");
    event.setStart(start);

    DateTime endDateTime = new DateTime("2020-07-16T17:00:00-07:00");
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

    Calendar service = new Calendar.Builder(
      new NetHttpTransport(),
      new JacksonFactory(),
      googleCalendarCredential
    ).setApplicationName("Friend Matching Plus").build(); // TODO: not sure iff app name matters

    try {
      String calendarId = "primary";
      event = service.events().insert(calendarId, event).execute();
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (Exception e) {
      System.out.println("there was in error in CalendarManager.createTestGCalEvent()");
      e.printStackTrace();
    }
  }

  public static List<String> getScopes() {
    List<String> scopes = new ArrayList<>();
    scopes.add(CalendarScopes.CALENDAR_EVENTS); // "View and edit events on all your calendars"
    return scopes;
  }
}

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
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.CalendarScopes;

// TODO: method to return true/false is user is authenticated (javascript support too)
// TODO: add javascript support for checking if a user is validated
// TODO: add test cases
// TODO: conform to Google Java style

public class CalendarManager {
  private CalendarManager() {}

  public static void createMatchEvent(String hostUserId, String guestUserId,
                                      int year, int month, int day, int hour, int minute) {
    createMatchEvent(new User(hostUserId), new User(guestUserId),
                      year, month, day, hour, minute);
  }

  public static void createMatchEvent(User hostUser, User guestUser,
                                      int year, int month, int day, int hour, int minute) {
    if (!hostUser.isAuthenticated()) {
      throw new IllegalStateException("The host user isn't authenticated. "
                                      + "Unable to create match event.");
    }

    Event matchEvent = new MatchEventBuilder()
      .setAttendees(hostUser, guestUser)
      // .setStartDateTime(2020, 7, 21, 9, 0)
      .setStartDateTime(year, month, day, hour, minute)
      .build();

    pushMatchEvent(hostUser, guestUser, matchEvent);
  }

  public static List<String> getScopes() {
    List<String> scopes = new ArrayList<>();
    scopes.add(CalendarScopes.CALENDAR_EVENTS); // "View and edit events on all your calendars"
    return scopes;
  }

  // hostUser is the event owner and guestUser will receive an email invite
  private static void pushMatchEvent(User hostUser, User guestUser, Event event) {
    Calendar calendar = getCalendar(hostUser);
    try {
      String calendarId = "primary";
      event = calendar.events().insert(calendarId, event).execute();
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (Exception e) {
      System.out.println("Unable to push match event.");
      e.printStackTrace();
    }
  }

  private static Calendar getCalendar(User user) {
    return new Calendar.Builder(
        new NetHttpTransport(),
        new JacksonFactory(),
        user.getCredential()
      ).setApplicationName("Friend Matching Plus").build(); // TODO: not sure if app name matters
  }

}

class MatchEventBuilder {
  private Event matchEvent;

  public MatchEventBuilder() {
    matchEvent = new Event();
  }

  public MatchEventBuilder setAttendees(User hostUser, User guestUser) {
    matchEvent.setSummary("FMP: " + hostUser.getName() + " / " + guestUser.getName());

    matchEvent.setDescription(hostUser.getName() + " and " + guestUser.getName()
                              + " have matched on Friend Matching Plus!");

    matchEvent.setAttendees(Arrays.asList(new EventAttendee[] {
      new EventAttendee().setEmail(guestUser.getEmail())
    }));

    return this;
  }

  // ZonedDateTime guide: https://www.baeldung.com/java-8-date-time-intro
  public MatchEventBuilder setStartDateTime(int year, int month, int day, int hour, int minute) {
    String dateTimeString = dateTimeString(year, month, day, hour, minute);

    EventDateTime start = new EventDateTime()
      .setDateTime(new DateTime(dateTimeString));
      // .setTimeZone("America/Los_Angeles"); // TODO how to configure time zones?
    matchEvent.setStart(start);

    // Assume a 1 hour event.
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, dateTimeFormatter);
    zonedDateTime = zonedDateTime.plusHours(1);
    dateTimeString = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    EventDateTime end = new EventDateTime()
      .setDateTime(new DateTime(dateTimeString));
      // .setTimeZone("America/Los_Angeles"); // TODO how to configure time zones?
    matchEvent.setEnd(end);

    return this;
  }

  public Event build() {
    return matchEvent;
  }

  // createDateTimeString(2020, 07, 21, 9, 0, 0) -> "2020-07-21T09:00:00-05:00"
  private static String dateTimeString(int year, int month, int day, int hour, int minute) {
    int timeZone = -5; // central time zone. TODO(adamsamuelson): eliminate timezones

    if (timeZone < 0) {
      timeZone *= -1;
      return String.format("%04d-%02d-%02dT%02d:%02d:%02d-%02d:%02d",
                      year, month, day, hour, minute, 0, timeZone, 0);
    } else {
      return String.format("%04d-%02d-%02dT%02d:%02d:%02d+%02d:%02d",
                      year, month, day, hour, minute, 0, timeZone, 0);
    }
  }
}


/*

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
    ).setApplicationName("Friend Matching Plus").build(); // TODO: not sure if app name matters

    try {
      String calendarId = "primary";
      event = service.events().insert(calendarId, event).execute();
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (Exception e) {
      System.out.println("there was in error in CalendarManager.createTestGCalEvent()");
      e.printStackTrace();
    }
  }

*/
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

import java.io.IOException;
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
      .setStartDateTime(year, month, day, hour, minute)
      .build();

    pushMatchEvent(hostUser, guestUser, matchEvent);
  }

  public static List<String> getScopes() {
    List<String> scopes = new ArrayList<>();
    scopes.add(CalendarScopes.CALENDAR_EVENTS); // "View and edit events on all your calendars"
    return scopes;
  }

  /**
   * Push the given event to the hostUser's Google Calendar and invite the guestUser to it.
   *
   * @param hostUser the owner of the Google Calendar event.
   * @param guestUser the user to be invited to the Google Calendar event.
   * @param event the event to be pushed to Google Calendar. This event can be conveniently
   *              built using CalendarManager.MatchEventBuilder.
   */
  private static void pushMatchEvent(User hostUser, User guestUser, Event event) {
    Calendar calendar = getCalendar(hostUser);
    try {
      String calendarId = "primary";
      event = calendar.events().insert(calendarId, event).execute();
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (IOException e) {
      System.out.println("ERROR: " + e.getMessage());
      System.out.println("Unable to push match event.");
    }
  }

  private static Calendar getCalendar(User user) {
    return new Calendar.Builder(
        new NetHttpTransport(),
        new JacksonFactory(),
        user.getCredential()
      ).setApplicationName("Friend Matching Plus").build();
  }

}

class MatchEventBuilder {
  private Event matchEvent;
  private static final int DEFAULT_EVENT_LENGTH_MINUTES = 60;

  public MatchEventBuilder() {
    matchEvent = new Event();
  }

  public MatchEventBuilder setAttendees(User hostUser, User guestUser) {
    String eventSummary = String.format("FMP: %s / %s", hostUser.getName(), guestUser.getName());
    matchEvent.setSummary(eventSummary);

    String eventDescription = String.format("%s and %s have matched on Friend Matching Plus! ",
                                              hostUser.getName(), guestUser.getName());
    eventDescription += "You can join a video call with your match by clicking "
                        + "\"Join with Google Meet\"";
    matchEvent.setDescription(eventDescription);

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
    zonedDateTime = zonedDateTime.plusMinutes(DEFAULT_EVENT_LENGTH_MINUTES);
    dateTimeString = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    EventDateTime end = new EventDateTime()
      .setDateTime(new DateTime(dateTimeString));
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

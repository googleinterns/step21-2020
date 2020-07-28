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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
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

  /**
   * Given a hostUser, guestUser, and a date and time, create a Google Calendar event on the
   * hostUser's primary calendar and send an invite to the guestUser. The guestUser will see an
   * event invite on their Google Calendar and will also receive an email from Google Calendar.
   *
   * @param hostUser the owner of the Google Calendar Event
   * @param guestUser the user to be invited to the Google Calendar Event
   */
  public static void createMatchEvent(User hostUser, User guestUser,
                                      int year, int month, int day, int hour, int minute) {
    if (!hostUser.isAuthenticated()) {
      throw new IllegalStateException("The host user isn't authenticated. "
                                      + "Unable to create match event.");
    } else if (guestUser == null) {
      throw new IllegalStateException("guestUser cannot be null.");
    } else if (!checkDateTimeConfiguration(year, month, day, hour, minute)) {
      throw new IllegalStateException("Received an invalid date/time configuration");
    }

    Event matchEvent = new MatchEventBuilder()
      .setAttendees(hostUser, guestUser)
      .setStartDateTime(year, month, day, hour, minute)
      .build();

    pushMatchEvent(hostUser, guestUser, matchEvent);
  }

  /**
   * @return the Google Calendar API scopes that we need access to.
   */
  public static List<String> getScopes() {
    List<String> scopes = new ArrayList<>();
    scopes.add(CalendarScopes.CALENDAR_EVENTS); // "View and edit events on all your calendars"
    scopes.add(CalendarScopes.CALENDAR_SETTINGS_READONLY); // "View your Calendar settings."
    return scopes;
  }

  public static Calendar getCalendar(User user) {
    return new Calendar.Builder(
        new NetHttpTransport(),
        new JacksonFactory(),
        user.getCredential()
      ).setApplicationName("Friend Matching Plus").build();
  }

  /**
   * @param user the user whose timezone is being returned. Requires this user to be authenticated.
   * @return a String of the user's timezone in IANA time zone format (ex: "America/Los_Angeles").
   *         Return null if unable to get the user's timezone.
   */
  public static String getUserTimezone(User user) {
    if (!user.isAuthenticated()) {
      throw new IllegalStateException("User isn't authenticated. Unable to get the user's timezone.");
    }

    try {
      return CalendarManager.getCalendar(user).settings().get("timezone").execute().getValue();
    } catch (IOException e) {
      System.err.println("ERROR: " + e.getMessage());
      System.err.println("Unable to get the user's timezone.");
      return null;
    }
  }

  /**
   * Push the given event to the hostUser's Google Calendar and invite the guestUser to it.
   * guestUser will receive an email invite as well as an event invite on their Google Calendar.
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
      event = calendar.events().insert(calendarId, event).setSendNotifications(true).execute();
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (IOException e) {
      System.err.println("ERROR: " + e.getMessage());
      System.err.println("Unable to push match event.");
    }
  }

  // Return true when the date/time configuration is valid, false otherwise.
  private static boolean checkDateTimeConfiguration(int year, int month, int day, int hour, int minute) {
    if(year < 2020 || year > 3000) {
      return false;
    } else if (month < 1 || month > 12) {
      return false;
    } else if (day < 1 || day > 31) {
      return false;
    } else if (hour < 1 || hour > 23) { 
      return false;
    } else if (minute < 0 || minute > 59) {
      return false;
    }

    return true;
  }

}

/* A builder class to create a match event.
 * Example:
 * Event matchEvent = new MatchEventBuilder()
 *    .setAttendees(hostUser, guestUser)
 *    .setStartDateTime(year, month, day, hour, minute)
 *    .build();
 */
class MatchEventBuilder {
  private static final int DEFAULT_EVENT_LENGTH_MINUTES = 60;
  private Event matchEvent;
  private String hostTimezone;

  public MatchEventBuilder() {
    matchEvent = new Event();
  }

  /**
   * @param hostUser the owner of the Google Calendar event.
   * @param guestUser the invitee of the Google Calendar event.
   */
  public MatchEventBuilder setAttendees(User hostUser, User guestUser) {
    String eventSummary = String.format("FMP: %s : %s", hostUser.getName(), guestUser.getName());
    matchEvent.setSummary(eventSummary);

    String eventDescription = String.format("%s and %s have matched on Friend Matching Plus! ",
                                              hostUser.getName(), guestUser.getName());
    eventDescription += "You can join a video call with your match by clicking "
                        + "\"Join with Google Meet\"";
    matchEvent.setDescription(eventDescription);

    matchEvent.setAttendees(Arrays.asList(new EventAttendee[] {
      new EventAttendee().setEmail(guestUser.getEmail())
    }));

    hostTimezone = CalendarManager.getUserTimezone(hostUser);

    return this;
  }

  /**
   * Set the start date/time of the match event. Assume a 1 hour event.
   */
  public MatchEventBuilder setStartDateTime(int year, int month, int day, int hour, int minute) {
    String dateTimeString = dateTimeString(year, month, day, hour, minute);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.of(hostTimezone)); // apply host's timezone
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, dateTimeFormatter);

    // Set start date/time.
    dateTimeString = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    EventDateTime start = new EventDateTime().setDateTime(new DateTime(dateTimeString));
    matchEvent.setStart(start);

    // Assume a 1 hour event.
    zonedDateTime = zonedDateTime.plusMinutes(DEFAULT_EVENT_LENGTH_MINUTES);
    
    // Set end date/time.
    dateTimeString = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    EventDateTime end = new EventDateTime().setDateTime(new DateTime(dateTimeString));
    matchEvent.setEnd(end);

    return this;
  }

  public Event build() {
    return matchEvent;
  }

  /**
   * Create a date/time string following the RFC3339 format (example: "2020-07-21T09:00:00-05:00").
   *
   * @return a date/time string with it's timezone offset set to a dummy value. The caller is
   *         expected to overwrite the timezone offset.
   */
  private static String dateTimeString(int year, int month, int day, int hour, int minute) {
    return String.format("%04d-%02d-%02dT%02d:%02d:%02d+%02d:%02d",
                          year, month, day, hour, minute, 0, 0, 0);
  }
}

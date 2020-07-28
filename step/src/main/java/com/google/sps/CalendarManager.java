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
    scopes.add(CalendarScopes.CALENDAR); // "See, edit, share, and permanently delete all the calendars you can access using Google Calendar."
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

class MatchEventBuilder {
  private Event matchEvent;
  private static final int DEFAULT_EVENT_LENGTH_MINUTES = 60;
  private String timeZone;

  public MatchEventBuilder() {
    matchEvent = new Event();
  }

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

    // timeZone = getCalendar(hostUser).getTimeZone();
    timeZone = getUserTimezone(hostUser);
    System.out.println("hostUser's timezone: " + timeZone);

    return this;
  }

  // ZonedDateTime guide: https://www.baeldung.com/java-8-date-time-intro
  public MatchEventBuilder setStartDateTime(int year, int month, int day, int hour, int minute) {
    String dateTimeString = dateTimeString(year, month, day, hour, minute);
    // String dateTimeString = dateTimeString(year, month, day, hour, minute, timeZone);

    // DateTime dateTime = new DateTime(
    //   new Date(year - 1900, month - 1, day, hour, minute),
    //   TimeZone.getTimeZone(timeZone));

    // EventDateTime start = new EventDateTime()
    //   .setDateTime(new DateTime(dateTimeString))
    //   // .setDateTime(dateTime)
    //   .setTimeZone(timeZone); // TODO how to configure time zones?
    // matchEvent.setStart(start);
    // System.out.println("dateTimeString: " + dateTimeString);
    // System.out.println("start.toString: " + start.toString());
    // System.out.println("start.getTimeZone(): " + start.getTimeZone());



    // Assume a 1 hour event.
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    dateTimeFormatter = dateTimeFormatter.withZone(ZoneId.of(timeZone));
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, dateTimeFormatter);
    System.out.println("start string: " + zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

    EventDateTime start = new EventDateTime()
      .setDateTime(new DateTime(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
    matchEvent.setStart(start);

    zonedDateTime = zonedDateTime.plusMinutes(DEFAULT_EVENT_LENGTH_MINUTES);
    dateTimeString = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    System.out.println("end string: " + dateTimeString);
    
    // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    // LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    // localDateTime = localDateTime.plusMinutes(DEFAULT_EVENT_LENGTH_MINUTES);
    // dateTimeString = localDateTime.format(dateTimeFormatter);

    // dateTime = new DateTime(
    //   new Date(localDateTime.getYear() - 1900, localDateTime.getMonthValue() - 1, localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute()),
    //   // new Date(2020, 7, 27, 13, 0),
    //   TimeZone.getTimeZone(timeZone));

    EventDateTime end = new EventDateTime()
      // .setDateTime(dateTime)
      .setDateTime(new DateTime(dateTimeString));
      // .setTimeZone(timeZone);
    matchEvent.setEnd(end);

    return this;
  }

  public Event build() {
    return matchEvent;
  }

  public static String getUserTimezone(User user) {
    String timezone = "";
    try {
      Calendar calendar = CalendarManager.getCalendar(user);
      Calendar.Settings.Get settings = calendar.settings().get("timezone");
      com.google.api.services.calendar.model.Setting modelSetting = settings.execute();
      System.out.println("modelSetting.getValue(): " + modelSetting.getValue());
      timezone = modelSetting.getValue();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return timezone;
  } 

  // createDateTimeString(2020, 07, 21, 9, 0, 0) -> "2020-07-21T09:00:00-05:00"
  private static String dateTimeString(int year, int month, int day, int hour, int minute) {
    // return String.format("%04d-%02d-%02dT%02d:%02d:%02d",
    //                   year, month, day, hour, minute, 0);
    return String.format("%04d-%02d-%02dT%02d:%02d:%02d+%02d:%02d",
                      year, month, day, hour, minute, 0, 0, 0);
  }

  private static String dateTimeString(int year, int month, int day, int hour, int minute, String timezone) {
    TimeZone timeZone = TimeZone.getTimeZone(timezone);
    System.out.println("timeZone.getOffset(0): " + timeZone.getOffset(0));
    int timeZoneOffset = -5; // central time zone. TODO(adamsamuelson): eliminate timezones

    if (timeZoneOffset < 0) {
      timeZoneOffset *= -1;
      return String.format("%04d-%02d-%02dT%02d:%02d:%02d-%02d:%02d",
                      year, month, day, hour, minute, 0, timeZoneOffset, 0);
    } else {
      return String.format("%04d-%02d-%02dT%02d:%02d:%02d+%02d:%02d",
                      year, month, day, hour, minute, 0, timeZoneOffset, 0);
    }
  }
}

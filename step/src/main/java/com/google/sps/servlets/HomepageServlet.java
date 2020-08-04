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

package com.google.sps.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import com.google.sps.DatabaseHandler;
import com.google.sps.User;
import com.google.sps.Notification;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/Homepage")
public class HomepageServlet extends HttpServlet {

  private static final int NUM_NOTIFS_TO_DISPLAY = 10;
  private static final String MATCHES = "matches";
  private static final String NOTIFICATIONS = "notifications";
  private static final String STATUS = "status";
  private static final String IMAGE = "image";
  private static final String PENDING = "pending";
  private static final String NOT_PENDING = "not pending";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String id = userService.getCurrentUser().getUserId();
    User user = new User(id);

    JSONObject userData = new JSONObject();
    userData.put(MATCHES, populateMatchesArray(user));
    userData.put(NOTIFICATIONS, populateNotificationArray(id));
    userData.put(STATUS, getMatchStatus(user));
    userData.put(IMAGE, DatabaseHandler.getUserImageUrl(id));

    response.getWriter().println(userData);
  }


  // Method for fetching a user's matches and handling potential errors
  private Collection<User> fetchUserMatches(User user) {
    Collection<User> userMatches;
    try {
      userMatches = user.getMatches();
    } catch(DatastoreNeedIndexException e) {
      System.err.println("Error occured while fetching user's matches.");
      userMatches = new ArrayList<>();
    }
    return userMatches;
  }

  // Method for fetching a user's notifications and handling potential errors
  private Collection<Notification> fetchUserNotifications(String id) {
    Collection<Notification> notifications;
    try {
      notifications = DatabaseHandler.getUserNotifications(id);
    } catch(DatastoreNeedIndexException e) {
      System.err.println("Error occured while fetching user's notifications.");
      notifications = new ArrayList<>();
    }
    return notifications;
  }

  // Method for populating a JSON array with all relavant information about a
  // user's matches
  private JSONArray populateMatchesArray(User user) {
    Collection<User> matches = fetchUserMatches(user);
    JSONArray matchesArray = new JSONArray();

    for (User match: matches) {
      JSONObject matchJson = new JSONObject();
      matchJson.put("name", match.getName());
      matchJson.put("email", match.getEmail());
      matchJson.put("image", match.getImageUrl());
      matchesArray.add(matchJson);
    }

    return matchesArray;
  }

  // Method for populating a JSON array with all relavant information about a user's
  // notifications
  private JSONArray populateNotificationArray(String id) {
    Collection<Notification> notifications = fetchUserNotifications(id);
    JSONArray notificationsArray = new JSONArray();
    int notificationCounter = 0;

    for (Notification notification: notifications) {
      if (notificationCounter == NUM_NOTIFS_TO_DISPLAY) {
        break;
      }
      notificationsArray.add(notification.getText());
      notificationCounter += 1;
    }

    return notificationsArray;
  }

  // Method for getting the right match status to put in the JSON based on whether the user
  // is waiting on a match
  private String getMatchStatus(User user) {
    boolean matchPending = user.isMatchPending();

    if (matchPending) {
      return PENDING;
    } else {
      return NOT_PENDING;
    }
  }


}

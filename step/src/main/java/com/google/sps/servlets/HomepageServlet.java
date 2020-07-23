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
  private static final String STATUS = "status";
  private static final String PENDING = "pending";
  private static final String NOT_PENDING = "not pending";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
   // response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    UserService userService = UserServiceFactory.getUserService();
    String id = userService.getCurrentUser().getUserId();
    User user = new User(id);

    Collection<User> userMatches;
    Collection<Notification> notifications;
    try {
      userMatches = user.getMatches();
    } catch(DatastoreNeedIndexException e) {
      userMatches = new ArrayList<>();
    }

    try {
      notifications = DatabaseHandler.getUserNotifications(id);
    } catch(DatastoreNeedIndexException e) {
      notifications = new ArrayList<>();
   }
    
    JSONArray matchesArray = new JSONArray();
    JSONArray notificationsArray = new JSONArray();

    for (User match: userMatches) {
      JSONObject matchJson = new JSONObject();
      matchJson.put("name", match.getName());
      matchJson.put("email", match.getEmail());
      matchesArray.add(matchJson);
    }

    int notificationCounter = 0;
    for (Notification notification: notifications) {
      if (notificationCounter == NUM_NOTIFS_TO_DISPLAY) {
        break;
      }
      notificationsArray.add(notification.getText());
      notificationCounter += 1;
    }

    boolean matchPending = user.isMatchPending();

    JSONObject userData = new JSONObject();
    userData.put("matches", matchesArray);
    userData.put("notifications", notificationsArray);

    if (matchPending) {
      userData.put(STATUS, PENDING);
    } else {
      userData.put(STATUS, NOT_PENDING);
    }

    out.println(userData);
  }

}

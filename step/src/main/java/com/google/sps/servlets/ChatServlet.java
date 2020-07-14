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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Message;
import com.google.sps.data.Conversation;
import java.io.IOException;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet responsible for listing tasks. */
@WebServlet("/Chat")
public class ChatServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Query query = new Query("Conversation").addSort("timestamp", SortDirection.ASCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    List<Conversation> conversations = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        String id = (String) entity.getProperty("Sender");
        String otherUserID = (String) entity.getProperty("Recipient");
        String text = (String) entity.getProperty("Message");
        long timestamp = (long) entity.getProperty("timestamp");
        Conversation c = new Conversation(id, otherUserID, text, timestamp);
        conversations.add(c);
    }
    request.setAttribute("conversations", conversations);
    request.getRequestDispatcher("chat.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    String id = userService.getCurrentUser().getUserId();
    String otherUserID = "116864793199754962735"; // add a random user id
    String text = request.getParameter("text");
    long timestamp = System.currentTimeMillis();
    Entity messageEntity = new Entity("Conversation");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    messageEntity.setProperty("Sender",id);
    messageEntity.setProperty("Recipient", otherUserID);
    messageEntity.setProperty("Message", text);
    messageEntity.setProperty("timestamp", timestamp);
    datastore.put(messageEntity);
    doGet(request, response);
  }
}

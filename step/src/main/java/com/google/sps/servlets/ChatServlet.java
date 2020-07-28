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
import com.google.sps.Message;
import java.io.IOException;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import com.google.sps.DatabaseHandler;
import com.google.sps.MessageHandler;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** Servlet responsible for listing messages. */
@WebServlet("/Chat")
public class ChatServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UserService userService = UserServiceFactory.getUserService();
    String senderID = userService.getCurrentUser().getUserId();
    String recipientID = request.getParameter("user");
    List<Message> messages = new ArrayList<>();
    messages = MessageHandler.getMessages(senderID, recipientID);
    request.setAttribute("messages", messages);
    request.setAttribute("currUser", senderID);
    request.getRequestDispatcher("chat1.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UserService userService = UserServiceFactory.getUserService();
    String senderID = userService.getCurrentUser().getUserId();
    String recipientID = request.getParameter("user");
    String text = request.getParameter("text");
    long timestamp = System.currentTimeMillis();
    Message m = new Message(senderID, recipientID, text, timestamp);
    MessageHandler.addMessage(m);
    doGet(request, response);
  }
}

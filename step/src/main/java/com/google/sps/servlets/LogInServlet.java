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
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class LogInServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    UserService userService = UserServiceFactory.getUserService();

    // If user is not logged in, show a login form (could also redirect to a login page)
    if (!userService.isUserLoggedIn()) {
      String loginUrl = userService.createLoginURL("/Login");
      out.println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
      return;
    }

    boolean theFirstTime = firstTimeLogIn(userService.getCurrentUser().getUserId());
    if (theFirstTime) { 
      out.println("<h1 style=\"text-align: center; padding-top: 30vh; font-size: 80px\"> Welcome to Friend Matching Plus </h1>");
      out.println("<form method=\"POST\" action=\"/Login\" style=\"text-align: center;\">");
      out.println("<button style=\"color: #fff; background-color: #4285F4; border: none; font-size: 40px; padding: 20px 40px;\" > Next</button>");
      out.println("</form>");
    } else {
        response.sendRedirect("profile.jsp");
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    PrintWriter out = response.getWriter();
    out.println("Welcome to Friend Matching Plus");
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/Login");
      return;
    }
    String email = userService.getCurrentUser().getEmail();
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity("User", id);
    entity.setProperty("id", id);
    entity.setProperty("email", email);
    // The put() function automatically inserts new data or updates existing data based on ID
    datastore.put(entity);
    response.sendRedirect("infoForm.jsp");
  }

  // Returns if the user logs in for the first time. If yes, return true. Otherwise, false.
  private boolean firstTimeLogIn(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return true;
    }
    return false;
  }
}

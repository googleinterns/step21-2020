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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.DatabaseHandler;

@WebServlet("/Pref")
public class PrefServlet extends HttpServlet {
  // The class stores the user' personal information data
  private static final String UNKNOWN = "Unknown";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String q1 = (String) request.getParameter("q1") == null ? UNKNOWN : (String) request.getParameter("q1");
    String q2 = (String) request.getParameter("q2") == null ? UNKNOWN : (String) request.getParameter("q2");
    String q3 = (String) request.getParameter("q3") == null ? UNKNOWN : (String) request.getParameter("q3");
    String q4 = (String) request.getParameter("q4") == null ? UNKNOWN : (String) request.getParameter("q4");
    String q5 = (String) request.getParameter("q5") == null ? UNKNOWN : (String) request.getParameter("q5");

    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();

    if (entity == null) {
      response.sendRedirect("index.jsp");
    }

    // Ensuring that only users who have filled all of the info fields out are added to the database
    if (q1 != null && q2 != null && q3 != null && q4 != null &&
      q5 != null) {
      DatabaseHandler.addUserPref(id, q1, q2, q3, q4, q5);
    }
    response.sendRedirect("profile.jsp");
  }
}

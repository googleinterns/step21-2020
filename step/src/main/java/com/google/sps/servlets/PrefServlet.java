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

@WebServlet("/Pref")
public class PrefServlet extends HttpServlet {
  // The class stores the user' personal information data
  private static final String UNKNOWN = "Unknown";
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String q1 = request.getParameter("q1");
    if (q1 == null) {
      q1 = UNKNOWN;
    }

    String q2 = request.getParameter("q2");
    if (q2 == null) {
      q2 = UNKNOWN;
    }

    String q3 = request.getParameter("q3");
    if (q3 == null) {
      q3 = UNKNOWN;
    }

    String q4 = request.getParameter("q4");
    if (q4 == null) {
      q4 = UNKNOWN;
    }

    String q5 = request.getParameter("q5");
    if (q5 == null) {
      q5 = UNKNOWN;
    }

    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    entity.setProperty("q1", q1);
    entity.setProperty("q2", q2);
    entity.setProperty("q3", q3);
    entity.setProperty("q4", q4);
    entity.setProperty("q5", q5);
    datastore.put(entity);
    response.sendRedirect("profile.jsp");
  }
}

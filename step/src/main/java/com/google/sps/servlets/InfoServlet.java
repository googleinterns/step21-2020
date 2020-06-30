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

@WebServlet("/Info")
public class InfoServlet extends HttpServlet {
    // The class stores the user' personal information data
    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String fname = request.getParameter("fname");
    String lname = request.getParameter("lname");
    String phone =request.getParameter("phone");
    int dbirth = Integer.parseInt(request.getParameter("dbirth"));
    int mbirth = Integer.parseInt(request.getParameter("mbirth"));
    int ybirth = Integer.parseInt(request.getParameter("ybirth"));

    String email = userService.getCurrentUser().getEmail();
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    entity.setProperty("fname", fname);
    entity.setProperty("lname", lname);
    entity.setProperty("phone", phone);
    entity.setProperty("dbirth", dbirth);
    entity.setProperty("mbirth", mbirth);
    entity.setProperty("ybirth", ybirth);
    datastore.put(entity);
    response.sendRedirect("profile.jsp");
  }
}

/** Personal Information: 
    Date/Month/Year: Allow people to put 2-digit numbers only
    Picture: Allow users to add pictures
    Gender: Choose among different gender options
    Organization/ Country
    Phone Number: 10-digit number only
*/

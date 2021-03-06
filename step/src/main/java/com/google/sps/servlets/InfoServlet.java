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
import com.google.sps.DatabaseHandler;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

@WebServlet("/Info")
public class InfoServlet extends HttpServlet {
  // The class stores the user' personal information data
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    User user = UserServiceFactory.getUserService().getCurrentUser();
    HttpSession session = request.getSession();

    String firstName = request.getParameter("firstName");
    session.setAttribute("firstName", firstName);
    String lastName = request.getParameter("lastName");
    session.setAttribute("lastName", lastName);
    String dayBirth = (String) request.getParameter("dayBirth");
    session.setAttribute("dayBirth", dayBirth);
    String monthBirth = (String) request.getParameter("monthBirth");
    session.setAttribute("monthBirth", monthBirth);
    String yearBirth = (String) request.getParameter("yearBirth");
    String email = user.getEmail();
    String id = user.getUserId();
    
    // Ensuring that only users who have filled all of the info fields out are added to the database
    if (firstName != null && lastName != null && dayBirth != null && monthBirth != null &&
      yearBirth != null && email != null && id != null) {
      DatabaseHandler.addUser(firstName, lastName, dayBirth, monthBirth, yearBirth, email, id);
    }

    response.sendRedirect("prefForm.jsp");
  }
}

/** Personal Information: 
    Date/Month/Year: Allow people to put 2-digit numbers only
    Picture: Allow users to add pictures
    Gender: Choose among different gender options
    Organization/ Country
*/

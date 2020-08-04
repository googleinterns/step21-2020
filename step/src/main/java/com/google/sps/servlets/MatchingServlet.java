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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.sps.MatchManager;
import com.google.sps.User;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Collection;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.servlet.ServletException;


@WebServlet("/matching")
public class MatchingServlet extends HttpServlet {

  public static String REQUEST_TYPE = "request-type";
  public static String REQUEST_TYPE_MATCH = "request-type-match";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String requestType = request.getParameter(REQUEST_TYPE);

    if (requestType.equals(REQUEST_TYPE_MATCH)) {
      UserService userService = UserServiceFactory.getUserService();
      User currentUser = new User(userService.getCurrentUser().getUserId());
      MatchManager.generateMatch(currentUser);
    }
    doGet(request, response);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //Collect all the matches and display them in the left container in chat.jsp
    //Collect name, user_id, timestamp getting matched
    PrintWriter out = response.getWriter();
    UserService userService = UserServiceFactory.getUserService();
    String id = userService.getCurrentUser().getUserId();
    User user = new User(id);
    Collection<User> userMatches;
    try {
      userMatches = user.getMatches();
    } catch(DatastoreNeedIndexException e) {
      userMatches = new ArrayList<>();
    }
    //Send all the matches to the front-end
    request.setAttribute("matches", userMatches); 
    response.sendRedirect("profile.jsp");
    //request.getRequestDispatcher("chat.jsp").forward(request, response);
  }

}

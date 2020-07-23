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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.MatchManager;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@WebServlet("/matching")
public class MatchingServlet extends HttpServlet {

  public static String REQUEST_TYPE = "request-type";
  public static String REQUEST_TYPE_MATCH = "request-type-match";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String requestType = request.getParameter(REQUEST_TYPE);
    if (requestType.equals(REQUEST_TYPE_MATCH)) {
      UserService userService = UserServiceFactory.getUserService();
      com.google.appengine.api.users.User userServiceUser = userService.getCurrentUser();
      com.google.sps.User currentUser = new com.google.sps.User(userServiceUser.getUserId());
      MatchManager.generateMatch(currentUser);
    } 

    response.sendRedirect("/profile.jsp");
  }

}

<%--
Copyright 2019 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>

<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<% UserService userService = UserServiceFactory.getUserService();
   String urlToRedirectToAfterUserLogsOut = "/";
   String logoutURL = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut); %> 

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="style.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@700&display=swap" rel="stylesheet">
    <title>My Portfolio</title>
  </head>
  <body>
    <nav>
        <a href="<%= logoutURL %>"> Log Out </a>
    </nav>
    <h1>Friend Matching Plus </h1>
    <p>Here is your profile! </p>

    <% if (!userService.isUserLoggedIn()) {
        response.sendRedirect("index.jsp");   
    } %>

    <form action="/auth" method="get">
      <button type="submit">OAuth button</button>
      <!-- <button type="submit" id="oauthButton" formmethod="get">OAuth button</button> -->
    </form>

    <form action="/userapi" method="get">
      <button type="submit">userapi button</button>
    </form>

    <form action="/cal" method="get">
      <button type="submit">/cal button</button>
    </form>

  </body>
</html>

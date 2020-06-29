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

<!-- <%-- The Java code in this JSP file runs on the server when the user navigates
     to the homepage. This allows us to insert the Blobstore upload URL into the
     form without building the HTML using print statements in a servlet. --%> -->
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<% UserService userService = UserServiceFactory.getUserService();
   String urlToRedirectToAfterUserLogsIn = "/"; 
   String loginURL = userService.createLoginURL(urlToRedirectToAfterUserLogsIn); %> 

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="style.css">
    <title>Friend Matching Plus</title>
  </head>
  <body>
    <nav>
        <a class="active" href="index.jsp"> About Us </a>
        <a href="<%= loginURL %>"> Log In </a>
    </nav>

    <% if (userService.isUserLoggedIn()) {
        String userEmail = userService.getCurrentUser().getEmail();
        response.sendRedirect("Login");   
    } %>
  </body>
</html>

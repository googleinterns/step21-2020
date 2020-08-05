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
    <link rel="stylesheet" href="style_index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@900&display=swap" rel="stylesheet">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
    <title>Friend Matching Plus</title>
  </head>
  <body>
        <nav>
            <div> <a href="<%= loginURL %>" class="btn"> Log In </a> <div>
        </nav>
        <% if (userService.isUserLoggedIn()) {	
            String userEmail = userService.getCurrentUser().getEmail();	
            response.sendRedirect("Login");   	
        } %>
        
        <header class="showcase">
            <div class="container showcase-inner">
            <h1>Friend Matching Plus</h1>
            <t>Friending. Made easy.
            </t>
            <a href="#" class="btn">Read More</a>
            </div>
        </header>
  </body>
</html>

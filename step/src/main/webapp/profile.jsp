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
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
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
    <title>My Profile</title>
  </head>
  <body>
    <nav>
        <a href="<%= logoutURL %>"> Log Out </a>
    </nav>
    <h1>Friend Matching Plus </h1>

    <% if (!userService.isUserLoggedIn()) {
        response.sendRedirect("index.jsp");   
    } %>

    <%
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    %>

    <h2> Your Portfolio </h2>
    <div> 
        First Name: <%= (String) entity.getProperty("firstName")%>
        <br>
        Last Name: <%= (String) entity.getProperty("lastName")%>
        <br>
        Date of Birth: <%= String.valueOf(entity.getProperty("monthBirth"))%>/<%= String.valueOf(entity.getProperty("dayBirth"))%>/<%= String.valueOf(entity.getProperty("yearBirth"))%>
    </div>

    <div> 
        Question 1: Are you staying in the US now? <%= (String) entity.getProperty("q1")%>
        <br>
        Question 2: Do you have any pets? <%= (String) entity.getProperty("q2")%>
        <br>
        Question 3: Do you want to only be matched with someone from your institution/company? <%= (String) entity.getProperty("q3")%>
        <br>
        Question 4: <a href="https://en.wikipedia.org/wiki/The_dress#:~:text=The%20dress%20itself%20was%20confirmed,not%20available%20at%20the%20time." target="_blank"> Is the dress blue or gold?</a> <%= (String) entity.getProperty("q4")%>
        <br>
        Question 5: <a href="https://www.youtube.com/watch?v=7X_WvGAhMlQ" target="_blank"> Is it Yanny or Laurel?</a> <%= (String) entity.getProperty("q5")%>
        <br>
    </div>


  </body>
</html>

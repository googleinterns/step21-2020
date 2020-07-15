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
    <link rel="stylesheet" href="style_profile.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <title>Friend Matching Plus</title>

  </head>
  <body onload="getMatches()">
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
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    %>

    <h2> Your Profile </h2>
    <div class="container">
        <div class="sub-container" id="list-selection">
            <div id="profile-pic"> <img src="avatar.png" alt="Profile Picture"> </div>
            <div id="navbar-selection"> 
                <a href="#personal-container">Personal Information</a>
                <a href="#questionaire-container">Questionaire</a>
                <a href="#matches-container">Your Matches</a>
                <a href="#find-a-match-container">Find a match!</a>
            </div>
        </div>
        <div class="sub-container" id="profile-info"> 
            <h3> <a href="infoForm.jsp"> Personal Infomation </a> </h3>
            <div class="personal-container" id="personal-container">
                <div class="personal-item">
                    <div class="item-label"> First Name </div> 
                    <div class="item-info"> <%= (String) entity.getProperty("firstName")%> </div>
                </div>
                <div class="personal-item">
                    <div class="item-label">Last Name </div> 
                    <div class="item-info"><%= (String) entity.getProperty("lastName")%> </div> 
                </div>
                <div class="personal-item">
                    <div class="item-label">Email </div> 
                    <div class="item-info"><%= (String) entity.getProperty("email")%> </div> 
                </div>
                <div class="personal-item">
                    <div class="item-label">Date of Birth </div> 
                    <div class="item-info"><%= String.valueOf(entity.getProperty("monthBirth"))%>/<%= String.valueOf(entity.getProperty("dayBirth"))%>/<%= String.valueOf(entity.getProperty("yearBirth"))%> </div> 
                </div>
            </div>
            <h3> <a href="prefForm.jsp"> Questionaire </a> </h3>
            <div class="questionaire-container" id="questionaire-container"> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Are you staying in the US now? </div> 
                    <div class="item-info"><%= (String) entity.getProperty("q1")%> </div> 
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Do you have any pets? </div> 
                    <div class="item-info"> <%= (String) entity.getProperty("q2")%> </div> 
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Only be matched with someone from your institution? </div> 
                    <div class="item-info"> <%= (String) entity.getProperty("q3")%> </div> 
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> <a href="https://en.wikipedia.org/wiki/The_dress#:~:text=The%20dress%20itself%20was%20confirmed,not%20available%20at%20the%20time." target="_blank">  Is the dress blue or gold?</a> </div> 
                    <div class="item-info"> <%= (String) entity.getProperty("q4")%> </div>
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> <a href="https://www.youtube.com/watch?v=7X_WvGAhMlQ" target="_blank"> Is it Yanny or Laurel?</a> </div>
                    <div class="item-info"> <%= (String) entity.getProperty("q5")%> </div>
                </div> 
            </div>
            <h3> Your Matches </h3>
            <div class="matches-container" id="matches-container">
                    <div id="match-item"> </div>
            </div>
        </div>
            

        <div class="sub-container" id="page-right">
            <h3> Find a Match! </h3>
                
            <div id="find-a-match-container">
                <form class="match-button" action="/matching" method="post">
                    <input type="hidden" id="request-type" name="request-type" value="request-type-match">
                    <button type="submit" value="Submit">Find a match!</button>
                </form>
            </div>
            <h3> Notifications </h3>
            <div class="find-a-match-container">
                <div class="match-item" id="notification-container"></div>
                <br>
            </div>
        </div>
    </div>

        <script>
      function getMatches() {
        fetch('/Homepage')
          .then((response) => {
            return response.json();
          })
          .then((json) => {
            matches = json["matches"];
            notifications = json["notifications"];

            renderMatches(matches);
            renderNotifications(notifications);
          });
      }

      function renderMatches(matches) {
        const matchContainer = document.getElementById('match-item');
        matches.forEach(match => {
          name = match["name"];
          email = match["email"];

          const matchDiv = document.createElement('div');
          matchDiv.className = 'match-item';

          const nameElement = document.createElement('div');
          nameElement.innerText = name;
          nameElement.className = 'match-name';
          matchDiv.appendChild(nameElement);
          
          const emailElement = document.createElement('div');
          emailElement.innerText = email;
          emailElement.className = 'match-email';
          matchDiv.appendChild(emailElement);

          matchContainer.appendChild(matchDiv);

          const lineBreak = document.createElement('br');
          matchContainer.appendChild(lineBreak);
        });

      }

      function renderNotifications(notifications) {
        const notificationContainer = 
          document.getElementById('notification-container');
          
        notifications.forEach(notification => {
          const notificationElement = document.createElement('p');
          notificationElement.innerText = notification;
          notificationContainer.appendChild(notificationElement);

          const lineElement = document.createElement('hr');
          lineElement.className = 'horizontal-line';
          notificationContainer.appendChild(lineElement);
        });
      }
    </script> 
  </body>
</html>

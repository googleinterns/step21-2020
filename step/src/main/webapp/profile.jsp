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
    <title>Friend Matching Plus</title>
  </head>
  <body onload="getMatches()">
    <nav>
        <a href="<%= logoutURL %>"> Log Out </a>
    </nav>
    <h1>Friend Matching Plus </h1>

    <div class="column">
      <div id="match-container" class="row">
        Matches
      </div>
      <div id="notification-container" class="row">
        Notifications
      </div>
    </div>  



    <% if (!userService.isUserLoggedIn()) {
        response.sendRedirect("index.jsp");   
    } %>

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
        const matchContainer = document.getElementById('match-container');
        matches.forEach(match => {
          name = match["name"];
          email = match["email"];

          const matchDiv = document.createElement('div');

          const nameElement = document.createElement('p');
          nameElement.innerText = name;
          nameElement.className = 'match-name';
          matchDiv.appendChild(nameElement);
          
          const emailElement = document.createElement('p');
          emailElement.innerText = email;
          emailElement.className = 'match-email';
          matchDiv.appendChild(emailElement);

          matchDiv.className = 'match';
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

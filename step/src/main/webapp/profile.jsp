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
<% UserService userService = UserServiceFactory.getUserService();
   String urlToRedirectToAfterUserLogsOut = "/index.jsp";
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
  <body onload="setPage()">
    <nav>
        <a id="log-out-button" href="<%= logoutURL %>"> Log Out </a>
    </nav>
    <img src="logo.png" alt="logo" id="logo">

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

    String q1 = (String) entity.getProperty("q1");
    if (q1 == null) {
      q1 = "Unknown";
    }

    String q2 = (String) entity.getProperty("q2");
    if (q2 == null) {
      q2 = "Unknown";
    }

    String q3 = (String) entity.getProperty("q3");
    if (q3 == null) {
      q3 = "Unknown";
    }

    String q4 = (String) entity.getProperty("q4");
    if (q4 == null) {
      q4 = "Unknown";
    }

    String q5 = (String) entity.getProperty("q5");
    if (q5 == null) {
      q5 = "Unknown";
    }
    %>

    <div class="container">
        <div class="sub-container" id="list-selection">
            <h2> Your Profile </h2>
            <div id="profile-pic"> </div>
            <div id="navbar-selection"> 
                <a href="#personal-container">Personal Information</a>
                <a href="#questionaire-container">Questionaire</a>
                <a href="#matches-container">Your Matches</a>
                <a href="#find-a-match-container">Find a match!</a>
                <a href="#modal" id="update-pic">Update Profile Picture</a>

                <br>
            </div>
        </div>
        <div class="sub-container" id="profile-info"> 
            <h3> <a href="infoForm.jsp" style="#4B0082;"> Personal Infomation </a> </h3>
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
            <br><br>
            <h3> <a href="prefForm.jsp" style="#4B0082;"> Questionaire </a> </h3>
            <div class="questionaire-container" id="questionaire-container"> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Do you have any pets? </div>
                    <div class="item-info"><%= q1%> </div> 
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> <a href="https://en.wikipedia.org/wiki/The_dress#:~:text=The%20dress%20itself%20was%20confirmed,not%20available%20at%20the%20time." target="_blank">  Is the dress blue or gold?</a> </div> 
                    <div class="item-info"> <%= q2%> </div> 
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Would you rather win the lottery or work at the perfect job? </div>
                    <div class="item-info"> <%= q3%> </div> 
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Would you rather trade intelligence for looks or looks for intelligence? </div>
                    <div class="item-info"> <%= q4%> </div>
                </div> 
                <div class="questionaire-item"> 
                    <div class="item-label"> Is your glass half full or half empty? </div>
                    <div class="item-info"> <%= q5%> </div>
                </div> 
            </div>
            <br><br>
            <h3> Your matches </h3>
            <div class="matches-container" id="matches-container">
              <div id="match-item"> </div>
            </div>
        </div>
            

        <div class="sub-container" id="page-right">
                
            <div id="find-a-match-container">
                <form class="match-button" action="/matching" method="post">
                    <input type="hidden" id="request-type" name="request-type" value="request-type-match">
                    <button id="match-submit" type="submit" value="Submit">Find a match!</button>
                    <p id="match-status"></p>
                </form>
            </div>
            <h3> Notifications </h3>
            <div class="find-a-match-container">
                <div class="match-item" id="notification-container"></div>
                <br>
            </div>
            <form class="chat-button" action="/ChatButton" method="get">
                <input type="hidden" id="request-type" name="request-type" value="request-type-match">
                <button id="chat-submit" class="button" type="submit" value="Submit">Chat</button>
                <p id="match-status"></p>
            </form>
        </div>
    </div>

    <div id="modal" class="modal">
     <div class="modal-content">
      <span class="close">&times;</span>
      <form id="image-form" method="POST" enctype="multipart/form-data">
        Upload a new profile picture:
        <input type="file" name="image" placeholder="Upload Icon">
        <input type="submit" value="Submit"/> 
      </form>
    </div>

</div>

      <script>

      function setPage() {
        displayMatchesAndNotifications();
        grabBlobURL();
      }

      function displayMatchesAndNotifications() {
        fetch('/Homepage')
          .then((response) => {
            return response.json();
          })
          .then((json) => {
            matches = json["matches"];
            notifications = json["notifications"];
            status = json["status"];
            image = json["image"];

            setImage(image);
            renderMatches(matches);
            renderNotifications(notifications);
            renderMatchStatus(status);
          });
      }

      function renderMatches(matches) {
        const matchContainer = document.getElementById('match-item');
        matches.forEach(match => {
          name = match["name"];
          email = match["email"];
          image = match["match-image"];

          renderMatchPicture(image, matchContainer);
          renderMatchUserInfo(name, email, matchContainer);
          addLineBreak(matchContainer);
        });
      }

      function renderMatchPicture(image, matchContainer) {
        matchIcon = document.createElement('IMG');
        
        if (image === "") {
          matchIcon.setAttribute('src', "avatar.png");
        } else {
          matchIcon.setAttribute('src', "/serve?key=" + image);
        }

        matchIcon.setAttribute('id', 'match-picture');
        matchIcon.setAttribute('alt', "match picture");
        matchContainer.appendChild(matchIcon);
      }

      function renderMatchUserInfo(name, email, matchContainer) {
        const nameElement = document.createElement('div');
        nameElement.innerText = name;
        nameElement.className = 'match-name';

        const newLine = document.createElement('p');

        const emailElement = document.createElement('div');
        emailElement.innerText = email;
        emailElement.className = 'match-email';

        const userInfoDiv = document.createElement('div');
        userInfoDiv.appendChild(nameElement);
        userInfoDiv.appendChild(newLine);
        userInfoDiv.appendChild(emailElement);
        userInfoDiv.className = 'user-info';
        matchContainer.appendChild(userInfoDiv);
      }

      function addLineBreak(matchContainer) {
        const lineBreak = document.createElement('br');
        matchContainer.appendChild(lineBreak);
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

      function renderMatchStatus(status) {
        if (status === "pending") {
          const statusContainer = document.getElementById('match-status');  
          statusContainer.innerText = "Match pending... please check back later."
        }
      }

      function setImage(image) {
        const imageContainer = document.getElementById('profile-pic');
        userIcon = document.createElement('IMG');
        
        if (image === "") {
          userIcon.setAttribute('src', "avatar.png");
        } else {
          userIcon.setAttribute('src', "/serve?key=" + image);
        }

        userIcon.setAttribute('alt', "Profile picture.");
        imageContainer.appendChild(userIcon);
      }

      async function grabBlobURL(){
        const blobURL = await fetch("/image-upload").then((response) => {return response.text();});
        const myForm = document.getElementById("image-form");
        myForm.action = blobURL;
      }

     var modal = document.getElementById("modal");

    // Get the button that opens the modal
    var btn = document.getElementById("update-pic");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks on the button, open the modal
    btn.onclick = function() {
    modal.style.display = "block";
    }

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
    modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }


    </script> 
  </body>
</html>
            



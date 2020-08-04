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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<!DOCTYPE html>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<% UserService userService = UserServiceFactory.getUserService();
   String urlToRedirectToAfterUserLogsOut = "/index.jsp";
   String logoutURL = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut); %> 
<html>
    <head> 
        <meta charset="UTF-8">
        <title> Friend Matching Plus </title>
        <link rel="stylesheet" href="style_chat.css"> 
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    </head>
    <body onload="onload()">
        <nav>
            <a id="log-out-button" href="<%= logoutURL %>"> Log Out </a>
        </nav>
        <img src="logo.png" alt="logo" id="logo">
        <div id="chat-container">
            <div id="find-a-match-container">
                <form class="match-button" action="/matching" method="post">
                    <input type="hidden" id="request-type" name="request-type" value="request-type-match">
                    <button id="match-submit" type="submit" value="Submit">Find a match!</button>
                    <p id="match-status"></p>
                </form>
            </div>
            <div id="conversation-list" class="tab"> 
                <c:forEach items="${matches}" var="match">
                    <div id="conversation-box">
                        <div class="message-text"></div>
                        <div class="message-time"></div>
                    <form action="Chat" method="POST">
                        <button class="conversation" name="user" type="submit" value="${match.getId()}" onclick="openChatBox()">
                            <img src="avatar.png" alt="Person 2" />
                            <div class="title-text">
                                <c:out value="${match.getName()}"/>
                            </div>
                            <div class="created-date">
                                
                            </div>
                            <!-- <div class="conversation-message">  

                            </div> -->
                        </button>
                    </form>
                    </div>
                </c:forEach>
            </div>
            <!-- Person 1 chatbox --> 
            <div id="${recipientID}" class="tabcontent active">
                <div id="chat-title" class="chat-title">
                    <span> ${recipient} </span>
                    <img src="avatar.png" alt="Your match's avatar"/>
                    <button id="calendar"> <img src="google_calendar_logo.png" alt="Google Calendar"/> </button>
                </div>
                <form action="Chat" class="chat-input" method="POST">
                    <div id="chat-message-list">
                        <c:forEach items="${messages}" var="m">
                            <c:choose>
                                <c:when test="${currUser == m.getSenderID() && recipientID == m.getRecipientID()}">
                                    <div class="message-row your-message">
                                        <div class="message-content">
                                            <tr>
                                                <td><div class="message-text"><c:out value="${m.getText()}"/></div></td>
                                                <td><div class="message-time"><c:out value="${m.gettimeStamp()}"/></div></td>
                                            </tr>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="message-row other-message">
                                        <div class="message-content">
                                            <tr>
                                                <td><div class="message-text"><c:out value="${m.getText()}"/></div></td>
                                                <td><div class="message-time"><c:out value="${m.gettimeStamp()}"/></div></td>
                                            </tr>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach> 
                    </div>
                    <div id="chat-form"> 
                        <i class="fa fa-paperclip"></i>
                        <input type="hidden" name="user" value="${recipientID}"></input>
                        <input type="text" placeholder="type a message" name="text" required/>
                    </div>
                </form>
                <div id="modal" class="modal"> 
                    <div class="modal-content">
                        <div class="modal-header">
                            <div class="close">&times;</div>
                            <img src="google_calendar_logo.png" alt="Google Calendar"/>
                            <span class="modal-header-text"> Book a meeting with ${recipient} </span>
                        </div>
                        <div class="modal-body">
                            <form action="/oauth2" method="GET">
                                <button type="submit">Authorize access to Google Calendar</button>
                            </form>

                            <form action="/cal" method="POST">
                                <label>Month:</label><br>
                                <input type="number" id="month" name="month" min="1" max="12" required><br>
                                <label>Day:</label><br>
                                <input type="number" id="day" name="day" min="1" max="31" required><br>
                                <label>Year:</label><br>
                                <input type="number" id="year" name="year" min="2020" max="2021" required><br>
                                <label>Hour:</label><br>
                                <input type="number" id="hour" name="hour" min="0" max="23" required><br>
                                <label>Minute:</label><br>
                                <input type="number" id="minute" name="minute" min="0" max="59" required><br>
                                <input type="hidden" id="minute" name="guestName" value="${recipient}"><br>
                                <button type="submit">Create a Google Calendar event</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script>
            function openChatBox() {
                var i, tabcontent, tablinks, conversation, title;
                tabcontent = document.getElementsByClassName("tabcontent");
                document.getElementsByClassName("tabcontent").style.display = "block";
            }

            //Get the modal
            var modal = document.getElementById("modal");
            var btn = document.getElementById("calendar");
            var div = document.getElementsByClassName("close")[0];

            btn.onclick = function() {
                modal.style.display = "block";
            }

            div.onclick = function() {
                modal.style.display = "none";
            }
            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
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

            async function grabBlobURL() {
                const blobURL = await fetch("/image-upload").then((response) => {return response.text();});
                const myForm = document.getElementById("image-form");
                myForm.action = blobURL;
            }

            function onload() {
              checkServerAlerts();
            }

            function checkServerAlerts() {
              var queryParam = window.location.search;
              var alertIndex = queryParam.indexOf("alert");
              if(alertIndex != -1) { // if there is an alert
                console.log('alertIndex: ' + alertIndex);
                var alertMessageIndex = queryParam.indexOf("=") + 1;
                var alertMessage = queryParam.substr(alertMessageIndex);
                alertMessage = alertMessage.replace(/%20/g, " "); // Replace the %20s with spaces
                alert(alertMessage);
              }
            }
        </script>
    </body>
</html>

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
<!DOCTYPE html>
<html>
    <head> 
        <meta charset="UTF-8">
        <title> Friend Matching Plus </title>
        <link rel="stylesheet" href="style_chat1.css"> 
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    </head>
    <body>
        <div id="chat-container">
            <div id="search-container">
                <input type="text" placeholder="Search" />
            </div>
            <div id="conversation-list" class="tab"> 
                <c:forEach items="${matches}" var="match">
                    <tr>
                        <td><div class="message-text"></div></td>
                        <td><div class="message-time"></div></td>
                    </tr>
                    <div class="conversation">
                        <img src="avatar.png" alt="Person 2" />
                        <div class="title-text">
                            <form action="Chat" method="POST"> <button class="tablinks" name="user" type="submit" value="${match.getId()}" onclick="openChatBox()"><c:out value="${match.getName()}"/></button> </form>
                        </div>
                        <div class="created-date">
                            April 16
                        </div>
                        <div class="conversation-message"> 
                            This is a message 
                        </div>
                    </div>
                </c:forEach>
            </div>
            <!-- Person 1 chatbox --> 
            <div id="${recipient}" class="tabcontent active">
                <div id="chat-title" class="chat-title">
                    <span> ${recipient} </span>
                    <img src="avatar.png" alt="Your match's avatar" />
                </div>
                <form action="Chat" class="chat-input" method="POST">
                    <div id="chat-message-list">
                        <c:forEach items="${messages}" var="m">
                            <c:choose>
                                <c:when test="${currUser == m.getSenderID() && recipient == m.getRecipientID()}">
                                    <div class="message-row your-message">
                                        <div class="message-content">
                                        <tr>
                                            <td><div class="message-text"><c:out value="${m.getText()}"/></div></td>
                                            <td><div class="message-time"><c:out value="${m.getCurrTime()}"/></div></td>
                                        </tr>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="message-row other-message">
                                        <div class="message-content">
                                            <tr>
                                                <td><div class="message-text"><c:out value="${m.getText()}"/></div></td>
                                                <td><div class="message-time"><c:out value="${m.getCurrTime()}"/></div></td>
                                            </tr>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach> 
                    </div>
                    <div id="chat-form"> 
                        <i class="fa fa-paperclip"></i>
                        <input type="hidden" name="user" value="${recipient}"></input>
                        <input type="text" placeholder="type a message" name="text" required/>
                    </div>
                </form>
            </div>
        </div>
        
        <script>
            function openChatBox() {
                var i, tabcontent, tablinks, conversation, title;
                tabcontent = document.getElementsByClassName("tabcontent");
                document.getElementsByClassName("tabcontent").style.display = "block";
            }
            
            function openChatBox(event, userID) {
                var i, tabcontent, tablinks, conversation, title;
                tabcontent = document.getElementsByClassName("tabcontent");
                tabcontent[0].id = userID;
                tablinks = document.getElementsByClassName("tablinks");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].className = tablinks[i].className.replace(" active", "");
                }

                conversation = document.getElementsByClassName("conversation");
                for (i = 0; i < conversation.length; i++) {
                    conversation[i].className = conversation[i].className.replace(" active", "");
                }
                title = document.getElementById("chat-title").getElementsByTagName("span");
                title.innerHTML = userID;
                document.getElementById(userID).style.display = "block";
                event.currentTarget.className += " active";
            }
        </script>
    </body>
</html>
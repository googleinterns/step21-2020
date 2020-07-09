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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="style_chat.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@700&display=swap" rel="stylesheet">
    <title>Friend Matching Plus</title>
  </head>
  <body>
      <!-- In the future, it would be other's user profile pictures/ names -->
      <h1> Hello </h1>
      <!-- !Set the condition so the Form won't close after the user types -->
      <button class="open-button" onclick="openForm()">Person 1</button>

      <div class="chat-popup" id="myForm"> 
          <form action="Chat" class="form-container" method="POST">
            <h1> Chat </h1>
            <label for="text"><b>Message</b></label>
            <br>
            <c:forEach items="${messages}" var="message">
                <tr>
                    <td><c:out value="${message.email}" /></td>
                    <td><c:out value="${message.text}" /></td>
                    <td><c:out value="${message.timestamp}" /></td>
                </tr>
                <br>
            </c:forEach>
            <textarea placeholder="Type message.." name="text" required></textarea>


            <button type="submit" class="btn">Send</button>
            <button type="button" class="btn cancel" onclick="closeFrom()">Close</button>
          </form> 
      </div>

      <script>
            function openForm() {
                document.getElementById("myForm").style.display = "block";
            }

            function closeForm() {
                document.getElementById("myForm").style.display = "none";
            }
      </script>
  </body>
</html>


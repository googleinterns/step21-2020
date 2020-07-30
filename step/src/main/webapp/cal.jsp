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
                <form action="/oauth2" method="GET">
                  <button type="submit">Authorize access to Google Calendar</button>
                </form>

                <form action="/cal" method="POST">
                  <label>Month:</label><br>
                  <input type="text" id="month" name="month"><br>
                  <label>Day:</label><br>
                  <input type="text" id="day" name="day"><br>
                  <label>Year:</label><br>
                  <input type="text" id="year" name="year"><br>
                  <label>Hour:</label><br>
                  <input type="text" id="hour" name="hour"><br>
                  <label>Minute:</label><br>
                  <input type="text" id="minute" name="minute"><br>
                  <label>Match's name:</label><br>
                  <input type="text" id="minute" name="guestName"><br>
                  <button type="submit">Create a Google Calendar event</button>
                  </form>
  </body>
</html>
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
   String urlToRedirectToAfterUserLogsOut = "/index.jsp";
   String logoutURL = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut); %> 

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="style_info.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@900&display=swap" rel="stylesheet">
    <script src="script.js"></script>
    <title>My Information Form </title>
  </head>
  <body>
    <nav>
        <img src="logo.png" alt="logo" id="logo">
        <a id="log-out-button" href="<%= logoutURL %>"> Log Out </a>
        <% if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/index.jsp");   
        } %>
    </nav>
    
    <section class="infoform">
    <form id="regForm" action="Info" method="POST"> 
        <div class="tab"> Name 
            <p><input placeholder="First Name" oninput="this.className = ''"  name="firstName" required></p>
            <p><input placeholder="Last Name" oninput="this.className = ''"  name="lastName" required></p>
        </div>
        <div class="tab"> Birthday
            <p>Day<input placeholder="dd" oninput="this.className = ''" name="dayBirth" type="number" min="1" max="31" required></p>
            <p>Month<input placeholder="mm" oninput="this.className = ''" name="monthBirth" type="number" min="1" max="12" required></p>
            <p>Year<input placeholder="yyyy" oninput="this.className = ''" name="yearBirth" type="number" min="1920" max="2007" required></p>
        </div>
        <div style="overflow:auto;">
            <div style="float:right;">
                <button type="button" id="prevBtn" onclick="nextPrev(-1)"> Previous </button>
                <button type="button" id="nextBtn" onclick="nextPrev(1)"> Next </button>
            </div>
        </div>
         <!-- Circles which indicates the steps of the form: -->
        <div style="text-align:center;margin-top:40px;">
            <span class="step"></span>
            <span class="step"></span>
        </div>
    </form>
    </section>
    <script>
        var currentTab = 0; // Current bar is the bar contains the current question
        showTab(currentTab); //Display the current question

        function showTab(n) {
            var tabs = document.getElementsByClassName("tab");
            tabs[n].style.display = "block";
            /** Fix the button 
                If it is the first question, then only button "Next" displayed
                If it is the last question, then display "Previous" and "Submit" buttons
                Otherwise, display "Previous" and "Next" buttons
            */
            if (n == 0) {
                document.getElementById("prevBtn").style.display = "none";
            } else {
                document.getElementById("prevBtn").style.display = "inline";
            }
            if (n == (tabs.length - 1)) {
                document.getElementById("nextBtn").innerHTML = "Submit";
            } else {
                document.getElementById("nextBtn").innerHTML = "Next";
            }
            fixStepIndicator(n);
        }

        // This function will figure out which tab to display
        function nextPrev(n) {
            var tabs = document.getElementsByClassName("tab");
            if (n == 1 && !validateForm()) return false;
            tabs[currentTab].style.display = "none";
            // Update the current tab
            currentTab = currentTab + n;
            if (currentTab >= tabs.length) {
                document.getElementById("regForm").submit();
                return false;
            }
            showTab(currentTab);
        }

        // This function deals with validation of the form fields
        function validateForm() {
            var tabs, currTab, i, valid = true;
            tabs = document.getElementsByClassName("tab");
            currTabInput = tabs[currentTab].getElementsByTagName("input");
            // A loop that checks every input field in the current tab
            for (i = 0; i < currTabInput.length; i++) {
                if (currTabInput[i].value == "") {
                currTabInput[i].className += " invalid";
                valid = false;
                }
            }
            if (valid) {
                document.getElementsByClassName("step")[currentTab].className += " finish";
            }
            return valid;
        }

        function fixStepIndicator(n) {
            var i, step = document.getElementsByClassName("step");
            for (i = 0; i < step.length; i++) {
                step[i].className = step[i].className.replace(" active", "");
            }
            step[n].className += " active";
        }
    </script>
  </body>
</html>

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
    <link rel="stylesheet" href="style_pref.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@700&display=swap" rel="stylesheet">
    <script src="script.js"></script>
    <title>My Preference Form </title>
  </head>
  <body>
    <nav>
        <a href="<%= logoutURL %>"> Log Out </a>
        <h1 style="float: left; font-size: 25px"> Friend Matching Plus </h1>
    </nav>
    
    
    <% if (!userService.isUserLoggedIn()) {
        response.sendRedirect("index.jsp");   
    } %>
    <section class="prefform">
    <form id="regForm" action="Pref" method="POST"> 
        <div class="tab"> Are you staying in the US now? 
            <p>
                <input type="radio" oninput="this.className = ''" name="q1" value="Yes" id="q1yes">
                <label for="q1yes"> Yes </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q1" value="No" id="q1no">
                <label for="q1no"> No </label>
            </p>
        </div>
        <br>  
        <div class="tab"> Do you have any pets? 
            <p>
                <input type="radio" oninput="this.className = ''" name="q2" value="Yes" id="q2yes">
                <label for="q2yes"> Yes </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q2" value="No" id="q2no">
                <label for="q2no"> No </label>
            </p>
        </div>
        <br>  
        <div class="tab"> Do you have any siblings? 
            <p>
                <input type="radio" oninput="this.className = ''" name="q3" value="Yes" id="q3yes">
                <label for="q3yes"> Yes </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q3" value="No" id="q3no">
                <label for="q3no"> No </label>
            </p>
        </div>
        <br>  
        <div class="tab"> <a href="https://en.wikipedia.org/wiki/The_dress#:~:text=The%20dress%20itself%20was%20confirmed,not%20available%20at%20the%20time." target="_blank"> Is the dress blue or gold? </a>
            <p>
                <input type="radio" oninput="this.className = ''" name="q4" value="Blue" id="q4blue">
                <label for="q4blue"> Blue </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q4" value="Gold" id="q4gold">
                <label for="q4gold"> Gold </label>
            </p>
        </div>
        <div class="tab"> <a href="https://www.youtube.com/watch?v=7X_WvGAhMlQ" target="_blank"> Is it Yanny or Laurel? </a>
            <p>
                <input type="radio" oninput="this.className = ''" name="q5" value="Yanny" id="q5yanny">
                <label for="q5yanny"> Yanny </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q4" value="Laurel" id="q5laurel">
                <label for="q5laurel"> Laurel </label>
            </p>
        </div>
        <br>  
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
            <span class="step"></span>
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
            var tabs, currTabInput, i, valid = true;
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
                step[i].className = x[i].className.replace(" active", "");
            }
            step[n].className += " active";
        }
    </script>
  </body>
</html>
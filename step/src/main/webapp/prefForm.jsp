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
    <link rel="stylesheet" href="style_pref.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@900&display=swap" rel="stylesheet">
    <script src="script.js"></script>
    <title>My Preference Form </title>
  </head>
  <body>
    <nav>
        <img src="logo.png" alt="logo" id="logo">
        <a id="log-out-button" href="<%= logoutURL %>"> Log Out </a>
    </nav>
    
    
    <% if (!userService.isUserLoggedIn()) {
        response.sendRedirect("/index.jsp");   
    } %>
    <section class="prefform">
    <form id="regForm" action="Pref" method="POST"> 
        <div class="tab"> <div class="question"> Do you have any pets? </div>
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
        <div class="tab"> <div class="question"> <a href="https://en.wikipedia.org/wiki/The_dress#:~:text=The%20dress%20itself%20was%20confirmed,not%20available%20at%20the%20time." target="_blank"> Is the dress blue or gold? </a> </div>
            <p>
                <input type="radio" oninput="this.className = ''" name="q2" value="Blue" id="q2blue">
                <label for="q2blue"> Blue </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q2" value="Gold" id="q2gold">
                <label for="q2gold"> Gold </label>
            </p>
        </div>
        <br>  
        <div class="tab"> <div class="question"> Would you rather win the lottery or work at the perfect job? </div>
            <p>
                <input type="radio" oninput="this.className = ''" name="q3" value="Win the lottery" id="q3loterry">
                <label for="q3loterry"> Win the lottery </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q3" value="Work the perfect job" id="q3work">
                <label for="q3work"> Work the perfect job </label>
            </p>
        </div>
        <br>  
        <div class="tab"> <div class="question"> Would you rather trade intelligence for looks or looks for intelligence? </div>
            <p>
                <input type="radio" oninput="this.className = ''" name="q4" value="Trade intelligence for looks" id="q4intelligence">
                <label for="q4intelligence"> Trade intelligence for looks </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q4" value="Trade looks for intelligence" id="q4looks">
                <label for="q4looks"> Trade looks for intelligence </label>
            </p>
        </div>
        <div class="tab"> <div class="question"> Is your glass half full or half empty? </div>
            <p>
                <input type="radio" oninput="this.className = ''" name="q5" value="Half full" id="q5full">
                <label for="q5full"> Half full </label>
            </p>
            <p>
                <input type="radio" oninput="this.className = ''" name="q5" value="Half empty" id="q5empty">
                <label for="q5empty"> Half empty </label>
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

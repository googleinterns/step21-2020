// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// Client ID and API key from the Developer Console
var CLIENT_ID = '427851753576-r0n9jbplnf7t2peic2ltnibeqquemo2j.apps.googleusercontent.com'; // TODO is a client id needed?
var API_KEY = 'AIzaSyBjLLBbdndJmQ9XvljCT7XCE7_zrAeBNGQ';

// Array of API discovery doc URLs for APIs used by the quickstart
var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/hangouts/meet"];

// Authorization scopes required by the API; multiple scopes can be
// included, separated by spaces.
var SCOPES = 'https://www.googleapis.com/auth/meetings';

var authorizeButton = document.getElementById('authorize_button');
var signoutButton = document.getElementById('signout_button');

function joinCall() {
  console.log('joinCall()');

  meet.users.me.getPermissions();
}

/**
  *  On load, called to load the auth2 library and API client library.
  */
function handleClientLoad() {
  gapi.load('client:auth2', initClient);
}

/**
  *  Initializes the API client library and sets up sign-in state
  *  listeners.
  */
function initClient() {
  gapi.client.init({
    apiKey: API_KEY,
    clientId: CLIENT_ID,
    discoveryDocs: DISCOVERY_DOCS,
    scope: SCOPES
  }).then(function () {
    // Listen for sign-in state changes.
    gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);

    // Handle the initial sign-in state.
    updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
    authorizeButton.onclick = handleAuthClick;
    signoutButton.onclick = handleSignoutClick;
  }, function(error) {
    appendPre(JSON.stringify(error, null, 2));
  });
}

/**
  *  Called when the signed in status changes, to update the UI
  *  appropriately. After a sign-in, the API is called.
  */
function updateSigninStatus(isSignedIn) {
  if (isSignedIn) {
    authorizeButton.style.display = 'none';
    signoutButton.style.display = 'block';
    createMeeting();
  } else {
    authorizeButton.style.display = 'block';
    signoutButton.style.display = 'none';
  }
}

/**
  *  Sign in the user upon button click.
  */
function handleAuthClick(event) {
  gapi.auth2.getAuthInstance().signIn();
}

/**
  *  Sign out the user upon button click.
  */
function handleSignoutClick(event) {
  gapi.auth2.getAuthInstance().signOut();
}

/**
  * Append a pre element to the body containing the given message
  * as its text node. Used to display the results of the API call.
  *
  * @param {string} message Text to be placed in pre element.
  */
function appendPre(message) {
  var pre = document.getElementById('content');
  var textContent = document.createTextNode(message + '\n');
  pre.appendChild(textContent);
  console.log('response: ' + message);
}

/**
  * Create a meeting and show meeting code.
  */
function createMeeting() {
  gapi.client.meet.spaces.create({
    "creatorRegionCode": "US"
  }).then(function(response) {
    var meetingCode = response.result.name;
    appendPre("Meeting code: "+meetingCode);
  });
}

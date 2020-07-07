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
var CLIENT_ID = '427851753576-r0n9jbplnf7t2peic2ltnibeqquemo2j.apps.googleusercontent.com';
var API_KEY = 'AIzaSyA0OH6zV98PRX0YALa86K6InnPRAKvuI0s';

// Array of API discovery doc URLs for APIs used by the quickstart
var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];

// Authorization scopes required by the API; multiple scopes can be
// included, separated by spaces.
var SCOPES = 
  // "https://www.googleapis.com/auth/calendar.readonly";
  "https://www.googleapis.com/auth/calendar";
  // More scopes at https://developers.google.com/calendar/auth

var authorizeButton;
var signoutButton;

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
  authorizeButton = document.getElementById('authorize_button');
  signoutButton = document.getElementById('signout_button');

  gapi.client.init({
    apiKey: API_KEY,
    clientId: CLIENT_ID,
    discoveryDocs: DISCOVERY_DOCS,
    scope: SCOPES
  }).then(function () {
    // Listen for sign-in state changes.
    gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);

    // Handle the initial sign-in state and update the front end.
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
    listUpcomingEvents();
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
}

/**
  * Print the summary and start datetime/date of the next ten events in
  * the authorized user's calendar. If no events are found an
  * appropriate message is printed.
  */
function listUpcomingEvents() {
  gapi.client.calendar.events.list({
    'calendarId': 'primary',
    'timeMin': (new Date()).toISOString(),
    'showDeleted': false,
    'singleEvents': true,
    'maxResults': 10,
    'orderBy': 'startTime'
  }).then(function(response) {
    var events = response.result.items;
    appendPre('Upcoming events:');

    if (events.length > 0) {
      for (i = 0; i < events.length; i++) {
        var event = events[i];
        var when = event.start.dateTime;
        if (!when) {
          when = event.start.date;
        }
        appendPre(event.summary + ' (' + when + ')')
      }
    } else {
      appendPre('No upcoming events found.');
    }
  });
}

function addEvent(title) {
  console.log('addEvent(' + title + ')');
  // console.log(gapi.client.calendar);
  // console.log(gapi.client.calendar.calendars);
  // console.log(gapi.client.calendar.calendars.timeZone);
  // console.log(gapi.client.calendar.calendars.timezone);
  // console.log(gapi.client.calendar.settings.timezone);
  // console.log(gapi.client.calendar.settings.get.timezone);
  // console.log(gapi.client.calendar.calendars.get('primary'));
  // console.log(gapi.client.calendar.settings.get('timezone').timezone);
  // example path: /calendar/v3/calendars/primary/events found from https://github.com/google/google-api-javascript-client/blob/ed1b241471571d1c4b739098dd79a3aee93cb5d3/samples/requestWithBody.html
  // console.log(gapi.client.calendar.settings.get('/calendar/v3/settings').timezone);
  // console.log(gapi.client.calendar.settings.get('timezone').value);

  var event = {
    'summary': title,
    'description': 'Testing the Google Calendar API',
    'start': {
      'dateTime': '2020-07-06T09:00:00-07:00',
      'timeZone': 'America/Los_Angeles'
      // 'timeZone': gapi.client.calendar.calendar.timeZone
    },
    'end': {
      'dateTime': '2020-07-06T17:00:00-07:00',
      'timeZone': 'America/Los_Angeles'
      // 'timeZone': gapi.client.calendar.calendars.timeZone
    },
    'attendees': [
      {'email': 'lpage@example.com'},
      {'email': 'sbrin@example.com'}
    ],
  };

  var request = gapi.client.calendar.events.insert({
    'calendarId': 'primary',
    'resource': event
  });

  request.execute(function(event) {
    appendPre('Event created: ' + event.htmlLink);
  });
}
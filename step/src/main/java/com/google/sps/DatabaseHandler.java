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

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.lang.IllegalArgumentException;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

public final class DatabaseHandler {
  
  // Accessing Datastore
  private static DatastoreService datastore =
         DatastoreServiceFactory.getDatastoreService();
  public static final int MATCHING = 1;
  public static final int MESSAGE = 2;

  private static final String UNKNOWN = "Unkown"; 
  private static final String FIRST_NAME =  "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String MATCH_PENDING = "matchPending"; 
  private static final String QUESTION_1 = "q1";
  private static final String QUESTION_2 = "q2";
  private static final String QUESTION_3 = "q3";
  private static final String QUESTION_4 = "q4";
  private static final String QUESTION_5 = "q5";
  private static final String EMAIL = "email";
  private static final String USER_ID = "userId";
  private static final String OTHER_USER_ID = "otherUserId";
  private static final String TIMESTAMP = "timestamp";
  private static final String TYPE = "type";
  private static final String NOTIFICATION = "Notification";
  private static final String IMAGE_URL = "imageUrl";

  private DatabaseHandler() {
      
  }

  // Method for adding a user to the database
  public static void addUser(String firstName, String lastName, String dayBirth,
    String monthBirth, String yearBirth, String email, String id) {
    Entity entity = new Entity("User", id);

    if (firstName == null) {
      firstName = UNKNOWN;
    } 
    
    if (lastName == null) {
      lastName = UNKNOWN;
    }

    entity.setProperty(FIRST_NAME, firstName);
    entity.setProperty(LAST_NAME, lastName);
    entity.setProperty("dayBirth", dayBirth);
    entity.setProperty("monthBirth", monthBirth);
    entity.setProperty("yearBirth", yearBirth);
    entity.setProperty(EMAIL, email);
    entity.setProperty(IMAGE_URL, "");
    entity.setProperty("id", id);
    entity.setProperty(MATCH_PENDING, false);
    datastore.put(entity);    
  }

  //Adding users' preferences info 
  public static void addUserPref(String id, String q1, String q2, String q3, String q4, String q5) {
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    entity.setProperty(QUESTION_1, q1);
    entity.setProperty(QUESTION_2, q2);
    entity.setProperty(QUESTION_3, q3);
    entity.setProperty(QUESTION_4, q4);
    entity.setProperty(QUESTION_5, q5);
    datastore.put(entity);
  }
 
  // Adding a user's notificaton to the database
  public static void addNotification(String firstId, String secondId,
      long timestamp, int type) {
    
    if (firstId == null || secondId == null) {
      throw new NullPointerException();
    }

    Entity entity = new Entity(NOTIFICATION);
    entity.setProperty(USER_ID, firstId);
    entity.setProperty(OTHER_USER_ID, secondId);
    entity.setProperty(TIMESTAMP, timestamp);
    entity.setProperty(TYPE, type);
    datastore.put(entity); 
  }

  // Getting a user's notifications using their id
  public static Collection<Notification> getUserNotifications(String id) {
    List<Notification> notifications = new ArrayList<>();

    Filter idFilter = new FilterPredicate(USER_ID, FilterOperator.EQUAL, id);

    // Querying all of the user's notifications from the Datastore.
    Query query = new Query(NOTIFICATION).setFilter(idFilter);

    PreparedQuery results = datastore.prepare(query);

    // Populating the list
    for (Entity entity : results.asIterable()) {
      String firstId = (String) entity.getProperty(USER_ID);
      String secondId = (String) entity.getProperty(OTHER_USER_ID);
      long timestamp = (long) entity.getProperty(TIMESTAMP);
      long notificationType = (long) entity.getProperty(TYPE);
       
      Notification notification;

      if (notificationType == MATCHING) {
        notification = new MatchNotification(firstId, secondId, timestamp);
      } else if (notificationType == MESSAGE) {
        notification = new MessageNotification(firstId, secondId, timestamp);
      } else {
        throw new IllegalArgumentException("Invalid notification type.");
      }

      notifications.add(notification);
    }

    Collections.sort(notifications);
    Collections.reverse(notifications);

    return notifications;    
  }

  // Method for getting a user's email using their id
  public static String getUserEmail(String id) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      return (String) user.getProperty(EMAIL);
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
      return null; 
    }
  }

  // Method for getting a user's name using their id
  public static String getUserName(String id) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      String firstName = (String) user.getProperty(FIRST_NAME);
      String lastName = (String) user.getProperty(LAST_NAME);
      return firstName + " " + lastName;
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  //Method for getting a user's preferences using their id
  public static ArrayList<String> getUserPreferences(String id) {
    try {
        Entity user = datastore.get((new User(id)).getKey());
        String q1 = (String) user.getProperty(QUESTION_1);
        String q2 = (String) user.getProperty(QUESTION_2);
        String q3 = (String) user.getProperty(QUESTION_3);
        String q4 = (String) user.getProperty(QUESTION_4);
        String q5 = (String) user.getProperty(QUESTION_5);

        ArrayList<String> preferences = new ArrayList<>();
        preferences.add(q1);
        preferences.add(q2);
        preferences.add(q3);
        preferences.add(q4);
        preferences.add(q5);
        return preferences;
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  // Method for getting all of a user's matches
  public static Collection<User> getUserMatches(String id) {
    Filter idFilter = new FilterPredicate(USER_ID, FilterOperator.EQUAL, id);
    Filter typeFilter = new FilterPredicate(TYPE, FilterOperator.EQUAL, MATCHING);

    CompositeFilter compositeFilter = new CompositeFilter(CompositeFilterOperator.AND, 
      Arrays.asList(idFilter, typeFilter));

    Query query = new Query(NOTIFICATION).setFilter(compositeFilter);
    PreparedQuery results = datastore.prepare(query);

    Collection<User> matches = new ArrayList<>();
    for (Entity entity: results.asIterable()) {
      String matchId = (String) entity.getProperty(OTHER_USER_ID);
      matches.add(new User(matchId));
    }
    return matches;
  }

    // Method for updating a user's match-pending status
  public static void updateMatchPendingStatus(String id, boolean status) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      user.setProperty(MATCH_PENDING, status);
      datastore.put(user);
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
    }
  }

  // Method for getting a user's match-pending status
  public static Boolean getMatchPendingStatus(String id) {
   try {
      Entity user = datastore.get((new User(id)).getKey());
      Boolean matchPending = (Boolean) user.getProperty(MATCH_PENDING);
      return matchPending;
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  // Method for adding an image link to a user's entity in datastore
  public static void uploadUserImage(String id, String imageUrl) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      user.setProperty(IMAGE_URL, imageUrl);
      datastore.put(user);
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
    }
  }

  // Method for getting the image link for a user
  public static String getUserImageUrl(String id) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      String imageUrl = (String) user.getProperty(IMAGE_URL);

      // case for users who signed up before image uploading features were added
      if (imageUrl == null) {
        imageUrl = "";
      }
      return imageUrl;

    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      e.printStackTrace();
      return null;
    } 
  }

}

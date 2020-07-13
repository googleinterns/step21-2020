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
import java.util.ArrayList;
import java.util.Arrays;
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

  private DatabaseHandler() {

  }

  // Method for adding a user to the database
  public static void addUser(String firstName, String lastName, int dayBirth,
    int monthBirth, int yearBirth, String email, String id) {
    Entity entity = new Entity("User", id);
    entity.setProperty("firstName", firstName);
    entity.setProperty("lastName", lastName);
    entity.setProperty("dayBirth", dayBirth);
    entity.setProperty("monthBirth", monthBirth);
    entity.setProperty("yearBirth", yearBirth);
    entity.setProperty("email", email);
    entity.setProperty("id", id);
    datastore.put(entity);    
  }
 
  // Adding a user's notificaton to the database
  public static void addNotification(String firstId, String secondId,
      long timestamp, int type) {
    Entity entity = new Entity("Notification");
    entity.setProperty("userId", firstId);
    entity.setProperty("otherUserId", secondId);
    entity.setProperty("timestamp", timestamp);
    entity.setProperty("type", type);
    datastore.put(entity); 
  }

  // Getting a user's notifications using their id
  public static Collection<Notification> getUserNotifications(String id) {
    Collection<Notification> notifications = new ArrayList<>();

    Filter idFilter = new FilterPredicate("userId", FilterOperator.EQUAL, id);

    // Querying all of the user's notifications from the Datastore.
    Query query = new Query("Notification").setFilter(idFilter)
      .addSort("timestamp",SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    // Populating the list
    for (Entity entity : results.asIterable()) {
      String firstId = (String) entity.getProperty("userId");
      String secondId = (String) entity.getProperty("otherUserId");
      long timestamp = (long) entity.getProperty("timestamp");
      long notificationType = (long) entity.getProperty("type");
       
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

    return notifications;    
  }

  // Method for getting a user's email using their id
  public static String getUserEmail(String id) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      return (String) user.getProperty("email");
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      return null; 
    }
  }

  // Method for getting a user's name using their id
  public static String getUserName(String id) {
    try {
      Entity user = datastore.get((new User(id)).getKey());
      String firstName = (String) user.getProperty("firstName");
      String lastName = (String) user.getProperty("lastName");
      return firstName + " " + lastName;
    } catch (EntityNotFoundException e) {
      System.err.println("Element not found: " + e.getMessage());
      return null;
    }
  }

  // Method for getting all of a user's matches
  public static Collection<User> getUserMatches(String id) {
    Filter idFilter = new FilterPredicate("userId", FilterOperator.EQUAL, id);
    Filter typeFilter = new FilterPredicate("type", FilterOperator.EQUAL, MATCHING);

    CompositeFilter compositeFilter = new CompositeFilter(CompositeFilterOperator.AND, 
      Arrays.asList(idFilter, typeFilter));

    Query query = new Query("Notification").setFilter(compositeFilter);
    PreparedQuery results = datastore.prepare(query);

    Collection<User> matches = new ArrayList<>();
    for (Entity entity: results.asIterable()) {
      String matchId = (String) entity.getProperty("otherUserId");
      matches.add(new User(matchId));
    }
    return matches;
  }

}

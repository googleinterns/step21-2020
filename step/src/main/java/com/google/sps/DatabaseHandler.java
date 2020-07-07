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
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public final class DatabaseHandler {
  
  // Accessing Datastore
  private static DatastoreService datastore =
         DatastoreServiceFactory.getDatastoreService();

  // TODO: replace with Datastore
  private static Map<Long, User> userMap = new HashMap<>();
  private static Map<Long, Collection<Notification>> notificationMap = new HashMap<>();

  private DatabaseHandler() {

  }

  // TODO: call this method when a user first signs up
  // Method for adding a user to the database
  public static void addUserToDatabase(User user, HttpServletRequest request) {
    userMap.put(user.getId(), user);

    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    int dayBirth = Integer.parseInt(request.getParameter("dayBirth"));
    int monthBirth = Integer.parseInt(request.getParameter("monthBirth"));
    int yearBirth = Integer.parseInt(request.getParameter("yearBirth"));

    // TODO: ask Ngan to pass in userService.getCurrentUser()
    String email = userService.getCurrentUser().getEmail();
    String id = userService.getCurrentUser().getUserId();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    entity.setProperty("firstName", firstName);
    entity.setProperty("lastName", lastName);
    entity.setProperty("dayBirth", dayBirth);
    entity.setProperty("monthBirth", monthBirth);
    entity.setProperty("yearBirth", yearBirth);
    datastore.put(entity);    
  }

  // Method for getting a user object using an id
  public static User getUserById(long id) throws NoSuchElementException {
    if (userMap.containsKey(id)) {
      return userMap.get(id);    
    } else {
      throw new NoSuchElementException();  
    }

    Query query = new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);        

  }

  // Method for removing a user from our database
  public static void removeUserById(long id) {
    if (userMap.containsKey(id)) {
      userMap.remove(id);    
    }
  }
 
  // Storing a user's notification
  public static void addNotification(Notification notification) {
    long userId = notification.getId();
    
    // adding the user to the hashmap if they're not already in there
    if (!notificationMap.containsKey(userId)) {
      notificationMap.put(userId, new LinkedList<>());      
    }
    
    // retrieving the user's list of notifications, adding the new notification
    // to it, and adding it to the hashmap
    LinkedList<Notification> userNotifications = (LinkedList) notificationMap.get(userId);
    userNotifications.addFirst(notification);
    notificationMap.put(notification.getId(), userNotifications);
  }

  // Getting a user's notifications using their id
  public static Collection<Notification> getNotificationsById(long id) {
    if (notificationMap.containsKey(id)) {
      return notificationMap.get(id);    
    } else {
      throw new NoSuchElementException();  
    }      
  }

  // Method for clearing all saved user data
  public static void clearSavedUsers() {
    userMap = new HashMap<>();  
  }

  // Method for clearing all saved notifications
  public static void clearSavedNotifications() {
    notificationMap = new HashMap<>();
  }

}

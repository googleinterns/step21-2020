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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class DatabaseHandler {
  
  // Accessing Datastore
  private static DatastoreService datastore =
         DatastoreServiceFactory.getDatastoreService();

  private static Map<String, Collection<Notification>> notificationMap = new HashMap<>();

  private DatabaseHandler() {

  }

  // Method for adding a user to the database
  public static void addUser(User user, HttpServletRequest request) {
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    int dayBirth = Integer.parseInt(request.getParameter("dayBirth"));
    int monthBirth = Integer.parseInt(request.getParameter("monthBirth"));
    int yearBirth = Integer.parseInt(request.getParameter("yearBirth"));

    String email = user.getEmail();
    String id = user.getUserId();

    Entity entity = new Entity("User");
    entity.setProperty("firstName", firstName);
    entity.setProperty("lastName", lastName);
    entity.setProperty("dayBirth", dayBirth);
    entity.setProperty("monthBirth", monthBirth);
    entity.setProperty("yearBirth", yearBirth);
    entity.setProperty("email", email);
    entity.setProperty("id", id);
    datastore.put(entity);    
  }
 
  // Storing a user's notification
  public static void addNotification(Notification notification) {
    String userId = notification.getId();
    
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
  public static Collection<Notification> getNotificationsById(String id) {
    if (notificationMap.containsKey(id)) {
      return notificationMap.get(id);    
    } else {
      throw new NoSuchElementException();  
    }      
  }

  // Method for clearing all saved notifications
  public static void clearSavedNotifications() {
    notificationMap = new HashMap<>();
  }

}

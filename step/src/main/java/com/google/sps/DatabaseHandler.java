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

public final class DatabaseHandler {
  
  // TODO: replace with Datastore
  private static Map<Long, User> userMap = new HashMap<>();
  private static Map<Long, Collection<Notification>> notificationMap = new HashMap<>();

  private DatabaseHandler() {

  }

  // TODO: call this method when a user first signs up
  // Method for adding a user to the database
  public static void addUserToDatabase(User user) {
    userMap.put(user.getId(), user);    
  }

  // Method for getting a user object using an id
  public static User getUserById(long id) throws NoSuchElementException {
    if (userMap.containsKey(id)) {
      return userMap.get(id);    
    } else {
      throw new NoSuchElementException();  
    }    
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

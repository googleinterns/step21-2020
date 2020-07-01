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
import java.util.HashSet;

public class User {
  
  private long id;
  private String email;
  private String name;
  private Collection<User> matches;
  private Collection<Notification> notifications;

  // TODO: determine what fields we want.
  // TODO: update the constructor so that it only needs to take
  //       in an id, and the other fields can then be looked up
  //       in the database. this update will be made when data-
  //       -store is integrated
  public User(long id, String email, String name) {
    this.id = id;
    this.email = email;
    this.name = name;
    //TODO: update this to fetch from database instead of making a new hashset    
    this.matches = new HashSet<>();
    this.
  }

  public long getId() {
    return id;    
  }

  // Getter method for a user's email
  public String getEmail() {
    return email;  
  }

  // Getter method for a user's name
  public String getName() {
    return name;  
  }

  // Getter method for a user's matches
  public Collection<User> getMatches() {
    return matches;    
  }

  // Method for adding a match for a user
  public void addMatch(User user) {
    matches.add(user);   
  }

  // Method for removing a match for a user
  public void removeMatch(User user) {
    if (matches.contains(user)) {
      matches.remove(user);  
    }  
  }

  public boolean isMatchedWith(User user) {
    return matches.contains(user);   
  }

  // Overriden equals method
  @Override
  public boolean equals(Object obj) { 
    if(this == obj) {
      return true; 
    } 
 
    if(obj == null || obj.getClass()!= this.getClass()) {
      return false;
    }
     
    User user = (User) obj; 
    return this.email.equals(user.getEmail()); 
  }

  // Overriden hashCode method
  @Override
  public int hashCode() { 
    return email.hashCode(); 
  }

  // Method for clearing all of a user's matches
  public void clearMatches() {
    matches = new HashSet<>();  
  }

  // Method for adding a notification to a user's collection of notifications
  public void addNotification(Notification notification) {
    notifications.add(notification);      
  }

  public Collection<Notification> getNotifications() {
    return notifications;     
  }

}
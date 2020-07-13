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
import java.util.ArrayList;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class User {
  
  private String userServiceUserId;
  private String email;
  private String name;
  private Collection<User> matches;
  private Key key;

  public User(String userServiceUserId) {
    this.userServiceUserId = userServiceUserId;
  }

  // ID getter method
  public String getId() {
    return userServiceUserId;    
  }

  // Key getter method
  public Key getKey() {
    if (key == null) {
      key = KeyFactory.createKey("User", userServiceUserId);
    }
    return key;
  }

  // Getter method for a user's email
  public String getEmail() {
    if (email == null) {
      email = DatabaseHandler.getUserEmail(userServiceUserId);
    }
    return email;  
  }

  // Getter method for a user's name
  public String getName() {
    if (name == null) {
      name = DatabaseHandler.getUserName(userServiceUserId);
    }
    return name;  
  }

  // Getter method for a user's matches
  public Collection<User> getMatches() {
    if (matches == null) {
      matches = DatabaseHandler.getUserMatches(userServiceUserId);
    }
    return matches;   
  }

  // Method for updating a user's matches -- called after the user gets a new match
  public void updateMatches() {
    matches = DatabaseHandler.getUserMatches(userServiceUserId);
  }

  // Method for checking if a user is matched with another user
  public boolean isMatchedWith(User user) {
    // ensuring that the matches field is instantiated
    updateMatches();
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
    return this.userServiceUserId.equals(user.getId()); 
  }

  // Overriden hashCode method
  @Override
  public int hashCode() { 
    return userServiceUserId.hashCode(); 
  }

}

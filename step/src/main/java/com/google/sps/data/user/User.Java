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

package com.google.sps.data.user;

import com.google.sps.data.user.User;
import java.util.Collection;
import java.util.HashSet;

public class User() {
  
  private Collection<User> matches;

  // TODO: determine what fields we want.
  public User() {  
    this.matches = new HashSet<>();
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

  // TODO: equals and hashcode methods
}
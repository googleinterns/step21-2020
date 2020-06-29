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

package com.google.sps.data.matching;

import java.util.Collection;
import java.util.Stack;
import com.google.sps.data.user.User;

public class MatchManager {

  Stack<User> matchQueue;

  public MatchManager() {
    this.matchQueue = new Stack<>();  
  }

  /**
   * Method for adding a user to the list of users to be matched
   * Returns a boolean indicating whether or not the user was successfully matched
   * in that instant
   */ 
  public boolean getMatch(User user) {
    if (!matchQueue.contains(user)) {
      matchQueue.push(user);
    }
    return generateMatch();     
  }

  // Method for matching users on a first-come first-serve basis
  // Returns true when a match is generated or the user is at least
  // added to the matching queue, false otherwise
  private boolean generateMatch() {
    if (matchQueue.size() >= 2) {
      User firstUser = matchQueue.pop();
      User secondUser = matchQueue.pop();
      
      // Reminder: all matches are mutual  
      if (!firstUser.isMatchedWith(secondUser)) {
        firstUser.addMatch(secondUser);
        secondUser.addMatch(firstUser);
      /** case where the two users in the queue are already matched, this is handled
          by returning false to the user who most recently tried to get a match and
          adding the older user back into the queue
      */   
      } else {
        matchQueue.push(firstUser);  
        return false;  
      }   
    }
    return true;   
  }

}
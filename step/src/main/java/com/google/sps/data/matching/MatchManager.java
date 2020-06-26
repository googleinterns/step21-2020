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

  // Method for adding a user to the list of users to be matched
  public void getMatch(User user) {
    if (!matchQueue.contains(user)) {
      matchQueue.push(user);
    }
    manageMatches();     
  }

  // Method for matching users on a first-come first-serve basis
  private void manageMatches() {
    if (matchQueue.size() >= 2) {
      User firstUser = matchQueue.pop();
      User secondUser = matchQueue.pop();
      
      if (!firstUser.equals(secondUser)) {
        if (!firstUser.getMatches().contains(secondUser)) {
          firstUser.addMatch(secondUser);
        }   
        
        if (!secondUser.getMatches.contains(firstUser)) {
          secondUser.addMatch(firstUser);
        }
      }
      
    }    
  }

}
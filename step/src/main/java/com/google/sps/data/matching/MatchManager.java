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

public final class MatchManager {

  // TODO: integrate datastore so that match requests aren't lost if the server restarts
  Stack<User> matchQueue = new Stack<>();

  private MatchManager() {

  }

  public static void generateMatch(User user) {
    if (matchQueue.empty()) {
      matchQueue.push(user);  
    } else {
      User matchResult = matchQueue.peek();

      if (!matchResult.isMatchedWith(user) && !matchResult.equals(user)) {
        // actually removing the match result from the queue
        matchResult = matchQueue.poll(); 
        createMatch(matchResult, user);
      // adding the user to the queue in case there wasn't a successful match for them
      // this is so they can be matched later      
      } else if (!matchQueue.contains(user)) {
        matchQueue.push(user);    
      }  
    }    
  }

  // helper method for actually adding to people into one another's matches
  private void createMatch(User firstUser, User secondUser) {
    firstUser.addMatch(secondUser);
    secondUser.addMatch(firstUser);
  }

}
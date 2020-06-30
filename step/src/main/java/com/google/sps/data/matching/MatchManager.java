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
import java.util.Queue;
import java.util.LinkedList;
import com.google.sps.data.user.User;

public final class MatchManager {

  // TODO: integrate datastore so that match requests aren't lost if the server restarts
  private static Queue<User> matchQueue = new LinkedList<>();

  private MatchManager() {

  }

  // Getter method for the match queue (for testing purposes)
  public static Queue getMatchQueue() {
    return matchQueue;
  }

  public static void generateMatch(User user) {
    if (matchQueue.isEmpty()) { 
      matchQueue.add(user);  
    } else {
      User matchResult = matchQueue.peek();

      if (!matchResult.isMatchedWith(user) && !matchResult.equals(user)) {
        // actually removing the match result from the queue
        matchResult = matchQueue.poll(); 
        createMatch(matchResult, user);
      // adding the user to the queue in case there wasn't a successful match for them
      // this is so they can be matched later      
      } else if (!matchQueue.contains(user)) {
        matchQueue.add(user);  
      }  
    }    
  }

  // helper method for actually adding to people into one another's matches
  private static void createMatch(User firstUser, User secondUser) {
    firstUser.addMatch(secondUser);
    secondUser.addMatch(firstUser);
  }

  // helper method for clearing out the match queue
  // this is primarily for testing purposes at the moment
  public static void clearQueue() {
    matchQueue = new LinkedList<>();
  }

}
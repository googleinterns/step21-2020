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
import java.util.Queue;
import java.util.LinkedList;

public final class MatchManager {

  // Integrate datastore so that match requests aren't lost if the server restarts
  private static Queue<User> matchQueue = new LinkedList<>();

  private MatchManager() {

  }

  // Getter method for the match queue (for testing purposes)
  public static Queue<User> getMatchQueue() {
    return matchQueue;
  }

  // Getter method to return a deep copy of the match queue (for testing purposes).
  // The returned queue can be modified without the changes effecting the actual match queue.
  public static Queue<User> getDeepCopyMatchQueue() {
    return new LinkedList<User>(matchQueue);
  }

  // FIFO matchmaker -- takes in the user who just requested a match and matches them
  // with anyone else who is in the queue waiting to be matched or adds them to the
  // queue to wait to be matched if no on else is in there
  public static void generateMatch(User user) {
    user.updateMatchPendingStatus(true);

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

  // Helper method for actually adding to people into one another's matches and sending
  // notifications to both users
  private static void createMatch(User firstUser, User secondUser) {
    long currTime = System.currentTimeMillis();

    DatabaseHandler.addNotification(firstUser.getId(), 
      secondUser.getId(), currTime, DatabaseHandler.MATCHING);
    DatabaseHandler.addNotification(secondUser.getId(), 
      firstUser.getId(), currTime, DatabaseHandler.MATCHING);

    firstUser.updateMatchPendingStatus(false);
    secondUser.updateMatchPendingStatus(false);

    firstUser.updateMatches();
    secondUser.updateMatches();    
  }

  // Helper method for clearing out the match queue
  // this is primarily for testing purposes at the moment
  public static void clearQueue() {
    matchQueue = new LinkedList<>();
  }
}

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
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

public final class MatchManager {

  // Integrate datastore so that match requests aren't lost if the server restarts
  private static HashSet<User> matchQueue = new HashSet<>();
  private static double MINIMUM_INTERESTS_THRESHOLD = 0.6;
  private static int QUESTIONS = 5;

  private MatchManager() {

  }

  // Getter method for the match queue (for testing purposes)
  public static HashSet<User> getMatchQueue() {
    return matchQueue;
  }

  // Getter method to return a deep copy of the match queue (for testing purposes).
  // The returned queue can be modified without the changes effecting the actual match queue.
  public static HashSet<User> getDeepCopyMatchQueue() {
    return new HashSet <User>(matchQueue);
  }

  // Setter method to add users directly to the matchqueue (for testing purposes).
  public static void addUserToMatchQueue(User u) {
      matchQueue.add(u);
  }

  // Matchmaker -- takes in the user who just requested a match and matches them
  // with the one who is in the queue having the highest number of mutual interests with the person
  // or adds them to the
  // queue to wait to be matched if no on else is in there
  public static void generateMatch(User user) {
    user.updateMatchPendingStatus(true);

    
    if (matchQueue.isEmpty()) {
        matchQueue.add(user); 
        return;
    }

    User matchResult = findCompatibleMatch(user);
    if (matchResult == null) {
        matchQueue.add(user); 
        user.updateMatchPendingStatus(true);
        return;
    }

    if (!matchResult.isMatchedWith(user) && !matchResult.equals(user)) {
        // actually removing the match result from the queue
        createMatch(matchResult, user);
        matchQueue.remove(matchResult);  
        
        // adding the user to the queue in case there wasn't a successful match for them
        // this is so they can be matched later      
    } else if (!matchQueue.contains(user)) {  
        matchQueue.add(user); 
    }      
  }

  // Match the user with the most compatible user in the database (the one who has the most number
  // of similar interests). 
  // Currently, the minimum percentage of mutual interests between 2 users required to get matched are 60%
  public static User findCompatibleMatch(User firstUser) {
    HashMap<User, Integer> mutualInterests = new HashMap<>(); 
    for (User secondUser: matchQueue) {
        mutualInterests.put(secondUser, 0);
        for (int i = 0; i < secondUser.getPreferences().size(); i++) {
            // Compare the second user's each preference answer with the first user's
            if (firstUser.getPreferences().get(i).equals(secondUser.getPreferences().get(i))) {
                mutualInterests.replace(secondUser, mutualInterests.get(secondUser)+1);
            }
        }
    }

    //Traverse through the hashmap to find the user with the maximum number of mutual interests with the first user
    int currHighestScore = 0; 
    String currHighestUser = "";

    for (Map.Entry<User, Integer> mapE: mutualInterests.entrySet()) {
        if (currHighestScore < (int)mapE.getValue()) {
            currHighestScore = (int) mapE.getValue();
            currHighestUser = ((User)mapE.getKey()).getId();
        }
    }

    if (currHighestScore < MINIMUM_INTERESTS_THRESHOLD * QUESTIONS) {
        return null;
    }

    User highestUser = new User(currHighestUser);
    return highestUser;
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
    matchQueue = new HashSet<>();
  }
}

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

import java.util.Queue;
import java.util.Collection;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.sps.data.user.User;
import org.apache.commons.collections4.CollectionUtils;
import com.google.sps.data.matching.MatchManager;

/** */
@RunWith(JUnit4.class)
public final class MatchManagerTest {

  // Some people that we can use in our tests.
  private static final User USER_A = new User("userA@email.com", "User A");
  private static final User USER_B = new User("userB@email.com", "User B");
  private static final User USER_C = new User("userC@email.com", "User C");


  private void breakDown(Collection<User> users) {
    MatchManager.clearQueue();

    for (User user: users) {
      user.clearMatches();  
    }
  }

  @Test
  public void testEmptyQueue() {
    // Test for when no one is in the matching queue. The user should be added to the
    // queue and their match list should be empty.
    MatchManager.generateMatch(USER_A);

    Queue matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.contains(USER_A));
    Assert.assertTrue(CollectionUtils.isEmpty(USER_A.getMatches()));
    
    breakDown(Arrays.asList(USER_A));  
  }

  @Test
  public void testSuccessfulMatch() {   
    //Test for a successful match
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_B);

    Queue matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.isEmpty());
    Assert.assertTrue(USER_A.isMatchedWith(USER_B));
    Assert.assertTrue(USER_B.isMatchedWith(USER_A));

    breakDown(Arrays.asList(USER_A, USER_B));  
  }

  @Test
  public void testUserTryingToMatchTwice() {
    // Test for when a user tries to match twice consecutively but no one else is
    // in the match queue
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_A);

    Queue matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.contains(USER_A));

    breakDown(Arrays.asList(USER_A));    
  }

  @Test
  public void testUsersAlreadyMatched() {
    // Test for when a user requests a match but the only other person in the match
    // queue is someone they are already matched with
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_B);

    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_B);

    Queue matchQueue = MatchManager.getMatchQueue();

    Assert.assertEquals(matchQueue.size(), 2);
    Assert.assertEquals(USER_A.getMatches().size(), 1);
    Assert.assertEquals(USER_B.getMatches().size(), 1);

    // Third user enters the queue
    MatchManager.generateMatch(USER_C);

    Assert.assertEquals(matchQueue.size(), 1);
    Assert.assertTrue(USER_A.isMatchedWith(USER_C));
    Assert.assertTrue(USER_C.isMatchedWith(USER_A));

    MatchManager.generateMatch(USER_C);

    Assert.assertTrue(matchQueue.isEmpty());
    Assert.assertTrue(USER_B.isMatchedWith(USER_C));
    Assert.assertTrue(USER_C.isMatchedWith(USER_B));

    breakDown(Arrays.asList(USER_A, USER_B, USER_C));    
  }
}

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
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.collections4.CollectionUtils;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/** */
@RunWith(JUnit4.class)
public final class MatchManagerTest {

  // Some people that we can use in our tests.
  private static final User USER_A = new User("1");
  private static final User USER_B = new User("2");
  private static final User USER_C = new User("3");

  // local datastore service for testing
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
    MatchManager.clearQueue();
  }

  // helper method for adding a user to the database
  private void addUserToDatabase() {
    DatabaseHandler.addUser("User", "A", 1, 1, 2000, "userA@email.com", "1");
    DatabaseHandler.addUser("User", "B", 2, 2, 2002, "userB@email.com", "2");
    DatabaseHandler.addUser("User", "C", 3, 3, 2003, "userC@email.com", "3");
  }

  @Test
  public void testEmptyQueue() {
    // Test for when no one is in the matching queue. The user should be added to the
    // queue and their match list should be empty.
    MatchManager.generateMatch(USER_A);

    Queue matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.contains(USER_A));
    Assert.assertTrue(CollectionUtils.isEmpty(USER_A.getMatches()));  
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
  }

  @Test
  public void testUserTryingToMatchTwice() {
    // Test for when a user tries to match twice consecutively but no one else is
    // in the match queue
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_A);

    Queue matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.contains(USER_A));   
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
  }
}

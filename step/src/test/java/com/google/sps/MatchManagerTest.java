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
import java.util.HashSet;
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
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUser("User", "B", "2", "2", "2002", "userB@email.com", "2");
    DatabaseHandler.addUser("User", "C", "3", "3", "2003", "userC@email.com", "3");
    DatabaseHandler.addUser("User", "D", "4", "4", "2004", "userD@email.com", "4");
    DatabaseHandler.addUser("User", "E", "5", "5", "2005", "userE@email.com", "5");
    DatabaseHandler.addUser("User", "F", "6", "6", "2006", "userF@email.com", "6");
    DatabaseHandler.addUser("User", "G", "7", "7", "2007", "userG@email.com", "7");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel");
    DatabaseHandler.addUserPref("2", "Yes", "No", "Yes", "Gold", "Laurel");
    DatabaseHandler.addUserPref("3", "Yes", "No", "Yes", "Blue", "Yanny");
    DatabaseHandler.addUserPref("4", "Yes", "No", "No", "Blue", "Yanny");
    DatabaseHandler.addUserPref("5", "Yes", "Yes", "No", "Blue", "Yanny");
    DatabaseHandler.addUserPref("6", "No", "No", "Yes", "Gold", "Laurel");
    DatabaseHandler.addUserPref("7", "No", "Yes", "Yes", "Gold", "Laurel");
  }

  @Test
  public void testEmptyQueue() {
    // Test for when no one is in the matching queue. The user should be added to the
    // queue and their match list should be empty.
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel");

    MatchManager.generateMatch(USER_A);
    HashSet matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.contains(USER_A));
    Assert.assertTrue(CollectionUtils.isEmpty(USER_A.getMatches()));  
  }

  @Test
  public void testSuccessfulMatch() {
    User USER_A = new User("1");
    User USER_B = new User("2");
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel"); 
    DatabaseHandler.addUser("User", "B", "2", "2", "2002", "userB@email.com", "2");
    DatabaseHandler.addUserPref("2", "Yes", "No", "Yes", "Gold", "Laurel");

    //Test for a successful match
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_B);

    HashSet matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.isEmpty());
    Assert.assertTrue(USER_A.isMatchedWith(USER_B));
    Assert.assertTrue(USER_B.isMatchedWith(USER_A));  
  }

  @Test
  public void testUserTryingToMatchTwice() {
    User USER_A = new User("1");
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel"); 

    // Test for when a user tries to match twice consecutively but no one else is
    // in the match queue
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_A);

    HashSet matchQueue = MatchManager.getMatchQueue();

    Assert.assertTrue(matchQueue.contains(USER_A));   
    Assert.assertEquals(matchQueue.size(), 1);
  }

  @Test
  public void testUsersAlreadyMatched() {
    User USER_A = new User("1");
    User USER_B = new User("2");
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel"); 
    DatabaseHandler.addUser("User", "B", "2", "2", "2002", "userB@email.com", "2");
    DatabaseHandler.addUserPref("2", "Yes", "No", "Yes", "Gold", "Laurel");

    // Test for when a user requests a match but the only other person in the match
    // queue is someone they are already matched with
    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_B);

    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_B);

    HashSet matchQueue = MatchManager.getMatchQueue();

    Assert.assertEquals(matchQueue.size(), 2);
    Assert.assertEquals(USER_A.getMatches().size(), 1);
    Assert.assertEquals(USER_B.getMatches().size(), 1);
  }

  // Test if there are two users in matchQueue but they have less than required mutual interests, then they won't be matched
  @Test
  public void testFindCompatibleMatch_minReq() {
    User USER_A = new User("1");
    User USER_D = new User("4");
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel"); 
    DatabaseHandler.addUser("User", "D", "4", "4", "2004", "userD@email.com", "4");
    DatabaseHandler.addUserPref("4", "Yes", "No", "No", "Blue", "Yanny");

    MatchManager.generateMatch(USER_A);
    MatchManager.generateMatch(USER_D);

    HashSet matchQueue = MatchManager.getMatchQueue();

    Assert.assertEquals(matchQueue.size(), 2);
    Assert.assertEquals(USER_A.getMatches().size(), 0);
    Assert.assertEquals(USER_D.getMatches().size(), 0);
    Assert.assertTrue(!USER_A.isMatchedWith(USER_D));
    Assert.assertTrue(!USER_D.isMatchedWith(USER_A));
  }

  // Return the user with the highest number of mutual interests with the first user
  @Test
  public void testFindMostCompatibleMatch() {
    User USER_A = new User("1");
    User USER_C = new User("3");
    User USER_F = new User("6");
    
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUser("User", "C", "3", "3", "2003", "userC@email.com", "3");
    DatabaseHandler.addUser("User", "F", "6", "6", "2006", "userF@email.com", "6");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel"); 
    DatabaseHandler.addUserPref("3", "Yes", "No", "Yes", "Blue", "Yanny");
    DatabaseHandler.addUserPref("6", "No", "No", "Yes", "Gold", "Laurel");

    MatchManager.generateMatch(USER_C);
    MatchManager.generateMatch(USER_F);

    HashSet matchQueue = MatchManager.getMatchQueue();

    //USER_C & USER_F should not be matched because they have only 40% interests in common
    Assert.assertEquals(matchQueue.size(), 2);
    Assert.assertEquals(USER_C.getMatches().size(), 0);
    Assert.assertEquals(USER_F.getMatches().size(), 0);
    Assert.assertTrue(!USER_C.isMatchedWith(USER_F));
    Assert.assertTrue(!USER_F.isMatchedWith(USER_C));

    MatchManager.generateMatch(USER_A);

    // USER_A & USER_C should be matched because they have 80% interests in common
    Assert.assertEquals(MatchManager.getMatchQueue().size(), 1);
    Assert.assertEquals(USER_A.getMatches().size(), 1);
    Assert.assertEquals(USER_C.getMatches().size(), 0);
    //Assert.assertEquals(USER_F.getMatches().size(), 1);
    Assert.assertTrue(!USER_A.isMatchedWith(USER_C));
    Assert.assertTrue(!USER_C.isMatchedWith(USER_A));
    Assert.assertTrue(USER_A.isMatchedWith(USER_F));
    Assert.assertTrue(USER_F.isMatchedWith(USER_A));
  }

  @Test
  public void testFIFO() {
    User USER_A = new User("1");
    User USER_C = new User("3");
    User USER_G = new User("7"); 

    // USER_C and USER_G each have 3 mutual interests with USER_A
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUser("User", "C", "3", "3", "2003", "userC@email.com", "3");
    DatabaseHandler.addUser("User", "G", "6", "6", "2006", "userG@email.com", "7");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel"); 
    DatabaseHandler.addUserPref("3", "Yes", "No", "Yes", "Blue", "Yanny");
    DatabaseHandler.addUserPref("7", "No", "Yes", "Yes", "Gold", "Laurel");

    MatchManager.generateMatch(USER_C);
    MatchManager.generateMatch(USER_G);

    HashSet matchQueue = MatchManager.getMatchQueue();

    //USER_C & USER_G should not be matched because they have only 20% interests in common
    Assert.assertEquals(matchQueue.size(), 2);
    Assert.assertEquals(USER_C.getMatches().size(), 0);
    Assert.assertEquals(USER_G.getMatches().size(), 0);
    Assert.assertTrue(!USER_C.isMatchedWith(USER_G));
    Assert.assertTrue(!USER_G.isMatchedWith(USER_C));

    // After USER_A enters the queue, USER_A should get matched with USER_C because USER_C entered the queue before USER_G
    MatchManager.generateMatch(USER_A);

    // USER_A & USER_C should be matched because they have 80% interests in common
    Assert.assertEquals(MatchManager.getMatchQueue().size(), 1);
    Assert.assertEquals(USER_A.getMatches().size(), 1);
    Assert.assertEquals(USER_G.getMatches().size(), 0);
    Assert.assertTrue(USER_A.isMatchedWith(USER_C));
    Assert.assertTrue(USER_C.isMatchedWith(USER_A));
    Assert.assertTrue(!USER_A.isMatchedWith(USER_G));
    Assert.assertTrue(!USER_G.isMatchedWith(USER_A));
  }
}

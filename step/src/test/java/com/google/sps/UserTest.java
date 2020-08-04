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

import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
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
public final class UserTest {

  private static final User USER_A = new User("1");
  private static final User USER_B = new User("2");

  // local datastore service for testing
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());  

  @Before
  public void setUp() {
    helper.setUp();
    addUsersToDatabase();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  // helper method for adding a user to the database
  private void addUsersToDatabase() {
    DatabaseHandler.addUser("User", "A", "1", "1", "2000", "userA@email.com", "1");
    DatabaseHandler.addUser("User", "B", "2", "2", "2002", "userB@email.com", "2");
    DatabaseHandler.addUserPref("1", "Yes", "No", "Yes", "Gold", "Laurel");
    DatabaseHandler.addUserPref("2", "Yes", "No", "Yes", "Gold", "Laurel");
  }

  @Test
  // Testing email fetching
  public void testGetEmail() {
    Assert.assertEquals(USER_A.getEmail(), "userA@email.com");
    Assert.assertEquals(USER_B.getEmail(), "userB@email.com");
  }

  @Test
  // Testing name fetching
  public void testGetName() {
    Assert.assertEquals(USER_A.getName(), "User A");
    Assert.assertEquals(USER_B.getName(), "User B");  
  }

  @Test
  // Testing preferences fetching
  public void testGetPreference() {
    Assert.assertEquals(USER_A.getPreferences(), new ArrayList<>(Arrays.asList("Yes", "No", "Yes", "Gold", "Laurel")));
    Assert.assertEquals(USER_B.getPreferences(), new ArrayList<>(Arrays.asList("Yes", "No", "Yes", "Gold", "Laurel")));
  }
  @Test
  // Testing match fetching
  public void testMatchFunctionality() {
    User USER_A = new User("1");
    User USER_B = new User("2");
    addUsersToDatabase();
    // Test for when a user does not yet have any matches
    Assert.assertEquals(USER_A.getMatches(), new ArrayList<>());

    // Test for when the user is the only person in the queue
    MatchManager.generateMatch(USER_A);
    Assert.assertEquals(USER_A.getMatches(), new ArrayList<>());

    // Test for when two users have been matched
    MatchManager.generateMatch(USER_B);
    Assert.assertTrue(USER_A.isMatchedWith(USER_B));
    Assert.assertEquals(USER_B.getMatches(), new ArrayList<>(Arrays.asList(USER_A)));
    Assert.assertEquals(USER_A.getMatches(), new ArrayList<>(Arrays.asList(USER_B)));
    Assert.assertTrue(USER_B.isMatchedWith(USER_A));  
  }

  @Test
  // Testing the overriden equals method in the User class
  public void testEquals() {
    Assert.assertEquals(USER_A, USER_A);
    Assert.assertNotEquals(USER_A, USER_B);    
  }

  @Test
  // Testing the overriden hashcode method in the User class
  public void testHashCode() {
    Assert.assertEquals(USER_A.hashCode(), "1".hashCode());  
  }

  @Test
  // Testing match pending functionality
  public void testMatchPending() {
    Assert.assertFalse(USER_A.isMatchPending());
    USER_A.updateMatchPendingStatus(true);
    Assert.assertTrue(USER_A.isMatchPending());
  }

}

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
import java.util.LinkedList;
import java.util.Collection;
import java.util.NoSuchElementException;
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
public final class DatabaseHandlerTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  // helper method for adding a user to the database
  private void addUserToDatabase() {
    DatabaseHandler.addUser("User", "A", 1, 1, 2000, "userA@email.com", "1");
  }

  // helper method for adding notifications to the database
  private void addNotificationsToDatabase() {
    DatabaseHandler.addNotification("A", "B", 1, "matching");
    DatabaseHandler.addNotification("B", "A", 1, "matching");
    DatabaseHandler.addNotification("A", "B", 2, "message");
  }

  @Test
  // Testing that a user's email is retrieved correctly
  public void testGetEmail() {
    addUserToDatabase();
    String email = DatabaseHandler.getUserEmail("1");
    Assert.assertEquals(email, "userA@email.com");
  }

  @Test
  // Testing that a user's name is retrieved correctly
  public void testGetUserName() {
    addUserToDatabase();
    String name = DatabaseHandler.getUserName("1");
    Assert.assertEquals(name, "User A");
  }

  @Test
  // Test for when there is no such user in the databse
  public void testNoSuchUser() {
    String email = DatabaseHandler.getUserEmail("1");
    Assert.assertEquals(email, null);
    String name = DatabaseHandler.getUserName("1");
    Assert.assertEquals(name, null);
  }

  @Test
  // Test for when a user has no notifications
  public void testNoNotifications() {
    Assert.assertEquals(DatabaseHandler.getUserNotifications("C"), new LinkedList<>());
  }

  @Test
  // Testing notification fetching
  public void testGetNotifications() {
    addNotificationsToDatabase();
    Collection<Notification> actual = DatabaseHandler.getUserNotifications("A");
    Collection<Notification> expected = new LinkedList<>(Arrays.asList(
      new MessageNotification("A", "B", 2), new MatchNotification("A", "B", 1)));
    Assert.assertEquals(actual, expected);

    actual = DatabaseHandler.getUserNotifications("B");
    expected = new LinkedList<>(Arrays.asList(
      new MatchNotification("B", "A", 1)));
    Assert.assertEquals(actual, expected);    
  } 

  @Test
  // Testing match fetching for a user with no matches
  public void testNoMatches() {
    Assert.assertEquals(DatabaseHandler.getUserMatches("A"), new LinkedList<>());
  }

  @Test
  // Testing match fetching for users with matches
  public void testGetMatches() {
   addNotificationsToDatabase();
   Collection<User> actual = DatabaseHandler.getUserMatches("A"); 
   Collection<User> expected = new LinkedList<>(Arrays.asList(new User("B"))); 
   Assert.assertEquals(actual, expected); 
  }

}

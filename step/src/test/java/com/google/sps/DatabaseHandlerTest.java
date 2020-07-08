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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.collections4.CollectionUtils;

/** */
@RunWith(JUnit4.class)
public final class DatabaseHandlerTest {

  private static final User USER_A = new User(1, "userA@email.com", "User A");
  private static final User USER_B = new User(2, "userB@email.com", "User B");
  private static final User USER_C = new User(3, "userC@email.com", "User C");

  private static final Notification MATCH_NOTIFICATION =
    new MatchNotification(1, "User A", 1);
  private static final Notification MESSAGE_NOTIFICATION =
    new MessageNotification(2, "User B", 2);  

  // Adds a user to the database and then retrieves that user to ensure it was
  // added correctly.
  @Test
  // Adds a user to the database and then retrieves that user to ensure it was
  // added correctly.
  public void testAddAndGetUser() {
    DatabaseHandler.addUserToDatabase(USER_A);

    User fetchedUser = DatabaseHandler.getUserById(1);
    Assert.assertEquals(fetchedUser, USER_A);
    DatabaseHandler.clearSavedUsers();
  }

  // Tests that a NoSuchElementException is thrown when a user is retrieved who
  // is not in the database.
  @Test(expected = NoSuchElementException.class)
  public void testNoSuchElementException() {
    DatabaseHandler.getUserById(USER_B.getId());
  }

  // Adds and removes a user from the databse then checks that an exception is
  // thrown when that user is fetched again.
  @Test(expected = NoSuchElementException.class)
  public void TestRemoveElement() {
    DatabaseHandler.addUserToDatabase(USER_A);
    DatabaseHandler.removeUserById(USER_A.getId());
    DatabaseHandler.getUserById(USER_A.getId());  
  }

  // Test for adding and retrieving notifications
  @Test(expected = NoSuchElementException.class)
  public void testAddAndGetNotifications() {
    
    /* Case where the user does not have any notifications yet and so
       has not been added to the notification datastore. This should
       throw a NoSuchElementException */ 
    DatabaseHandler.getNotificationsById(USER_A.getId());

    // Adding and retrieving one notification
    DatabaseHandler.addNotification(MATCH_NOTIFICATION);
    Collection<Notification> actual =
      DatabaseHandler.getNotificationsById(USER_A.getId());
    Collection<Notification> expected =
      new LinkedList<>(Arrays.asList(MATCH_NOTIFICATION));
    Assert.assertEquals(actual, expected);

    // Adding another notification and retrieving all notifications
    // Notifications should be ordered from most recent to oldest
    DatabaseHandler.addNotification(MESSAGE_NOTIFICATION);
    actual = DatabaseHandler.getNotificationsById(USER_A.getId());
    expected = new LinkedList<>(Arrays.asList(MESSAGE_NOTIFICATION, MATCH_NOTIFICATION));
    Assert.assertEquals(actual, expected);          
  }

}

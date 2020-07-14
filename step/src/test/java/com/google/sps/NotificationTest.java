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

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/** */
@RunWith(JUnit4.class)
public final class NotificationTest {

  private static final String ONE = "1";
  private static final String TWO = "2";
  private static final String THREE = "3";

  private static final long TIMESTAMP_ONE = 1;
  private static final long TIMESTAMP_TWO = 2; 

  private static final MatchNotification MATCH_NOTIFICATION =
    new MatchNotification(ONE, TWO, TIMESTAMP_ONE);
  private static final MessageNotification MESSAGE_NOTIFICATION =
    new MessageNotification(TWO, ONE, TIMESTAMP_TWO);
  private static final MatchNotification MATCH_NOTIFICATION_COPY = 
    new MatchNotification(ONE, TWO, TIMESTAMP_TWO);
  private static final MatchNotification OTHER_MATCH_NOTIFICATION =
    new MatchNotification(THREE, TWO, TIMESTAMP_TWO);    


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
  }

  // helper method for adding users to the database
  private void addUsersToDatabase() {
    DatabaseHandler.addUser("User", "A", 1, 1, 2000, "userA@email.com", "1");
    DatabaseHandler.addUser("User", "B", 2, 2, 2002, "userB@email.com", "2");
  }

  // Testing the GetId method
  @Test
  public void testGetId() {
    Assert.assertEquals(MATCH_NOTIFICATION.getId(), ONE);
    Assert.assertEquals(MESSAGE_NOTIFICATION.getId(), TWO);
  }

  // Testing the getOtherUserId method
  @Test
  public void testGetOtherUserId() {
    Assert.assertEquals(MATCH_NOTIFICATION.getOtherUserId(), TWO);
    Assert.assertEquals(MESSAGE_NOTIFICATION.getOtherUserId(), ONE);
  }

  // Testing that match notifications return the correct text
  @Test
  public void testMatchNotification() {
    addUsersToDatabase();
    String actual = MATCH_NOTIFICATION.getText();
    String expected = "New match alert! You matched with User B";

    Assert.assertEquals(actual, expected);
  }

  // Testing that message notifications return the correct text
  @Test
  public void testMessageNotification() {
    addUsersToDatabase();
    String actual = MESSAGE_NOTIFICATION.getText();
    String expected = "New message from User A";

    Assert.assertEquals(actual, expected);
  }

  // Testing notification equality method
  @Test
  public void testEquals() {
    Assert.assertEquals(MATCH_NOTIFICATION, MATCH_NOTIFICATION);
    // these two notifications are not equal because they were not created
    // at the same time and thus have different timestamps
    Assert.assertNotEquals(MATCH_NOTIFICATION, MATCH_NOTIFICATION_COPY);
    Assert.assertNotEquals(MATCH_NOTIFICATION, OTHER_MATCH_NOTIFICATION);
    Assert.assertNotEquals(MATCH_NOTIFICATION, MESSAGE_NOTIFICATION);
  }

  @Test
  public void testHashCode() {
    int actual = MATCH_NOTIFICATION.hashCode();
    int expected = MATCH_NOTIFICATION.getId().hashCode() * 
      MATCH_NOTIFICATION.getOtherUserId().hashCode(); 

    Assert.assertEquals(actual, expected);
  }

}

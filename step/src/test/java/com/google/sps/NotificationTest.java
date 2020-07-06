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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** */
@RunWith(JUnit4.class)
public final class NotificationTest {

  private static final MatchNotification matchNotification =
    new MatchNotification(1, "Ngan", 1);
  private static final MessageNotification messageNotification =
    new MessageNotification(2, "Adam", 2);
  private static final MatchNotification matchNotificationCopy = 
    new MatchNotification(1, "Ngan", 1);
  private static final MatchNotification otherMatchNotification =
    new MatchNotification(3, "Laila", 3);    

  // Testing the GetId method
  @Test
  public void testGetId() {
    Assert.assertEquals(matchNotification.getId(), 1);
    Assert.assertEquals(messageNotification.getId(), 2);
  }

  // Testing the getOtherUser method
  @Test
  public void testGetOtherUser() {
    Assert.assertEquals(matchNotification.getOtherUser(), "Ngan");
    Assert.assertEquals(messageNotification.getOtherUser(), "Adam");
  }

  // Testing the getTimestamp method
  @Test
  public void testGetTimestamp() {
    Assert.assertEquals(matchNotification.getTimestamp(), 1);
    Assert.assertEquals(messageNotification.getTimestamp(), 2);
  }

  // Testing that match notifications return the correct text
  @Test
  public void testMatchNotification() {
    String actual = matchNotification.getText();
    String expected = "New match alert! You matched with Ngan";

    Assert.assertEquals(actual, expected);
  }

  // Testing that message notifications return the correct text
  @Test
  public void testMessageNotification() {
    String actual = messageNotification.getText();
    String expected = "New message from Adam";

    Assert.assertEquals(actual, expected);
  }

  // Testing notification equality method
  @Test
  public void testEquals() {
    Assert.assertEquals(matchNotification, matchNotification);
    Assert.assertEquals(matchNotification, matchNotificationCopy);
    Assert.assertNotEquals(matchNotification, otherMatchNotification);
    Assert.assertNotEquals(matchNotification, messageNotification);
  }

  @Test
  public void testHashCode() {
    int actual = matchNotification.hashCode();
    int expected = (int) matchNotification.getId() * 
      matchNotification.getOtherUser().hashCode() *
      (int) matchNotification.getTimestamp(); 

    Assert.assertEquals(actual, expected);
  }

}

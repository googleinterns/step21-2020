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

  private static final String ONE = "1";
  private static final String TWO = "2";
  private static final String THREE = "3"; 

  private static final MatchNotification matchNotification =
    new MatchNotification(ONE, ONE);
  private static final MessageNotification messageNotification =
    new MessageNotification(TWO, TWO);
  private static final MatchNotification matchNotificationCopy = 
    new MatchNotification(ONE, TWO);
  private static final MatchNotification otherMatchNotification =
    new MatchNotification(THREE, TWO);    

  // Testing the GetId method
  @Test
  public void testGetId() {
    Assert.assertEquals(matchNotification.getId(), "1");
    Assert.assertEquals(messageNotification.getId(), "2");
  }

  // Testing the getOtherUserId method
  @Test
  public void testGetOtherUserId() {
    Assert.assertEquals(matchNotification.getOtherUserId(), "1");
    Assert.assertEquals(messageNotification.getOtherUserId(), "2");
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
    // these two notifications are not equal because they were not created
    // at the same time and thus have different timestamps
    Assert.assertNotEquals(matchNotification, matchNotificationCopy);
    Assert.assertNotEquals(matchNotification, otherMatchNotification);
    Assert.assertNotEquals(matchNotification, messageNotification);
  }

  @Test
  public void testHashCode() {
    int actual = matchNotification.hashCode();
    int expected = matchNotification.getId().hashCode() * 
      matchNotification.getOtherUserId().hashCode(); 

    Assert.assertEquals(actual, expected);
  }

}

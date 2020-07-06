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

  @Test
  public void testMatchNotification() {
    String actual = matchNotification.getText();
    String expected = "New match alert! You matched with Ngan";

    Assert.assertEquals(actual, expected);
  }

  @Test
  public void testMessageNotification() {
    String actual = messageNotification.getText();
    String expected = "New message from Adam";

    Assert.assertEquals(actual, expected);
  }

}

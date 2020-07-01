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

  @Test
  public void testAddAndGetUser() {
    DatabaseHandler.addUserToDatabase(USER_A);

    User fetchedUser = DatabaseHandler.getUserById(1);
    Assert.assertEquals(fetchedUser, USER_A);
    DatabaseHandler.clearDatabase();
  }

  @Test(expected = NoSuchElementException.class)
  public void testNoSuchElementException() {
    DatabaseHandler.getUserById(2);
  }

  @Test(expected = NoSuchElementException.class)
  public void TestRemoveElement() {
    DatabaseHandler.addUserToDatabase(USER_A);
    DatabaseHandler.removeUserById(1);
    DatabaseHandler.getUserById(1);  
  }

}

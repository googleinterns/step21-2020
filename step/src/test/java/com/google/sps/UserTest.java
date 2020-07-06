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
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.collections4.CollectionUtils;

/** */
@RunWith(JUnit4.class)
public final class UserTest {

  // Some people that we can use in our tests.
  private static final User USER_A = new User(1, "userA@email.com", "User A");
  private static final User USER_B = new User(2, "userB@email.com", "User B");

  @Test
  public void testGetId() {
    Assert.assertEquals(USER_A.getId(), 1);    
  }

  @Test
  public void testGetEmail() {
    Assert.assertEquals(USER_A.getEmail(), "userA@email.com");
  }

  @Test
  public void testGetName() {
    Assert.assertEquals(USER_A.getName(), "User A");  
  }

  @Test
  public void testMatchFunctionality() {
    Assert.assertEquals(USER_A.getMatches(), new HashSet<>());

    USER_A.addMatch(USER_B);
    Assert.assertEquals(USER_A.getMatches(),
      new HashSet<User>(Arrays.asList(USER_B)));
    Assert.assertTrue(USER_A.isMatchedWith(USER_B));  

    USER_A.removeMatch(USER_B);
    Assert.assertEquals(USER_A.getMatches(), new HashSet<>());
    Assert.assertFalse(USER_A.isMatchedWith(USER_B));

    USER_A.addMatch(USER_B);
    USER_A.clearMatches();
    Assert.assertEquals(USER_A.getMatches(), new HashSet<>());      
  }

  @Test
  public void testEquals() {
    Assert.assertEquals(USER_A, USER_A);
    Assert.assertNotEquals(USER_A, USER_B);    
  }

  @Test
  public void testHashCode() {
    Assert.assertEquals(USER_A.hashCode(), USER_A.getEmail().hashCode());  
  }

}

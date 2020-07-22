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
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.collections4.CollectionUtils;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

@RunWith(JUnit4.class)
public final class MessageTest {
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";

    private static final User USER_A = new User("1");
    private static final User USER_B = new User("2");

    private static final long TIMESTAMP_ONE = 1;
    private static final long TIMESTAMP_TWO = 2; 

    private static final Message MESSAGE_1 = new Message("1", "2", "Good Morning", TIMESTAMP_ONE);
    private static final Message MESSAGE_2 = new Message("2", "1", "Good Afternoon", TIMESTAMP_TWO);

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

    // Testing the getSenderID method
    @Test
    public void testgetSenderID() {
        Assert.assertEquals(MESSAGE_1.getSenderID(), "1");
        Assert.assertEquals(MESSAGE_2.getSenderID(), "2");
    }

    // Testing the getRecipientID method
    @Test
    public void testgetRecipientID() {
        Assert.assertEquals(MESSAGE_1.getRecipientID(), "2");
        Assert.assertEquals(MESSAGE_2.getRecipientID(), "1");
    }

    // Testing the text method
    @Test
    public void testgetText() {
        Assert.assertEquals(MESSAGE_1.getText(), "Good Morning");
        Assert.assertEquals(MESSAGE_2.getText(), "Good Afternoon");
    }

    // Testing the timestamp method
    @Test
    public void testTimestamp() {
        Assert.assertEquals(MESSAGE_1.timestamp(), TIMESTAMP_ONE);
        Assert.assertEquals(MESSAGE_2.timestamp(), TIMESTAMP_TWO);
    }

    @Test
    // Testing the overriden equals method in the Message class
    public void testEquals() {
        Assert.assertEquals(MESSAGE_1, MESSAGE_1);
        Assert.assertNotEquals(MESSAGE_2, MESSAGE_1);    
    }

    @Test
    // Testing the overriden hashcode method in the Message class
    public void testHashCode() {
        Assert.assertEquals(MESSAGE_1.getSenderID().hashCode() * MESSAGE_1.getRecipientID().hashCode(), "1".hashCode()* "2".hashCode());
        Assert.assertEquals(MESSAGE_2.getSenderID().hashCode() * MESSAGE_2.getRecipientID().hashCode(), "2".hashCode()* "1".hashCode());  
    }
}

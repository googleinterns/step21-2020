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

import java.lang.IllegalArgumentException;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

@RunWith(JUnit4.class)
public final class MessageHandlerTest {
    //Todo: Test Message Handler
    private static final User USER_A = new User("1");
    private static final User USER_B = new User("2");

    private static final String USER_A_ID = "1";
    private static final String USER_B_ID = "2";
    private static final String USER_C_ID = "3";

    private static final String TEXT_1 = "Text 1";
    private static final String TEXT_2 = "Text 2";
    private static final String TEXT_3 = "Text 3";

    private static final long TIMESTAMP_ONE = 1;
    private static final long TIMESTAMP_TWO = 2;

    private static final Message MESSAGE_1 = new Message(USER_A_ID, USER_B_ID, TEXT_1, TIMESTAMP_ONE);
    private static final Message MESSAGE_2 = new Message(USER_B_ID, USER_A_ID, TEXT_2, TIMESTAMP_TWO); 

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

    //helper method for adding messages to the database
    private void addMessageToDatabase() {
        MessageHandler.addMessage(MESSAGE_1);
        MessageHandler.addMessage(MESSAGE_2);
    }

    @Test
    public void testNoMessage() {
        addMessageToDatabase();
        Assert.assertEquals(MessageHandler.getMessages(USER_A_ID, USER_C_ID), new ArrayList<>());
    }

    @Test
    public void testGetMessage() {
        addMessageToDatabase();
        List<Message> actual = MessageHandler.getMessages(USER_A_ID, USER_B_ID);
        List<Message> expected = new ArrayList<>(Arrays.asList(MESSAGE_1, MESSAGE_2));
        Assert.assertEquals(actual, expected);
    }
}
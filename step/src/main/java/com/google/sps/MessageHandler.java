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

import java.util.Collection;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Comparator;
import com.google.sps.Message;
import java.util.Collections;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;


public final class MessageHandler {
  // Integrate datastore so that messages users send and receive from each other aren't lost if the server restarts
  private static List<Message> messages = new ArrayList<>();
  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  public static void addMessage(Message m) {
    Entity messageEntity = new Entity("Message");
    messageEntity.setProperty("Sender",m.getSenderID());
    messageEntity.setProperty("Recipient", m.getRecipientID());
    messageEntity.setProperty("Text", m.getText());
    messageEntity.setProperty("timestamp", m.timestamp());
    messageEntity.setProperty("timeStamp", m.gettimeStamp());
    datastore.put(messageEntity);
  }

  // Filter all the messages that the first user and the seconds user sent to each other
  public static ArrayList<Message> getMessages(String id, String otherUserID) {
    Filter firstUserSenderFilter = new FilterPredicate("Sender", FilterOperator.EQUAL, id);
    Filter secondUserRecipientFilter = new FilterPredicate("Recipient", FilterOperator.EQUAL, otherUserID);
    Filter firstUserRecipientFilter = new FilterPredicate("Recipient", FilterOperator.EQUAL, id);
    Filter secondUserSenderFilter = new FilterPredicate("Sender", FilterOperator.EQUAL, otherUserID);

    CompositeFilter compositeFilter = new CompositeFilter(CompositeFilterOperator.OR, Arrays.asList(
        new CompositeFilter(CompositeFilterOperator.AND, Arrays.asList(
            firstUserSenderFilter, secondUserRecipientFilter)), 
        new CompositeFilter(CompositeFilterOperator.AND, Arrays.asList(
            firstUserRecipientFilter, secondUserSenderFilter
    ))));

    Query query = new Query("Message").setFilter(compositeFilter);
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Message> messages = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        String sender = (String) entity.getProperty("Sender");
        String recipient = (String) entity.getProperty("Recipient");
        String text = (String) entity.getProperty("Text");
        long timestamp = (long) entity.getProperty("timestamp");
        String timeStamp = String.valueOf(entity.getProperty("timeStamp"));
        Message message = new Message(sender, recipient, text, timestamp, timeStamp);
        messages.add(message);
    }
    Collections.sort(messages, new Comparator<Message>() {
        public int compare(Message m1, Message m2) {
            return String.valueOf(m1.timestamp()).compareTo(String.valueOf(m2.timestamp()));
        }
    });
    return messages;
  } 
}


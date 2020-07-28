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

import java.util.List;
import java.util.ArrayList;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Variables of a message */
public final class Message {
  private final String senderID; 
  private final String recipientID; 
  private final String text; 
  private final long timestamp;
  private final String currTime;

  public Message(String senderID, String recipientID, String text, long timestamp, String currTime) {
      this.senderID = senderID;
      this.recipientID = recipientID;
      this.text = text;
      this.timestamp = timestamp;
      this.currTime = currTime;
  }
  public String getSenderID() {
      return senderID;
  }
  public String getRecipientID() {
      return recipientID;
  }
  public String getText() {
      return text;
  }
  public long timestamp() {
      return timestamp;
  }
  public String getCurrTime() {
      return currTime;
  }
  

  //Overriden equals method
  @Override
  public boolean equals(Object other) {
    if (this == other) {
        return true;
    }

    if (other == null || this.getClass() != other.getClass()) {
        return false;
    }

    Message message = (Message) other;
    return this.senderID.equals(message.getSenderID()) &&
           this.recipientID.equals(message.getRecipientID()) &&
           this.text.equals(message.getText()) &&
           this.currTime.equals(message.getCurrTime()) &&
           String.valueOf(this.timestamp).equals(String.valueOf(message.timestamp()));
  }

  //Overriden hashCode method
  @Override
  public int hashCode() {
      return senderID.hashCode() * recipientID.hashCode();
  }
}


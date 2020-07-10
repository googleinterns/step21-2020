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

package com.google.sps.data;

import java.util.List;
import java.util.ArrayList;

/** Variables of a comment */
public final class Message {
  private final long id; 
  private final String otherUserID;
  private final String email;
  private final List<String> messageList = new ArrayList<>();
  private final long timestamp;

  public Message(long id, String otherUserID, String email, List<String> messageList, long timestamp) {
    this.id = id;
    this.otherUserID = otherUserID;
    this.email = email;
    for (String text: messageList) {
        this.messageList.add(text);
    }
    this.timestamp = timestamp;
  }
  public long getID() {
      return id;
  }
  public String getOtherUserID() {
      return otherUserID;
  }
  public String getEmail() {
    return email; 
  }
  public List<String> getText() {
      return messageList;
  }
  public long getTimestamp() {
      return timestamp;
  }
}


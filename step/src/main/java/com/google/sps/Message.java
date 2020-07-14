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
import java.io.*; 

/** Variables of a comment */
public final class Message {
  private final String id; 
  private final String userID; 
  private final String text; 
  private final long timestamp;

  public Message(String id, String userID, String text, long timestamp) {
      this.id = id;
      this.userID = userID;
      this.text = text;
      this.timestamp = timestamp;
  }
  public String getID() {
      return id;
  }
  public String getUserID() {
      return userID;
  }
  public String getText() {
      return text;
  }
  public long timestamp() {
      return timestamp;
  }

  //Overriden equals method
  @Override
  public boolean equals(Object other) {
    return other instanceof Message && equals(this, (Message) other);
  }

  //Overriden hashCode method
  @Override
  public int hashCode() {
      return id.hashCode() * userID.hashCode();
  }
}


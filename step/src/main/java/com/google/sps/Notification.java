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

public abstract class Notification {
  private long id;  
  protected long otherUserId;
  private long timestamp;

  public Notification(long id, String otherUserId) {
    this.id = id;
    this.otherUserId = otherUserId;
    this.timestamp = System.currentTimeMillis();
  }
  
  // Getter method for the ID of the user with whom the notification is
  // associated.
  public long getId() {
    return id;    
  }

  // Method for getting the id of the user who sent the notification.
  public String getOtherUserId() {
    return otherUserId;  
  }
  
  // Getter method for the time at which the notification occured.
  public long getTimestamp() {
    return timestamp;  
  }

  // Overriden equals method
  @Override
  public boolean equals(Object obj) { 
    if(this == obj) {
      return true; 
    } 
 
    if(obj == null || obj.getClass()!= this.getClass()) {
      return false;
    }
     
    Notification notification = (Notification) obj;
    return this.id == notification.getId() &&
           this.otherUser.equals(notification.getOtherUser()) &&
           this.timestamp == notification.getTimestamp(); 
  }

  // Overriden hashCode method
  @Override
  public int hashCode() { 
    return (int) id * otherUser.hashCode(); 
  }

  // Method for putting together and returning the text associated
  // with the notification.
  public abstract String getText();
}

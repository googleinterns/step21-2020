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
  protected String name;
  private long timeStamp;

  public Notification(long id, String name, long timeStamp) {
    this.id = id;
    this.name = name;
    this.timeStamp = timeStamp;
  }
  
  public long getId() {
    return id;    
  }

  public long getTimeStamp() {
    return timeStamp;  
  }

  // Method for putting together and returning the text associated
  // with the notification.
  public abstract String getText();
}

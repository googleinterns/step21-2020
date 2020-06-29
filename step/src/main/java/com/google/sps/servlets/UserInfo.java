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

/** Variables of an user */
public final class UserInfo {
  private final long id;
  private final String email;
  private final long timestamp;

  public UserInfo(long id, String email, long timestamp) {
    this.id = id;
    this.email = email;
    this.timestamp = timestamp;
  }
  public long getID() {
    return id; 
  }
  public String getEmail() {
    return email; 
  }
  public long getTimestamp() {
    return timestamp;
  }
}
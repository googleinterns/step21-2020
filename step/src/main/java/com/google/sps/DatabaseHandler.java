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
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public final class DatabaseHandler {
  
  // TODO: replace with Datastore
  private static Map<Long, User> idMap = new HashMap<>();

  private DatabaseHandler() {

  }

  // TODO: call this method when a user first signs up
  // Method for adding a user to the database
  public static void addUserToDatabase(User user) {
    idMap.put(user.getId(), user);    
  }

  // Method for getting a user object using an id
  public static User getUserById(long id) throws NoSuchElementException {
    if (idMap.containsKey(id)) {
      return idMap.get(id);    
    } else {
      throw new NoSuchElementException();  
    }    
  }

  // Method for removing a user from our database
  public static void removeUserById(long id) {
    if (idMap.containsKey(id)) {
      idMap.remove(id);    
    }
  }

  public static void clearDatabase() {
    idMap = new HashMap<>();  
  }

}

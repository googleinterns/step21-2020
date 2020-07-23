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

import java.util.Arrays;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.CalendarManager;
import com.google.sps.User;

@WebServlet("/cal")
public class CalendarServlet extends HttpServlet {

  @Override
  public void init() {

  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String monthString = request.getParameter("month");
    String dayString = request.getParameter("day");
    String yearString = request.getParameter("year");
    String hourString = request.getParameter("hour");
    String minuteString = request.getParameter("minute");
    String guestName = request.getParameter("guestName");

    if(monthString == null|| dayString == null || yearString == null 
        || hourString == null || minuteString == null || guestName == null) {
      System.out.println("one or more values are null");
    } else {

      System.out.println(monthString + " / " + dayString + " / " + yearString 
                          + " " + hourString + ":" + minuteString + ". " + guestName);

      int monthInt = Integer.parseInt(monthString);
      int dayInt = Integer.parseInt(dayString);
      int yearInt = Integer.parseInt(yearString);
      int hourInt = Integer.parseInt(hourString);
      int minuteInt = Integer.parseInt(minuteString);

      User hostUser = new User(UserServiceFactory.getUserService().getCurrentUser().getUserId());
      User guestUser = null;
      System.out.println("hostUser matches: " + hostUser.getMatches().toString());
      for(User user : hostUser.getMatches()) {
        System.out.println("input: " + guestName);
        System.out.println("found: " + user.getName());
        if(user.getName().equals(guestName)) {
          guestUser = user;
        }
      }

      if(guestUser == null) {
        System.out.println("Unable to find the guest user, unable to create Google Calendar event.");
      } else {
        CalendarManager.createMatchEvent(hostUser, guestUser, 
                                          yearInt, monthInt, dayInt, hourInt, minuteInt);
      }
    }

    response.sendRedirect("/profile.jsp");
  }

}

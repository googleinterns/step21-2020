// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.*;
import javax.servlet.*;  
import javax.servlet.http.*; 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import com.google.sps.data.UserInfo;


@WebServlet ("/Logout")
public class LogOutServlet extends HttpServlet {
    List<String> userEmails = new ArrayList<>();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        UserService userService = UserServiceFactory.getUserService();
        String userEmail = "";
        // Check if the user logs in or not
        if (userService.isUserLoggedIn()) {
            userEmail = userService.getCurrentUser().getEmail();
            out.println(userEmail);
        } else {
            response.sendRedirect("index.jsp");
        }
        // Check if the user logged in before or not
        for (String u: userEmails) {
            // If it is the first time, tell the user to fill out the preference form
            if (u.equals(userEmail)) response.sendRedirect("preferenceform.jsp");
        }
        userEmails.add(userEmail);
        response.sendRedirect("portfolio.jsp");
    }
}

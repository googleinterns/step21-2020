import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class AlertManager {
  private AlertManager() {}

  // This method will set response.sendRedirect() so that the user will receive a JavaScript alert
  // when they are redirected. This method won't work if redirectUrl hasn't implemented an onload
  // JS function to handle an 'alert' query parameter.
  public static void setAlert(String message, String redirectUrl, HttpServletResponse response) throws IOException {
    response.sendRedirect(redirectUrl + "?alert=" + message);
  }
}

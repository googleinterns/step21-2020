import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class AlertManager {
  private AlertManager() {}

  public static void setAlert(String message, String redirectUrl, HttpServletResponse response) throws IOException {
    response.sendRedirect(redirectUrl + "?alert=" + message);
  }
}

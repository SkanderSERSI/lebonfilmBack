package servlets;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import services.UserService;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  // Connexion a l’application Web
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject retour = new JSONObject();
    try {
      JSONObject temp = new JSONObject(json);
      String username = temp.getString("username");
      String password = temp.getString("password");
      retour = UserService.login(username, password, response);
      if (response.getStatus() == 200) {
        // Creation de la session de l'utilisateur
        HttpSession session = request.getSession();
        session.setAttribute("username", username);
        // Expiration de la session au bout de 15 minutes d'inactivite
        session.setMaxInactiveInterval(900);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  // for Preflight
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    setAccessControlHeaders(resp);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  private void setAccessControlHeaders(HttpServletResponse resp) {
    resp.addHeader("Access-Control-Allow-Origin", "https://lebonfilm.herokuapp.com");
    resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
    resp.setHeader("Access-Control-Allow-Headers",
        "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
    resp.setHeader("Access-Control-Allow-Credentials", "true");
  }

}

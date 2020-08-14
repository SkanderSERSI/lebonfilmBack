package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import dao.UserTools;
import services.JSONMessage;

/**
 * Servlet implementation class IsConnectedServlet
 */
@WebServlet("/isConnected")
public class IsConnectedServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  // Recuperer les infos de session (connecte ou deconnecte)
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    JSONObject retour = new JSONObject();
    try {
      HttpSession session = request.getSession(false);
      if (session == null) {
        response.setStatus(200);
        retour = JSONMessage.response("You are not connected.", false);
      } else {
        JSONObject obj = UserTools.getUser((String) session.getAttribute("username"));
        if (obj != null) {
          response.setStatus(200);
          retour = JSONMessage.response(obj, "You are connected.");
        } else {
          response.setStatus(500);
          retour = JSONMessage.response("IsConnected failed.", true);
        }
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

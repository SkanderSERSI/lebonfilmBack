package servlets;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import services.LikeService;

/**
 * Servlet implementation class LikeServlet
 */
@WebServlet("/likes")
public class LikeServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  // Ajouter un like a un film
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject retour = new JSONObject();
    try {
      JSONObject temp = new JSONObject(json);
      String username = temp.getString("username");
      int tmdbId = temp.getInt("tmdb_id");
      retour = LikeService.addLike(username, tmdbId, response);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  // Retirer le like d’un film
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject retour = new JSONObject();
    try {
      JSONObject temp = new JSONObject(json);
      String username = temp.getString("username");
      int tmdbId = temp.getInt("tmdb_id");
      retour = LikeService.removeLike(username, tmdbId, response);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    JSONObject retour = new JSONObject();
    try {
      String username = request.getParameter("username");
      String id = request.getParameter("id");
      if (username != null) {
        // Recuperer tout les likes de l'utilisateur specifie en parametre
        retour = LikeService.getUserLikes(username, response);
      } else if (id != null) {
        // Recuperer tout les likes du film specifie en parametre
        retour = LikeService.getMovieLikes(id, response);
      } else {
        // Recuperer tous les likes de la BDD
        retour = LikeService.getLikes(response);
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
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
  }

}

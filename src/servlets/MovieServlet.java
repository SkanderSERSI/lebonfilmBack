package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import services.MovieService;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/movies")
public class MovieServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    JSONObject retour = new JSONObject();
    try {
      String id = request.getParameter("id");
      String trending = request.getParameter("trending");
      if (id != null) {
        // Recuperer les informations du film specifie en parametre
        retour = MovieService.getMovie(id, response);
      } else if (trending != null) {
        // Recuperer tous les films tendance de la BDD so trending = true
        retour = MovieService.getMovies(Boolean.parseBoolean(trending), response);
      } else {
        // Recuperer tous les films de la BDD
        retour = MovieService.getMovies(false, response);
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

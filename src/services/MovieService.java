package services;

import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import apis.ConsumingTMDB;
import dao.MovieTools;

public class MovieService {

  public static JSONObject getMovie(String id, HttpServletResponse response) throws JSONException {
    try {
      int tmdbId = Integer.parseInt(id);
      // Si le film n'existe pas dans notre bdd on recupere les infos du film en faisant appel a
      // l'api TMDB et ensuite on rajoute le film
      if (!MovieTools.movieExists(tmdbId)) {
        ConsumingTMDB tmdb = new ConsumingTMDB();
        tmdb.createMovie(tmdbId, false);
      }
      response.setStatus(200);
      return JSONMessage.response(MovieTools.getMovie(tmdbId), "Movie retrieved successfully.");
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve movie failed.", true);
    } catch (NumberFormatException e) {
      response.setStatus(404);
      return JSONMessage.response("Movie not found.", true);
    }
  }

  public static JSONObject getMovies(boolean trending, HttpServletResponse response)
      throws JSONException {
    try {
      if (trending) {
        response.setStatus(200);
        return JSONMessage.response(MovieTools.getTrendingMovies(),
            "Trending Movies retrieved successfully.");
      } else {
        response.setStatus(200);
        return JSONMessage.response(MovieTools.getMovies(), "Movies retrieved successfully.");
      }
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve movies failed.", true);
    }
  }

}

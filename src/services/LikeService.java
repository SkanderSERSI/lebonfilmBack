package services;

import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import dao.LikeTools;
import dao.MovieTools;
import dao.UserTools;
import tables.Like;

public class LikeService {

  public static JSONObject addLike(String username, int tmdbId, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(username) || !MovieTools.movieExists(tmdbId)) {
        response.setStatus(404);
        return JSONMessage.response("Username or movie not found.", true);
      } else {
        Like like = new Like(username, tmdbId);
        if (!LikeTools.likeExists(like)) {
          LikeTools.addLike(like);
          response.setStatus(201);
          return JSONMessage.response("You have liked successfully.", false);
        } else {
          response.setStatus(200);
          return JSONMessage.response("You have already liked.", false);
        }
      }
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Add like failed.", true);
    }
  }

  public static JSONObject removeLike(String username, int tmdbId, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(username) || !MovieTools.movieExists(tmdbId)) {
        response.setStatus(404);
        return JSONMessage.response("Username or movie not found.", true);
      } else {
        LikeTools.removeLike(username, tmdbId);
        response.setStatus(200);
        return JSONMessage.response("You removed like successfully.", false);
      }
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Remove like failed.", true);
    }
  }

  public static JSONObject getMovieLikes(String tmdbId, HttpServletResponse response)
      throws JSONException {
    try {
      int id = Integer.parseInt(tmdbId);
      if (!MovieTools.movieExists(id)) {
        response.setStatus(404);
        return JSONMessage.response("Movie not found", true);
      } else {
        response.setStatus(200);
        return JSONMessage.response(LikeTools.getMovieLikes(id),
            "Movie likes retrieved successfully.");
      }
    } catch (Exception e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve movie likes failed.", true);
    }
  }

  public static JSONObject getLikes(HttpServletResponse response) throws JSONException {
    try {
      response.setStatus(200);
      return JSONMessage.response(LikeTools.getLikes(), "Likes retrieved successfully.");
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve likes failed.", true);
    }
  }

  public static JSONObject getUserLikes(String username, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(username)) {
        response.setStatus(404);
        return JSONMessage.response("User not found.", true);
      } else {
        response.setStatus(200);
        return JSONMessage.response(LikeTools.getLikedMovies(username),
            "Liked movies retrieved successfully.");
      }
    } catch (Exception e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve liked movies failed.", true);
    }
  }

}

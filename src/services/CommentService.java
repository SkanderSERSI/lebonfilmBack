package services;

import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import dao.CommentTools;
import dao.MovieTools;
import dao.UserTools;
import tables.Comment;

public class CommentService {

  public static JSONObject addComment(Comment comment, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(comment.getUsername())
          || !MovieTools.movieExists(comment.getTmdbId())) {
        response.setStatus(404);
        return JSONMessage.response("Username or movie not found.", true);
      } else {
        CommentTools.addComment(comment);
        response.setStatus(201);
        return JSONMessage.response("You have added your comment successfully.", false);
      }
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Add comment failed.", true);
    }
  }

  public static JSONObject getMovieComments(String tmdbId, HttpServletResponse response)
      throws JSONException {
    try {
      int id = Integer.parseInt(tmdbId);
      if (!MovieTools.movieExists(id)) {
        response.setStatus(404);
        return JSONMessage.response("Movie not found", true);
      } else {
        response.setStatus(200);
        return JSONMessage.response(CommentTools.getMovieComments(id),
            "Movie comments retrieved successfully.");
      }
    } catch (Exception e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve movie comments failed.", true);
    }
  }

  public static JSONObject getComments(HttpServletResponse response) throws JSONException {
    try {
      response.setStatus(200);
      return JSONMessage.response(CommentTools.getComments(), "Comments retrieved successfully.");
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve comments failed.", true);
    }
  }

  public static JSONObject getUserComments(String username, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(username)) {
        response.setStatus(404);
        return JSONMessage.response("User not found.", true);
      } else {
        response.setStatus(200);
        return JSONMessage.response(CommentTools.getUserComments(username),
            "User comments retrieved successfully.");
      }
    } catch (Exception e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve user comments failed.", true);
    }
  }

}

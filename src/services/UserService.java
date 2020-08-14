package services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import dao.UserTools;
import tables.User;

public class UserService {

  public static JSONObject createUser(User user, HttpServletResponse response)
      throws JSONException {
    try {
      if (UserTools.usernameExists(user.getUsername())) {
        response.setStatus(400);
        return JSONMessage.response("Username already taken.", true);
      } else if (UserTools.emailExists(user.getEmail())) {
        response.setStatus(400);
        return JSONMessage.response("Email already used.", true);
      } else {
        // creation de la cle de Salage
        String salt = UserTools.getNewSalt();
        user.setSalt(salt);
        // creation du mot de passe crypte
        String encryptedPassword = UserTools.getEncryptedPassword(user.getPassword(), salt);
        user.setPassword(encryptedPassword);
        UserTools.addUser(user);
        response.setStatus(201);
        return JSONMessage.response("Account created successfully.", false);
      }
    } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      response.setStatus(500);
      return JSONMessage.response("Account creation failed.", true);
    }
  }

  public static JSONObject login(String username, String password, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(username) || !UserTools.checkPassword(username, password)) {
        response.setStatus(401);
        return JSONMessage.response("Username or password is incorrect.", true);
      } else {
        response.setStatus(200);
        return JSONMessage.response("Successfully connected.", false);
      }
    } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      response.setStatus(500);
      return JSONMessage.response("Login failed.", true);
    }
  }

  public static JSONObject deleteUser(String username, String password,
      HttpServletResponse response) throws JSONException {
    try {
      if (!UserTools.usernameExists(username) || !UserTools.checkPassword(username, password)) {
        response.setStatus(401);
        return JSONMessage.response("Username or password is incorrect.", true);
      } else {
        UserTools.removeUser(username);
        response.setStatus(200);
        return JSONMessage.response("Account deleted successfully.", false);
      }
    } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      response.setStatus(500);
      return JSONMessage.response("Account deletion failed.", true);
    }
  }

  public static JSONObject getUsers(HttpServletResponse response) throws JSONException {
    try {
      response.setStatus(200);
      return JSONMessage.response(UserTools.getUsers(), "Users retrieved successfully.");
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve users failed.", true);
    }
  }

  public static JSONObject updateUserProfil(String username, String email, String password,
      String profilBio, String profilImageUrl, HttpServletResponse response) throws JSONException {
    try {
      if (!UserTools.usernameExists(username) || !UserTools.checkPassword(username, password)) {
        response.setStatus(401);
        return JSONMessage.response("Username or password is incorrect.", true);
      } else {
        response.setStatus(200);
        UserTools.updateUserProfil(username, email, password, profilBio, profilImageUrl);
        return JSONMessage.response("User's profil updated successfully.", false);
      }
    } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      response.setStatus(500);
      return JSONMessage.response("User's profil update failed.", true);
    }
  }

  public static JSONObject getUser(String username, HttpServletResponse response)
      throws JSONException {
    try {
      if (!UserTools.usernameExists(username)) {
        response.setStatus(404);
        return JSONMessage.response("User not found.", true);
      } else {
        response.setStatus(200);
        return JSONMessage.response(UserTools.getUser(username), "User retrieved successfully.");
      }
    } catch (SQLException e) {
      response.setStatus(500);
      return JSONMessage.response("Retrieve user failed.", true);
    }
  }

}

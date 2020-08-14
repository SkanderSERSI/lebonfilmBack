package dao;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tables.Comment;
import tables.Like;
import tables.User;

public class UserTools {

  public static boolean usernameExists(String username) throws SQLException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT u FROM Users u WHERE u.username=:username";
      Query<User> query = session.createQuery(hql, User.class);
      query.setParameter("username", username);
      List<User> users = query.list();
      return (users.size() > 0);
    }
  }

  public static boolean emailExists(String email) throws SQLException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT u FROM Users u WHERE u.email=:email";
      Query<User> query = session.createQuery(hql, User.class);
      query.setParameter("email", email);
      List<User> users = query.list();
      return (users.size() > 0);
    }
  }

  public static boolean checkPassword(String username, String password)
      throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT u FROM Users u WHERE u.username=:username";
      Query<User> query = session.createQuery(hql, User.class);
      query.setParameter("username", username);
      User user = query.list().get(0);
      String salt = user.getSalt();
      String calculatedHash = getEncryptedPassword(password, salt);
      if (calculatedHash.equals(user.getPassword())) {
        return true;
      } else {
        return false;
      }
    }
  }

  public static void addUser(User user) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.save(user);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static JSONArray getUsers() throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT u FROM Users u";
      Query<User> query = session.createQuery(hql, User.class);
      List<User> users = query.list();
      JSONArray array = new JSONArray();
      for (User u : users) {
        array.put(u.getUsername());
      }
      return array;
    }
  }

  public static void removeUser(String username) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      String hql = "select u FROM Users u WHERE u.username=:username";
      String hql2 = "select l FROM Likes l WHERE l.username=:username";
      String hql3 = "select c FROM Comments c WHERE c.username=:username";
      Query<User> query = session.createQuery(hql, User.class);
      Query<Like> query2 = session.createQuery(hql2, Like.class);
      Query<Comment> query3 = session.createQuery(hql3, Comment.class);
      query.setParameter("username", username);
      query2.setParameter("username", username);
      query3.setParameter("username", username);
      User user = query.list().get(0);
      session.delete(user);
      List<Like> likes = query2.list();
      List<Comment> comments = query3.list();
      for (Like like : likes) {
        session.delete(like);
      }
      for (Comment comment : comments) {
        session.delete(comment);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static void updateUserProfil(String username, String email, String password,
      String profilBio, String profilImageUrl) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      String hql =
          "UPDATE Users u SET u.email=:email, u.profilBio=:profilBio, u.profilImageUrl=:profilImageUrl where u.username=:username";
      session.createQuery(hql).setParameter("username", username).setParameter("email", email)
          .setParameter("profilBio", profilBio).setParameter("profilImageUrl", profilImageUrl)
          .executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static JSONObject getUser(String username) throws JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT u FROM Users u WHERE u.username=:username";
      Query<User> query = session.createQuery(hql, User.class);
      query.setParameter("username", username);
      User u = query.list().get(0);
      JSONObject user = new JSONObject();
      user.put("username", u.getUsername());
      user.put("firstname", u.getFirstname());
      user.put("lastname", u.getLastname());
      user.put("email", u.getEmail());
      user.put("profil_bio", u.getProfilBio());
      user.put("profil_image_url", u.getProfilImageUrl());
      return user;
    }
  }

  // Get a encrypted password using PBKDF2 hash algorithm
  public static String getEncryptedPassword(String password, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    String algorithm = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 160; // for SHA1
    int iterations = 20000; // NIST specifies 10000

    byte[] saltBytes = Base64.getDecoder().decode(salt);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
    SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

    byte[] encBytes = f.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(encBytes);
  }

  // Returns base64 encoded salt
  public static String getNewSalt() throws NoSuchAlgorithmException {
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

}

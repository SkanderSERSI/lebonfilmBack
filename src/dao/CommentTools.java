package dao;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tables.Comment;
import tables.User;

public class CommentTools {

  public static void addComment(Comment comment) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.save(comment);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static JSONArray getUserComments(String username) throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT c FROM Comments c WHERE c.username=:username";
      Query<Comment> query = session.createQuery(hql, Comment.class);
      query.setParameter("username", username);
      List<Comment> comments = query.list();
      JSONArray array = new JSONArray();
      for (Comment c : comments) {
        JSONObject comment = new JSONObject();
        comment.put("id", c.getId());
        comment.put("username", c.getUsername());
        String hql2 = "SELECT u FROM Users u WHERE u.username=:username";
        Query<User> query2 = session.createQuery(hql2, User.class);
        query2.setParameter("username", c.getUsername());
        User user = query2.list().get(0);
        comment.put("profil_image_url", user.getProfilImageUrl());
        comment.put("tmdb_id", c.getTmdbId());
        comment.put("content", c.getContent());
        comment.put("date", c.getDate());
        array.put(comment);
      }
      return array;
    }
  }

  public static JSONArray getMovieComments(int tmdbId) throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql1 = "SELECT c FROM Comments c WHERE c.tmdbId=:tmdbId";
      Query<Comment> query1 = session.createQuery(hql1, Comment.class);
      query1.setParameter("tmdbId", tmdbId);
      List<Comment> comments = query1.list();
      JSONArray array = new JSONArray();
      for (Comment c : comments) {
        JSONObject comment = new JSONObject();
        comment.put("id", c.getId());
        comment.put("username", c.getUsername());
        String hql2 = "SELECT u FROM Users u WHERE u.username=:username";
        Query<User> query2 = session.createQuery(hql2, User.class);
        query2.setParameter("username", c.getUsername());
        User user = query2.list().get(0);
        comment.put("profil_image_url", user.getProfilImageUrl());
        comment.put("tmdb_id", c.getTmdbId());
        comment.put("content", c.getContent());
        comment.put("date", c.getDate());
        array.put(comment);
      }
      return array;
    }
  }

  public static JSONArray getComments() throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT c FROM Comments c";
      Query<Comment> query = session.createQuery(hql, Comment.class);
      List<Comment> comments = query.list();
      JSONArray array = new JSONArray();
      for (Comment c : comments) {
        JSONObject comment = new JSONObject();
        comment.put("id", c.getId());
        comment.put("username", c.getUsername());
        String hql2 = "SELECT u FROM Users u WHERE u.username=:username";
        Query<User> query2 = session.createQuery(hql2, User.class);
        query2.setParameter("username", c.getUsername());
        User user = query2.list().get(0);
        comment.put("profil_image_url", user.getProfilImageUrl());
        comment.put("tmdb_id", c.getTmdbId());
        comment.put("content", c.getContent());
        comment.put("date", c.getDate());
        array.put(comment);
      }
      return array;
    }
  }

}

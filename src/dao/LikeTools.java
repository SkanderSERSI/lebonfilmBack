package dao;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tables.Like;
import tables.Movie;

public class LikeTools {

  public static boolean likeExists(Like like) throws SQLException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction transaction = session.beginTransaction();
      String hql = "SELECT l FROM Likes l WHERE l.tmdbId=:tmdbId AND l.username=:username";
      Query<Like> query = session.createQuery(hql, Like.class);
      query.setParameter("tmdbId", like.getTmdbId());
      query.setParameter("username", like.getUsername());
      List<Like> likes = query.list();
      transaction.commit();
      return (likes.size() > 0);
    }
  }

  public static void addLike(Like like) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.save(like);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static void removeLike(String username, int tmdbId) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      String hql = "select l FROM Likes l WHERE l.username=:username AND l.tmdbId=:tmdbId";
      Query<Like> query = session.createQuery(hql, Like.class);
      query.setParameter("username", username);
      query.setParameter("tmdbId", tmdbId);
      List<Like> likes = query.list();
      if (!likes.isEmpty()) {
        Like like = likes.get(0);
        session.delete(like);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }


  public static JSONArray getLikedMovies(String username) throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql1 = "SELECT l.tmdbId FROM Likes l WHERE l.username=:username";

      Query<Integer> query1 = session.createQuery(hql1, Integer.class);
      query1.setParameter("username", username);
      List<Integer> movies = query1.list();
      JSONArray array = new JSONArray();
      for (int i : movies) {
        String hql2 = "SELECT m FROM Movies m WHERE m.id=:id";
        Query<Movie> query2 = session.createQuery(hql2, Movie.class);
        query2.setParameter("id", i);
        Movie movie = query2.list().get(0);
        JSONObject movieJSON = new JSONObject();
        movieJSON.put("id", movie.getId());
        movieJSON.put("title", movie.getTitle());
        movieJSON.put("poster_url", movie.getPosterUrl());
        array.put(movieJSON);
      }
      return array;
    }
  }

  public static JSONArray getMovieLikes(int tmdbId) throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT l.username FROM Likes l WHERE l.tmdbId=:tmdbId";
      Query<String> query = session.createQuery(hql, String.class);
      query.setParameter("tmdbId", tmdbId);
      List<String> users = query.list();
      JSONArray array = new JSONArray();
      for (String user : users) {
        array.put(user);
      }
      return array;
    }
  }

  public static JSONArray getLikes() throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT l FROM Likes l";
      Query<Like> query = session.createQuery(hql, Like.class);
      List<Like> likes = query.list();
      JSONArray array = new JSONArray();
      for (Like l : likes) {
        JSONObject like = new JSONObject();
        like.put("id", l.getId());
        like.put("tmdb_id", l.getTmdbId());
        like.put("username", l.getUsername());
        array.put(like);
      }
      return array;
    }
  }

}

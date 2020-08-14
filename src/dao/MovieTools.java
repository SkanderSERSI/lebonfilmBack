package dao;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tables.Movie;

public class MovieTools {

  public static boolean movieExists(int id) throws SQLException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction transaction = session.beginTransaction();
      String hql = "SELECT m FROM Movies m WHERE m.id=:id";
      Query<Movie> query = session.createQuery(hql, Movie.class);
      query.setParameter("id", id);
      List<Movie> movies = query.list();
      transaction.commit();
      return (movies.size() > 0);
    }
  }

  public static void addMovie(Movie movie) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.save(movie);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static void updateTrendingMovie(int id) throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      String hql = "UPDATE Movies m SET m.trending =:trending where m.id=:id";
      session.createQuery(hql).setParameter("trending", true).setParameter("id", id)
          .executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static void clearTrendingMovie() throws SQLException {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      String hql = "UPDATE Movies SET trending=:trending";
      session.createQuery(hql).setParameter("trending", false).executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  public static JSONObject getMovie(int id) throws JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT m FROM Movies m WHERE m.id=:id";
      Query<Movie> query = session.createQuery(hql, Movie.class);
      query.setParameter("id", id);
      Movie m = query.list().get(0);
      JSONObject movie = new JSONObject();
      movie.put("id", m.getId());
      movie.put("title", m.getTitle());
      movie.put("vote_average", m.getVoteAverage());
      movie.put("release_date", m.getReleaseDate());
      movie.put("category", m.getCategory());
      movie.put("backdrop_url", m.getBackdropUrl());
      movie.put("overview", m.getOverview());
      movie.put("poster_url", m.getPosterUrl());
      movie.put("trailer_url", m.getTrailerUrl());
      movie.put("homepage_url", m.getHomepageUrl());
      movie.put("status", m.getStatus());
      movie.put("runtime", m.getRuntime());
      movie.put("keywords", m.getKeywords());
      movie.put("recommendations", m.getRecommendations());
      return movie;
    }
  }

  public static JSONArray getMovies() throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT m FROM Movies m";
      Query<Movie> query = session.createQuery(hql, Movie.class);
      List<Movie> movies = query.list();
      JSONArray array = new JSONArray();
      for (Movie m : movies) {
        JSONObject movie = new JSONObject();
        movie.put("id", m.getId());
        movie.put("title", m.getTitle());
        movie.put("vote_average", m.getVoteAverage());
        movie.put("release_date", m.getReleaseDate());
        movie.put("category", m.getCategory());
        movie.put("backdrop_url", m.getBackdropUrl());
        movie.put("overview", m.getOverview());
        movie.put("poster_url", m.getPosterUrl());
        movie.put("trailer_url", m.getTrailerUrl());
        movie.put("homepage_url", m.getHomepageUrl());
        movie.put("status", m.getStatus());
        movie.put("runtime", m.getRuntime());
        movie.put("keywords", m.getKeywords());
        movie.put("recommendations", m.getRecommendations());
        array.put(movie);
      }
      return array;
    }
  }

  public static JSONArray getTrendingMovies() throws SQLException, JSONException {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      String hql = "SELECT m FROM Movies m WHERE m.trending=:trending";
      Query<Movie> query = session.createQuery(hql, Movie.class);
      query.setParameter("trending", true);
      List<Movie> movies = query.list();
      JSONArray array = new JSONArray();
      for (Movie m : movies) {
        JSONObject movie = new JSONObject();
        movie.put("id", m.getId());
        movie.put("title", m.getTitle());
        movie.put("vote_average", m.getVoteAverage());
        movie.put("release_date", m.getReleaseDate());
        movie.put("category", m.getCategory());
        movie.put("backdrop_url", m.getBackdropUrl());
        movie.put("overview", m.getOverview());
        movie.put("poster_url", m.getPosterUrl());
        movie.put("trailer_url", m.getTrailerUrl());
        movie.put("homepage_url", m.getHomepageUrl());
        movie.put("status", m.getStatus());
        movie.put("runtime", m.getRuntime());
        movie.put("keywords", m.getKeywords());
        movie.put("recommendations", m.getRecommendations());
        array.put(movie);
      }
      return array;
    }
  }

}

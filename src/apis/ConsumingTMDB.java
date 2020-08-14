package apis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import dao.MovieTools;
import tables.Movie;

public class ConsumingTMDB {
  private final static String GET_TRENDING_URL =
      "https://api.themoviedb.org/3/trending/movie/day?language=en-US&";
  private final static String GET_MOVIE_GENRE_LIST =
      "https://api.themoviedb.org/3/genre/movie/list?language=en-US&";
  private final static String GET_MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
  private final static String TMDB_API_KEY = "api_key=210760ec7578aa3083f6948ea0bce9a6";

  ResteasyClient client;
  private Map<Integer, String> mapCategory;

  public ConsumingTMDB() {
    client = new ResteasyClientBuilder().connectionPoolSize(200).build();
    mapCategory = new HashMap<>();
    Response genresResponse = client.target(GET_MOVIE_GENRE_LIST + TMDB_API_KEY).request().get();
    switch (genresResponse.getStatus()) {
      case 200:
        try {
          JSONObject genres = new JSONObject(genresResponse.readEntity(String.class));
          JSONArray arr = genres.getJSONArray("genres");
          for (int i = 0; i < arr.length(); i++) {
            JSONObject cat = arr.getJSONObject(i);
            mapCategory.put(cat.getInt("id"), cat.getString("name"));
          }
        } catch (JSONException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        break;
      default:
        System.out.println("Failed with HTTP error code : " + genresResponse.getStatus());
    }
  }

  public String fromMinutesToHHmm(int minutes) {
    long hours = TimeUnit.MINUTES.toHours(Long.valueOf(minutes));
    long remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours);
    return String.format("%02dH %02dM", hours, remainMinutes);
  }

  public void getTrending() throws JSONException, SQLException {
    MovieTools.clearTrendingMovie();
    Response trendingResponse = client.target(GET_TRENDING_URL + TMDB_API_KEY).request().get();
    switch (trendingResponse.getStatus()) {
      case 200:
        JSONObject obj = new JSONObject(trendingResponse.readEntity(String.class));
        JSONArray arr = obj.getJSONArray("results");
        for (int i = 0; i < arr.length(); i++) {
          JSONObject movie = arr.getJSONObject(i);
          if (MovieTools.movieExists(movie.getInt("id"))) {
            MovieTools.updateTrendingMovie(movie.getInt("id"));
          } else {
            createMovie(movie.getInt("id"), true);
          }
        }
        client.close();
        break;
      default:
        System.out.println("Failed with HTTP error code : " + trendingResponse.getStatus());
    }
  }

  public void createMovie(int id, boolean trending)
      throws IllegalArgumentException, NullPointerException, JSONException, SQLException {
    Response recommendationsResponse = client
        .target(
            GET_MOVIE_BASE_URL + id + "/recommendations?" + TMDB_API_KEY + "&language=en-US&page=1")
        .request().get();
    Response keywordsResponse =
        client.target(GET_MOVIE_BASE_URL + id + "/keywords?" + TMDB_API_KEY).request().get();
    Response trailerUrlResponse =
        client.target(GET_MOVIE_BASE_URL + id + "/videos?" + TMDB_API_KEY + "&language=en-US")
            .request().get();
    Response movieDetailsResponse = client
        .target(GET_MOVIE_BASE_URL + id + "?" + TMDB_API_KEY + "&language=en-US").request().get();

    String recommendations = "";
    String keywords = "";
    String trailerUrl = "";

    switch (recommendationsResponse.getStatus()) {
      case 200:
        JSONArray recommendationsList =
            new JSONObject(recommendationsResponse.readEntity(String.class))
                .getJSONArray("results");
        for (int j = 0; j < recommendationsList.length(); j++)
          recommendations +=
              String.valueOf(recommendationsList.getJSONObject(j).getInt("id") + " ");
        recommendations = recommendations.trim();
        break;
      default:
        System.out.println("Failed with HTTP error code : " + recommendationsResponse.getStatus());
    }
    switch (keywordsResponse.getStatus()) {
      case 200:
        JSONArray keywordsList =
            new JSONObject(keywordsResponse.readEntity(String.class)).getJSONArray("keywords");
        for (int j = 0; j < keywordsList.length(); j++)
          keywords += (keywordsList.getJSONObject(j).getString("name") + " ");
        keywords = keywords.trim();
        break;
      default:
        System.out.println("Failed with HTTP error code : " + keywordsResponse.getStatus());
    }
    switch (trailerUrlResponse.getStatus()) {
      case 200:
        JSONArray videos =
            new JSONObject(trailerUrlResponse.readEntity(String.class)).getJSONArray("results");
        if (videos.length() > 0) {
          for (int j = 0; j < videos.length(); j++) {
            if (videos.getJSONObject(j).getString("type").equals("Trailer")) {
              trailerUrl =
                  "https://www.youtube.com/embed/" + videos.getJSONObject(j).getString("key");
              break;
            }
          }
        }
        break;
      default:
        System.out.println("Failed with HTTP error code : " + trailerUrlResponse.getStatus());
    }
    switch (movieDetailsResponse.getStatus()) {
      case 200:
        JSONObject movieDetails = new JSONObject(movieDetailsResponse.readEntity(String.class));
        JSONArray cat = movieDetails.getJSONArray("genres");
        String category = "";
        for (int j = 0; j < cat.length(); j++)
          category += (mapCategory.get(cat.getJSONObject(j).getInt("id")) + " ");
        category = category.trim();
        Movie m = new Movie(movieDetails.getInt("id"), movieDetails.getString("title"),
            movieDetails.getDouble("vote_average"), movieDetails.getString("release_date"),
            category,
            ("https://image.tmdb.org/t/p/original" + movieDetails.getString("backdrop_path")),
            movieDetails.getString("overview"),
            ("https://image.tmdb.org/t/p/w780" + movieDetails.getString("poster_path")), trailerUrl,
            trending, movieDetails.getString("homepage"), movieDetails.getString("status"),
            fromMinutesToHHmm(movieDetails.getInt("runtime")), keywords, recommendations);
        MovieTools.addMovie(m);
        break;
      default:
        System.out.println("Failed with HTTP error code : " + movieDetailsResponse.getStatus());
    }
  }

}

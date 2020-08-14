package services.crontask;

import java.sql.SQLException;
import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import apis.ConsumingTMDB;

/**
 * Recupere les films tendance en faisant appel a l'api TMDB.
 */
public class TMDBJob implements Job {

  ConsumingTMDB tmdb = new ConsumingTMDB();

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try {
      System.out.println("-------------------------- TMDB --------------------------");
      tmdb.getTrending();
      System.out.println("----------------------------------------------------------");
    } catch (JSONException | SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

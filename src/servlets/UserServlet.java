package servlets;

import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import services.UserService;
import services.crontask.TMDBJob;
import tables.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static int cpt = 0; // Compteur pour declencher le cron task

  // Creation d’un compte
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject retour = new JSONObject();
    try {
      JSONObject temp = new JSONObject(json);
      String username = temp.getString("username");
      String password = temp.getString("password");
      String lastname = temp.getString("lastname");
      String firstname = temp.getString("firstname");
      String email = temp.getString("email");
      User user = new User(username, lastname, firstname, email, password);
      // On demarre le cron task lors de la premiere inscription
      if (cpt == 0) {
        // Job qui va faire la requete a l'api TMDB pour recuperer les films tendance.
        JobDetail job = JobBuilder.newJob(TMDBJob.class).withIdentity("myJob", "group1").build();

        // On declenche notre job immediatement et le job va se repeter indefiniment tout les 24h
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1").startNow()
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever())
            .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
      }
      cpt++;
      retour = UserService.createUser(user, response);
    } catch (JSONException | SchedulerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  // Suppression d’un compte
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject retour = new JSONObject();
    try {
      JSONObject temp = new JSONObject(json);
      String username = temp.getString("username");
      String password = temp.getString("password");
      retour = UserService.deleteUser(username, password, response);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    JSONObject retour = new JSONObject();
    try {
      String username = request.getParameter("username");
      if (username != null) {
        // Recuperer les informations de l'utilisateur specifie en parametre
        retour = UserService.getUser(username, response);
      } else {
        // Recuperer la liste de tous les utilisateurs inscrits
        retour = UserService.getUsers(response);
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    setAccessControlHeaders(response);
    String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject retour = new JSONObject();
    try {
      JSONObject temp = new JSONObject(json);
      String username = temp.getString("username");
      String password = temp.getString("password");
      String email = temp.getString("email");
      String profilBio = temp.getString("profil_bio");
      String profilImageUrl = temp.getString("profil_image_url");
      // Mettre à jour l’email, la bio et l’image de profil de l’utilisateur
      retour = UserService.updateUserProfil(username, email, password, profilBio, profilImageUrl,
          response);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    response.setContentType("application/json");
    response.getWriter().print(retour);
  }

  // for Preflight
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    setAccessControlHeaders(resp);
    resp.setStatus(HttpServletResponse.SC_OK);
  }

  private void setAccessControlHeaders(HttpServletResponse resp) {
    resp.addHeader("Access-Control-Allow-Origin", "https://lebonfilm.herokuapp.com");
    resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
    resp.setHeader("Access-Control-Allow-Headers",
        "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
  }

}

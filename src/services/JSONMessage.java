package services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe utilitaire pour envoyer des reponses JSON
 */
public class JSONMessage {

  public static JSONObject response(String message, boolean isError) throws JSONException {
    JSONObject objM = new JSONObject();
    if (!isError)
      objM.put("message", message);
    else
      objM.put("error", message);
    return objM;
  }

  public static JSONObject response(JSONObject obj, String message) throws JSONException {
    JSONObject objM = new JSONObject();
    objM.put("message", message);
    objM.put("result", obj);
    return objM;
  }

  public static JSONObject response(JSONArray array, String message) throws JSONException {
    JSONObject objM = new JSONObject();
    objM.put("message", message);
    objM.put("results", array);
    return objM;
  }

}

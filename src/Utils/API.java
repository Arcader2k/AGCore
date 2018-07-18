package Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class API
{
  public static UUID getUUID(String player)
  {
    try
    {
      URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player);
      HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
      String inputLine;
      if ((inputLine = in.readLine()) != null) 
      {
        JsonObject json = new JsonParser().parse(inputLine).getAsJsonObject();
        String raw = json.get("id").toString().replace("\"", "");
        raw = new StringBuilder(raw).insert(8, "-").toString();
        raw = new StringBuilder(raw).insert(13, "-").toString();
        raw = new StringBuilder(raw).insert(18, "-").toString();
        raw = new StringBuilder(raw).insert(23, "-").toString();
        UUID uuid = UUID.fromString(raw);
        return uuid;
      }

      in.close();
    } catch (Exception e) 
    {
      return null;
    }
    @SuppressWarnings("unused")
	URL url;
    return null;
  }

  public static JsonObject getLocation(String host) 
  {
    try 
    {
      URL url = new URL("http://ip-api.com/json/" + host);
      BufferedReader in = new BufferedReader(
        new InputStreamReader(url.openStream()));
      String inputLine;
      if ((inputLine = in.readLine()) != null) 
      {
        JsonObject json = new JsonParser().parse(inputLine).getAsJsonObject();
        return json;
      }

      in.close();
    } catch (Exception e) 
    {
      return null;
    }
    @SuppressWarnings("unused")
	URL url;
    return null;
  }

  public static JsonArray getPreviousNames(UUID uuid) 
  {
    try 
    {
      URL url = new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "") + "/names");
      HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      if ((inputLine = in.readLine()) != null) 
      {
        JsonArray json = new JsonParser().parse(inputLine).getAsJsonArray();
        return json;
      }

      in.close();
    } catch (Exception e) 
    {
      return null;
    }
    @SuppressWarnings("unused")
	URL url;
    return null;
  }
  @SuppressWarnings({ "rawtypes", "unchecked" })
public static List<String> getChangedNamesAtTime(JsonArray json) 
{
    List list = new ArrayList();
    for (JsonElement jo : json) 
    {
      JsonObject job = jo.getAsJsonObject();
      if (job.has("changedToAt")) 
      {
        Date date = new Date(job.get("changedToAt").getAsLong());
        String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
        list.add(job.get("name").getAsString() + " changed at " + newstring);
      } else 
      {
        list.add(job.get("name").getAsString());
      }
    }
    return list;
  }
}
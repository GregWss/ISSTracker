package uqac.ca.isstracker.model;

import android.util.Log;
import java.util.ArrayList;
import org.json.*;

/**
 * This class holds gathered information about astronauts from open-notify API.
 */
public class ISSAstros
{
    private ArrayList<Astro> astros;
    private int number;
    private String message;

    public ISSAstros(JSONObject obj)
    {
        astros = new ArrayList<>();
        try
        {
            JSONArray astrosArray = obj.getJSONArray("people");
            for (int i = 0; i < astrosArray.length(); i++)
                astros.add(
                        new Astro(
                                astrosArray.getJSONObject(i).getString("name"),
                                astrosArray.getJSONObject(i).getString("craft"))
                );
            number = obj.getInt("number");
            message = obj.getString("message");
        }
        catch (Exception e)
        {
            Log.e("API-ASTROS", e.getMessage());
        }
    }

    /**
     * Returns a list of ISSAstro objects, representing the astronauts that currently living in
     * space.
     * @return a list of ISSAstro objects.
     */
    public ArrayList<Astro> getAstros()
    {
        return astros;
    }

    /**
     * Returns the number of astronauts inside ISS.
     * @return the number of astronauts inside ISS.
     */
    public int getNumber()
    {
        return number;
    }

    /**
     * Returns the API status message.
     * @return the API status message.
     */
    public String getMessage()
    {
        return message;
    }
}

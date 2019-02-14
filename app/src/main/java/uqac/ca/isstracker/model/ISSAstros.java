package uqac.ca.isstracker.model;

import android.util.Log;
import java.util.ArrayList;
import org.json.*;

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

    public ArrayList<Astro> getAstros()
    {
        return astros;
    }

    public int getNumber()
    {
        return number;
    }

    public String getMessage()
    {
        return message;
    }
}

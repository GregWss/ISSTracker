package uqac.ca.isstracker.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class N2yoVisualPasses
{
    private int satId;
    private String satname;
    private int passescount;
    private ArrayList<N2yoPass> passes;

    public N2yoVisualPasses(JSONObject obj)
    {
        passes = new ArrayList<>();
        try
        {
            JSONObject info = obj.getJSONObject("info");
            satId = info.getInt("satid");
            satname = info.getString("satname");
            passescount = info.getInt("passescount");

            JSONArray passesArray = obj.getJSONArray("passes");
            if(passesArray.length() != 0)
                for (int i = 0; i < passesArray.length(); i++)
                    passes.add(new N2yoPass(passesArray.getJSONObject(i)));
        }
        catch (Exception e)
        {
            Log.e("API-N2YO-VISUAL-PASSES", e.getMessage());
        }
    }

    /**
     * As long the ISS is a satellite, it returns his official satellite registered viewName.
     * @return ISS satellite official viewName.
     */
    public String getSatname()
    {
        return satname;
    }

    /**
     * Returns the ISS NORAD id.
     * @return NORAD id.
     */
    public int getSatId()
    {
        return satId;
    }

    /**
     * Count of passes
     * @return count of passes
     */
    public int getPassescount()
    {
        return passescount;
    }

    /**
     *
     * @return
     */
    public ArrayList<N2yoPass> getPasses()
    {
        return passes;
    }
}

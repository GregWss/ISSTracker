package uqac.ca.isstracker.model;

import android.util.Log;
import java.util.ArrayList;
import org.json.*;

/**
 * This class holds gathered information about ISS from N2yo website & API.
 */
public class N2yo
{
    private String satname;
    private int satId;
    private ArrayList<ISSPosition> positions;

    public N2yo(JSONObject obj)
    {
        positions = new ArrayList<>();
        try
        {
            JSONObject info = obj.getJSONObject("info");
            satname = info.getString("satname");
            satId = info.getInt("satid");
            JSONArray positionsArray = obj.getJSONArray("positions");
            for (int i = 0; i < positionsArray.length(); i++)
                positions.add(
                        new ISSPosition(
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("satlatitude")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("satlongitude")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("sataltitude")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("azimuth")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("elevation")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("ra")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("dec")),
                                Float.parseFloat(positionsArray.getJSONObject(i).getString("timestamp"))
                        )
                );
        }
        catch (Exception e)
        {
            Log.e("API-N2YO", e.getMessage());
        }
    }

    /**
     * As long the ISS is a satellite, it returns his official satellite registered name.
     * @return ISS satellite official name.
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
     * Returns future positions of the ISS as footprints (latitude, longitude). Also return the
     * satellite's azimuth and elevation with respect to the observer location.
     * @return future positions.
     */
    public ArrayList<ISSPosition> getPositions()
    {
        return positions;
    }
}

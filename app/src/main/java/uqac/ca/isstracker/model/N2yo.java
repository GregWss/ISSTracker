package uqac.ca.isstracker.model;

import android.util.Log;
import java.util.ArrayList;
import org.json.*;

public class N2yo
{
    private String satname;
    private int satId;
    private ArrayList<ISSPosition> positions;

    public N2yo(String requestResponse)
    {
        positions = new ArrayList<>();
        try
        {
            JSONObject obj = new JSONObject(requestResponse);
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

    public String getSatname() {
        return satname;
    }

    public int getSatId() {
        return satId;
    }

    public ArrayList<ISSPosition> getPositions() {
        return positions;
    }
}

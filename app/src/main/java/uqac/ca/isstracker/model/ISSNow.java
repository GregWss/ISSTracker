package uqac.ca.isstracker.model;

import android.util.Log;
import org.json.*;

public class ISSNow
{
    private int timeStamp;
    private float issLongitude;
    private float issLatitude;
    private String message;

    public ISSNow(String requestResponse)
    {
        try
        {
            JSONObject obj = new JSONObject(requestResponse);
            timeStamp = obj.getInt("timestamp");
            JSONObject position = obj.getJSONObject("iss_position");
            issLongitude = Float.parseFloat(position.getString("longitude"));
            issLatitude = Float.parseFloat(position.getString("latitude"));
            message = obj.getString("message");
        }
        catch (Exception e)
        {
            Log.e("API-ISSNOW", e.getMessage());
        }
    }

    public int getTimeStamp()
    {
        return timeStamp;
    }

    public float getIssLongitude()
    {
        return issLongitude;
    }

    public float getIssLatitude()
    {
        return issLatitude;
    }

    public String getMessage()
    {
        return message;
    }
}

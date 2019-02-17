package uqac.ca.isstracker.model;

import android.util.Log;
import org.json.*;

/**
 * This class holds gathered global information about ISS from open-notify API.
 */
public class ISSNow
{
    private int timeStamp;
    private float issLongitude;
    private float issLatitude;
    private String message;

    public ISSNow(JSONObject obj)
    {
        try
        {
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

    /**
     * Unix time for this position (seconds).
     * @return Unix time for this position (seconds).
     */
    public int getTimeStamp()
    {
        return timeStamp;
    }

    /**
     * Returns ISS Longitude position from last request.
     * @return last ISS Longitude pos from last API request.
     */
    public float getIssLongitude()
    {
        return issLongitude;
    }

    /**
     * Returns ISS Latitude position from last request.
     * @return last ISS Latitude pos from last API request.
     */
    public float getIssLatitude()
    {
        return issLatitude;
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

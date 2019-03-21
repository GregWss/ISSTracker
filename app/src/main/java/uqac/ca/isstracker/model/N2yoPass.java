package uqac.ca.isstracker.model;

import android.util.Log;

import org.json.JSONObject;

public class N2yoPass
{
    private float startAZ;
    private String startAZCompass;
    private float startEl;
    private int startUTC;
    private float maxAz;
    private String maxAzCompass;
    private float maxEl;
    private int maxUTC;
    private float endAz;
    private String endAzCompass;
    private float endEl;
    private int endUTC;
    private float mag;
    private int duration;

    public N2yoPass(JSONObject jsonObject)
    {
        try
        {
            this.startAZ        = Float.parseFloat(jsonObject.getString("startAz"));
            this.startAZCompass = jsonObject.getString("startAzCompass");
            this.startEl        = Float.parseFloat(jsonObject.getString("startEl"));
            this.startUTC       = jsonObject.getInt("startUTC");
            this.maxAz          = Float.parseFloat(jsonObject.getString("maxAz"));
            this.maxAzCompass   = jsonObject.getString("maxAzCompass");
            this.maxEl          = Float.parseFloat(jsonObject.getString("maxEl"));
            this.maxUTC         = jsonObject.getInt("maxUTC");
            this.endAz          = Float.parseFloat(jsonObject.getString("endAz"));
            this.endAzCompass   = jsonObject.getString("endAzCompass");
            this.endEl          = Float.parseFloat(jsonObject.getString("endEl"));
            this.endUTC         = jsonObject.getInt("endUTC");
            this.mag            = Float.parseFloat(jsonObject.getString("mag"));
            this.duration       = jsonObject.getInt("duration");
        }
        catch (Exception e)
        {
            Log.e("API-N2YO-PASS", e.getMessage());
        }
    }

    public N2yoPass(float startAZ, String startAZCompass, float startEl, int startUTC, float maxAz, String maxAzCompass, float maxEl, int maxUTC, float endAz, String endAzCompass, float endEl, int endUTC, float mag, int duration)
    {
        this.startAZ        = startAZ;
        this.startAZCompass = startAZCompass;
        this.startEl        = startEl;
        this.startUTC       = startUTC;
        this.maxAz          = maxAz;
        this.maxAzCompass   = maxAzCompass;
        this.maxEl          = maxEl;
        this.maxUTC         = maxUTC;
        this.endAz          = endAz;
        this.endAzCompass   = endAzCompass;
        this.endEl          = endEl;
        this.endUTC         = endUTC;
        this.mag            = mag;
        this.duration       = duration;
    }

    /**
     * @return satellite azimuth for the start of this pass (relative to the observer, in degrees)
     */
    public float getStartAZ() {
        return startAZ;
    }

    /**
     * @return satellite azimuth for the start of this pass (relative to the observer). Possible values: N, NE, E, SE, S, SW, W, NW
     */
    public String getStartAZCompass() {
        return startAZCompass;
    }

    /**
     * @return satellite elevation for the start of this pass (relative to the observer, in degrees)
     */
    public float getStartEl() {
        return startEl;
    }

    /**
     * @return Unix time for the start of this pass. You should convert this UTC value to observer's time zone
     */
    public int getStartUTC() {
        return startUTC;
    }

    /**
     * @return satellite azimuth for the max elevation of this pass (relative to the observer, in degrees)
     */
    public float getMaxAz() {
        return maxAz;
    }

    /**
     * @return satellite azimuth for the max elevation of this pass (relative to the observer). Possible values: N, NE, E, SE, S, SW, W, NW
     */
    public String getMaxAzCompass() {
        return maxAzCompass;
    }

    /**
     * @return satellite max elevation for this pass (relative to the observer, in degrees)
     */
    public float getMaxEl() {
        return maxEl;
    }

    /**
     * @return Unix time for the max elevation of this pass. You should convert this UTC value to observer's time zone
     */
    public int getMaxUTC() {
        return maxUTC;
    }

    /**
     * @return satellite azimuth for the end of this pass (relative to the observer, in degrees)
     */
    public float getEndAz() {
        return endAz;
    }

    /**
     * @return satellite azimuth for the end of this pass (relative to the observer). Possible values: N, NE, E, SE, S, SW, W, NW
     */
    public String getEndAzCompass() {
        return endAzCompass;
    }

    /**
     * @return satellite elevation for the end of this pass (relative to the observer, in degrees)
     */
    public float getEndEl() {
        return endEl;
    }

    /**
     * @return Unix time for the end of this pass. You should convert this UTC value to observer's time zone
     */
    public int getEndUTC() {
        return endUTC;
    }

    /**
     * @return max visual magnitude of the pass, same scale as star brightness. If magnitude cannot be determined, the value is 100000
     */
    public float getMag() {
        return mag;
    }

    /**
     * @return twotal visible duration of this pass (in seconds)
     */
    public int getDuration() {
        return duration;
    }

    @Override
    public String toString()
    {
        return "N2yoPass{" +
                "startAZ=" + startAZ +
                ", startAZCompass='" + startAZCompass + '\'' +
                ", startEl=" + startEl +
                ", startUTC=" + startUTC +
                ", maxAz=" + maxAz +
                ", maxAzCompass='" + maxAzCompass + '\'' +
                ", maxEl=" + maxEl +
                ", maxUTC=" + maxUTC +
                ", endAz=" + endAz +
                ", endAzCompass='" + endAzCompass + '\'' +
                ", endEl=" + endEl +
                ", endUTC=" + endUTC +
                ", mag=" + mag +
                ", duration=" + duration +
                '}';
    }
}

package uqac.ca.isstracker.model;

public class ISSPosition
{
    private float satlatitude;
    private float satlongitude;
    private float sataltitude;
    private float azimuth;
    private float elevation;
    private float ra;
    private float dec;
    private float timestamp;

    public ISSPosition(float satlatitude, float satlongitude, float sataltitude, float azimuth, float elevation, float ra, float dec, float timestamp)
    {
        this.satlatitude = satlatitude;
        this.satlongitude = satlongitude;
        this.sataltitude = sataltitude;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.ra = ra;
        this.dec = dec;
        this.timestamp = timestamp;
    }

    public float getSatlatitude()
    {
        return satlatitude;
    }

    public float getSatlongitude()
    {
        return satlongitude;
    }

    public float getSataltitude()
    {
        return sataltitude;
    }

    public float getAzimuth()
    {
        return azimuth;
    }

    public float getElevation()
    {
        return elevation;
    }

    public float getRa()
    {
        return ra;
    }

    public float getDec()
    {
        return dec;
    }

    public float getTimestamp()
    {
        return timestamp;
    }
}

package uqac.ca.isstracker.model;

/**
 * This class holds a footprint of the ISS with his azimuth and elevation with respect to the
 * observer location.
 */
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

    /**
     * Returns the ISS footprint latitude (decimal degrees format) at this position.
     * @return footprint latitude (decimal degrees).
     */
    public float getSatlatitude()
    {
        return satlatitude;
    }

    /**
     * Returns the ISS footprint longitude (decimal degrees format) at this position.
     * @return footprint longitude (decimal degrees).
     */
    public float getSatlongitude()
    {
        return satlongitude;
    }

    /**
     * Returns the ISS footprint altitude above sea level at this position in kilometers.
     * @return footprint altitude (kilometers).
     */
    public float getSataltitude()
    {
        return sataltitude;
    }

    /**
     * Returns the ISS azimuth with respect to observer's location (degrees).
     * @return ISS azimuth.
     */
    public float getAzimuth()
    {
        return azimuth;
    }

    /**
     * Returns the ISS elevation with respect to observer's location (degrees).
     * @return ISS elevation.
     */
    public float getElevation()
    {
        return elevation;
    }

    /**
     * Returns the ISS right angle (degrees) at this position.
     * @return ISS right angle.
     */
    public float getRa()
    {
        return ra;
    }

    /**
     * Returns the ISS declination (degrees).
     * @return ISS declination.
     */
    public float getDec()
    {
        return dec;
    }

    /**
     * Unix time for this position (seconds).
     * @return Unix time for this position (seconds).
     */
    public float getTimestamp()
    {
        return timestamp;
    }
}


package uqac.ca.isstracker;

import java.util.HashMap;
import java.util.Map;

public class IssPosition {

    private String longitude;
    private String latitude;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public IssPosition() {
    }

    /**
     * 
     * @param longitude
     * @param latitude
     */
    public IssPosition(String longitude, String latitude) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


package uqac.ca.isstracker.model;

import java.util.HashMap;
import java.util.Map;

public class ISSInfoSource
{
    private ISSPosition issPosition;
    private Integer timestamp;
    private String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ISSInfoSource() { }

    /**
     * 
     * @param message
     * @param timestamp
     * @param issPosition
     */
    public ISSInfoSource(ISSPosition issPosition, Integer timestamp, String message)
    {
        super();
        this.issPosition = issPosition;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ISSPosition getIssPosition() {
        return issPosition;
    }

    public void setIssPosition(ISSPosition issPosition) {
        this.issPosition = issPosition;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

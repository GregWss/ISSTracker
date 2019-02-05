
package uqac.ca.isstracker;

import java.util.HashMap;
import java.util.Map;

public class ISSInfoSource {

    private IssPosition issPosition;
    private Integer timestamp;
    private String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ISSInfoSource() {
    }

    /**
     * 
     * @param message
     * @param timestamp
     * @param issPosition
     */
    public ISSInfoSource(IssPosition issPosition, Integer timestamp, String message) {
        super();
        this.issPosition = issPosition;
        this.timestamp = timestamp;
        this.message = message;
    }

    public IssPosition getIssPosition() {
        return issPosition;
    }

    public void setIssPosition(IssPosition issPosition) {
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

package its.my.time.data.ws.events;
import java.util.HashMap;
import java.util.Map;

public class PostEventReturn {

private Integer idEvent;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public Integer getIdEvent() {
return idEvent;
}

public void setIdEvent(Integer idEvent) {
this.idEvent = idEvent;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperties(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
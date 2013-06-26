
package its.my.time.data.ws.events.plugins.pj;

import java.util.HashMap;
import java.util.Map;

public class PjBeanWS {

    private Integer id;
    private String name;
    private String file_base64;
    private String mime;
    private String extension;
    private String date_crea;
    private Event event;
    private User user;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile_base64() {
        return file_base64;
    }

    public void setFile_base64(String file_base64) {
        this.file_base64 = file_base64;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDate_crea() {
        return date_crea;
    }

    public void setDate_crea(String date_crea) {
        this.date_crea = date_crea;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

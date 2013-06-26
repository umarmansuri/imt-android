
package its.my.time.data.ws.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class UtilisateurBeanWS {

    private Integer id;
    private String username;
    private String email;
    private List<Event> events = new ArrayList<Event>();
    private List<Account> accounts = new ArrayList<Account>();
    private List<Profil> profils = new ArrayList<Profil>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Profil> getProfils() {
        return profils;
    }

    public void setProfils(List<Profil> profils) {
        this.profils = profils;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


package its.my.time.data.ws.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EventBeanWS {

    private Integer id;
    private String type;
    private String title;
    private String content;
    private String date;
    private String date_fin;
    private String crea_date;
    private String modif_date;
    private Boolean all_day;
    private List<Owner> owners = new ArrayList<Owner>();
    private List<Account> accounts = new ArrayList<Account>();
    private List<Participant> participants = new ArrayList<Participant>();
    private List<Attachmants> attachments = new ArrayList<Attachmants>();
    private Integer importance;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getCrea_date() {
        return crea_date;
    }

    public void setCrea_date(String crea_date) {
        this.crea_date = crea_date;
    }

    public String getModif_date() {
        return modif_date;
    }

    public void setModif_date(String modif_date) {
        this.modif_date = modif_date;
    }

    public Boolean getAll_day() {
        return all_day;
    }

    public void setAll_day(Boolean all_day) {
        this.all_day = all_day;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Attachmants> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachmants> attachments) {
        this.attachments = attachments;
    }

    public Integer getImportance() {
        return importance;
    }

    public void setImportance(Integer importance) {
        this.importance = importance;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

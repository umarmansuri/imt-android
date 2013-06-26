
package its.my.time.data.ws.events.plugins.participant;

import java.util.HashMap;
import java.util.Map;



public class ParticipantsBeanWS {

    private Integer id;
    private Integer participation_state;
    private Event event;
    private Participant participant;
    private Account account;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParticipation_state() {
        return participation_state;
    }

    public void setParticipation_state(Integer participation_state) {
        this.participation_state = participation_state;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

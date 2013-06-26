
package its.my.time.data.ws.events.plugins.participation;

import java.util.HashMap;
import java.util.Map;



public class ParticipationBeanWS {

    private Integer id;
    private Integer participation_state;
    private Event event; 
    private Participation participant;
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

    public Participation getParticipant() {
        return participant;
    }

    public void setParticipant(Participation participant) {
        this.participant = participant;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

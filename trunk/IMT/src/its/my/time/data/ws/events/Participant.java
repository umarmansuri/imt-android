
package its.my.time.data.ws.events;

import java.util.HashMap;
import java.util.Map;



public class Participant {

    private Account account;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

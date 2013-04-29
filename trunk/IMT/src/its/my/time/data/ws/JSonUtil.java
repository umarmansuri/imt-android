package its.my.time.data.ws;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class JSonUtil {

	@SuppressWarnings("deprecation")
	public static List<EventBaseBean> getEventBaseBeansFromJson(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = null;

		List<EventBaseBean> events = new ArrayList<EventBaseBean>();
		try {
			jp = jsonFactory.createJsonParser(json.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.getSerializationConfig().setDateFormat(sdf);
			objectMapper.getDeserializationConfig().setDateFormat(sdf);
			events = objectMapper.readValue(jp, new TypeReference<List<EventBaseBean>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return events;
	}
	@SuppressWarnings("deprecation")
	public static EventBaseBean getEventFromJson(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = null;

		EventBaseBean event = null;
		try {
			jp = jsonFactory.createJsonParser(json.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.getSerializationConfig().setDateFormat(sdf);
			objectMapper.getDeserializationConfig().setDateFormat(sdf);
			event = objectMapper.readValue(jp, EventBaseBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return event;
	}
}

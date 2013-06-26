package its.my.time.data.ws.events;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class WSSendEvent extends WSPostBase<EventBaseBean>{

	public WSSendEvent(Context context, EventBaseBean event, PostCallback<EventBaseBean> callBack) {
		super(context, event, callBack);
	}

	@Override
	public String getUrl() {
		return "api/events";
	}
	
	@Override
	public BaseRepository<EventBaseBean> getRepository() {
		return new EventBaseRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs){
		EventBaseBean event = getObject();
		
		if(event.getIdDistant() <= 0) {
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_idEvent", String.valueOf(0)));
		} else {
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_idEvent", String.valueOf(event.getIdDistant())));
		}
		
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_title", event.getTitle()));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_content", event.getDetails()));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_date", DateUtil.getTimeInIso(event.gethDeb())));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_dateFin", DateUtil.getTimeInIso(event.gethFin())));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_allDay", String.valueOf(event.isAllDay())));
        CompteBean compte = new CompteRepository(getContext()).getById(event.getCid());
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_account", String.valueOf(compte.getIdDistant())));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_importance", "0"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_type", Types.Event.getLabelById(event.getTypeId())));
		return nameValuePairs;
	}
}

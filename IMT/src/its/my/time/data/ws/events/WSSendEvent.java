package its.my.time.data.ws.events;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendEvent extends WSPostBase<EventBaseBean>{

	public WSSendEvent(Activity context, PostCallback<EventBaseBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return URL_API_EVENT_POST;
	}

	@Override
	public EventBaseBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_title", "Titre depuis le mobile"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_content", "Le contenu de l'évènement depuisl e mobile"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_date", DateUtil.getTimeInIso(new GregorianCalendar(2013, 04, 9, 12, 35, 00))));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_dateEnd", DateUtil.getTimeInIso(new GregorianCalendar(2013, 04, 9, 12, 35, 00))));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_allDay", "false"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_account", "1"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_importance", "0"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_idEvent", "0"));
        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_type", "0"));
		return nameValuePairs;
	}

}

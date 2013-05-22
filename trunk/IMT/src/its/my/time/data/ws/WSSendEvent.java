package its.my.time.data.ws;

import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendEvent extends WSBase{

	public WSSendEvent(Activity context, Callback callBack) {
		super(context, callBack);
	}

	@Override
	public Exception run() {
		try {
			HttpClient client = createClient();
			URI website = new URI(URL_API_POST); 
			HttpPost request = new HttpPost();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_title", "Titre depuis le mobile"));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_content", "Le contenu de l'évènement depuisl e mobile"));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_date", DateUtil.getTimeInIso(new GregorianCalendar(2013, 04, 9, 12, 35, 00))));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_dateEnd", DateUtil.getTimeInIso(new GregorianCalendar(2013, 04, 9, 12, 35, 00))));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_allDay", "false"));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_account", "1"));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_importance", "0"));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_idEvent", "0"));
	        nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_type", "0"));
	    		        request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			request.setURI(website);
			HttpResponse response = client.execute(request);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}

}

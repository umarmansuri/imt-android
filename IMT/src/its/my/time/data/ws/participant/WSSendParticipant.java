package its.my.time.data.ws.participant;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendParticipant extends WSPostBase<ContactBean>{

	public WSSendParticipant(Activity context, PostCallback<ContactBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participant.json";
	}

	@Override
	public ContactBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("idParticipant", "1"));
        nameValuePairs.add(new BasicNameValuePair("idAccount", "1"));
        nameValuePairs.add(new BasicNameValuePair("idContact", "1"));
        nameValuePairs.add(new BasicNameValuePair("idEvent", "1"));
		return nameValuePairs;
	}
}

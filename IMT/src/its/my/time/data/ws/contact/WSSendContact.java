package its.my.time.data.ws.contact;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendContact extends WSPostBase<ContactBean>{

	public WSSendContact(Activity context, ContactBean contact, PostCallback<ContactBean> callBack) {
		super(context, contact, callBack);
	}

	@Override
	public String getUrl() {
		return "";
	}

	@Override
	public ContactBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("", ""));
        nameValuePairs.add(new BasicNameValuePair("", ""));
        nameValuePairs.add(new BasicNameValuePair("", ""));
        nameValuePairs.add(new BasicNameValuePair("", ""));
		return nameValuePairs;
	}
}

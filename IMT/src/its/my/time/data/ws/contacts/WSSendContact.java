package its.my.time.data.ws.contacts;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

public class WSSendContact extends WSPostBase<ContactBean>{

	public WSSendContact(Activity context, PostCallback<ContactBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContactBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(
			List<NameValuePair> nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

}

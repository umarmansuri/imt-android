package its.my.time.data.ws.contact;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.bdd.contacts.ContactRepository;
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
		return "/api/contact.json";
	}
	
	@Override
	public BaseRepository<ContactBean> getRepository() {
		return new ContactRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
		nameValuePairs.add(new BasicNameValuePair("contact_id ", ""));
        nameValuePairs.add(new BasicNameValuePair("contact_firstname", ""));
        nameValuePairs.add(new BasicNameValuePair("contact_name", ""));
        nameValuePairs.add(new BasicNameValuePair("contact_email", ""));
        nameValuePairs.add(new BasicNameValuePair("contact_user", ""));
        nameValuePairs.add(new BasicNameValuePair("contact_account", ""));
		return nameValuePairs;
	}
}

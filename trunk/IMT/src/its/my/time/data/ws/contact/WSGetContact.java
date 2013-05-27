package its.my.time.data.ws.contact;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetContact extends WSGetBase<ContactBean>{

	public WSGetContact(Activity context, int id, GetCallback<ContactBean> callBack) {
		super(context, id, callBack);
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

}

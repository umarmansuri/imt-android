package its.my.time.data.ws.contacts;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetContact extends WSGetBase<ContactBean>{

	public WSGetContact(Activity context, GetCallback<ContactBean> callBack) {
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

}

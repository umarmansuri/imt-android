package its.my.time.data.ws.events.plugins.participants;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetParticipant extends WSGetBase<ContactBean>{

	public WSGetParticipant(Activity context, int id, GetCallback<ContactBean> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participants/";
	}

	@Override
	public ContactBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

package its.my.time.data.ws.participant;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetParticipant extends WSGetBase<ContactBean>{

	public WSGetParticipant(Activity context, GetCallback<ContactBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participants/1.json";
	}

	@Override
	public ContactBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

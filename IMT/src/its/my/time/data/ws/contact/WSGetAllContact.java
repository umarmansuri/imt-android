package its.my.time.data.ws.contact;

import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllContact extends WSGetBase<List<ContactBean>>{

	public WSGetAllContact(Activity context, int id, GetCallback<List<ContactBean>> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "";
	}

	@Override
	public List<ContactBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

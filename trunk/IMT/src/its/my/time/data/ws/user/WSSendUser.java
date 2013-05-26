package its.my.time.data.ws.user;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendUser extends WSPostBase<CompteBean>{

	public WSSendUser(Activity context, PostCallback<CompteBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/user.json";
	}

	@Override
	public CompteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("gcm_id", "gsm_id"));
        nameValuePairs.add(new BasicNameValuePair("name", "nom"));
        nameValuePairs.add(new BasicNameValuePair("firstname", "prenom"));
		return nameValuePairs;
	}

}

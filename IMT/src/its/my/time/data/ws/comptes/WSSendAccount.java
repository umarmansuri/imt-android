package its.my.time.data.ws.comptes;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;

import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendAccount extends WSPostBase<CompteBean>{

	public WSSendAccount(Activity context, PostCallback<CompteBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/account.json";
	}

	@Override
	public CompteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair("imt_accountbundle_accountimttype_idAccount", "1"));
        nameValuePairs.add(new BasicNameValuePair("imt_accountbundle_accountimttype_type", "imt"));
        nameValuePairs.add(new BasicNameValuePair("imt_accountbundle_accountimttype[title]", "compte depuis le mobile"));
        nameValuePairs.add(new BasicNameValuePair("imt_accountbundle_accountimttype[color]", "fc-event-orange"));
        nameValuePairs.add(new BasicNameValuePair("imt_accountbundle_accountimttype[active]", "true"));
		return nameValuePairs;
	}

}

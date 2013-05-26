package its.my.time.data.ws.user;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetUser extends WSGetBase<CompteBean>{

	public WSGetUser(Activity context, GetCallback<CompteBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/users/1.json";
	}

	@Override
	public CompteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

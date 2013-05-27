package its.my.time.data.ws.user;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetUser extends WSGetBase<CompteBean>{

	public WSGetUser(Activity context, int id, GetCallback<CompteBean> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/users/";
	}

	@Override
	public CompteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

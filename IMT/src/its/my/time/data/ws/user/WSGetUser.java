package its.my.time.data.ws.user;

import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetUser extends WSGetBase<UtilisateurBeanWS>{

	public WSGetUser(Activity context, int id, GetCallback<UtilisateurBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/users/";
	}
}

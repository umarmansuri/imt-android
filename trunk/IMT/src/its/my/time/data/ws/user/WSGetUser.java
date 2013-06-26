package its.my.time.data.ws.user;

import its.my.time.data.ws.WSGetBase;
import android.content.Context;

public class WSGetUser extends WSGetBase<UtilisateurBeanWS>{

	public WSGetUser(Context context, int id, GetCallback<UtilisateurBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/users/";
	}
}

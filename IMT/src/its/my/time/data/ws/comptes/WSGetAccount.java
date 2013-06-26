package its.my.time.data.ws.comptes;

import its.my.time.data.ws.WSGetBase;
import android.content.Context;

public class WSGetAccount extends WSGetBase<CompteBeanWS>{

	public WSGetAccount(Context context, int id, GetCallback<CompteBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/accounts/";
	}
}

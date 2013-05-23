package its.my.time.data.ws.comptes;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetAccount extends WSGetBase<CompteBean>{

	public WSGetAccount(Activity context, GetCallback<CompteBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

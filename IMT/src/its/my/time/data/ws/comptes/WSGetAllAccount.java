package its.my.time.data.ws.comptes;

import java.util.List;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetAllAccount extends WSGetBase<List<CompteBean>>{

	public WSGetAllAccount(Activity context, int id, GetCallback<List<CompteBean>> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/accounts/";
	}

	@Override
	public List<CompteBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

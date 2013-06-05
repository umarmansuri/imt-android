package its.my.time.data.ws.events.plugins.odj;

import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetOdj extends WSGetBase<OdjBean>{

	public WSGetOdj(Activity context, int id, GetCallback<OdjBean> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}
}

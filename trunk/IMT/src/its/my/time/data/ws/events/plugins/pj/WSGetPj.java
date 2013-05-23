package its.my.time.data.ws.events.plugins.pj;

import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetPj extends WSGetBase<PjBean>{

	public WSGetPj(Activity context, GetCallback<PjBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PjBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

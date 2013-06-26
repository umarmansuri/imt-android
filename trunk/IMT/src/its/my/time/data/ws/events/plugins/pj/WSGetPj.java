package its.my.time.data.ws.events.plugins.pj;

import its.my.time.data.ws.WSGetBase;
import android.content.Context;

public class WSGetPj extends WSGetBase<PjBeanWS>{

	public WSGetPj(Context context, int id, GetCallback<PjBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/attachments/";
	}
}

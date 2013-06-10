package its.my.time.data.ws.events;

import its.my.time.data.ws.WSGetBase;
import android.app.Activity;
import android.content.Context;

public class WSGetEvent extends WSGetBase<EventBeanWS>{

	public WSGetEvent(Activity context, int id, GetCallback<EventBeanWS> callBack) {
		super(context, id, callBack);
	}
	
	public WSGetEvent(Context context, int id, GetCallback<EventBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "api/events/";
	}

}

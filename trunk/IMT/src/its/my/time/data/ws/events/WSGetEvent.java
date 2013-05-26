package its.my.time.data.ws.events;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetEvent extends WSGetBase<EventBaseBean>{

	public WSGetEvent(Activity context, GetCallback<EventBaseBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		return "api/events/1.json";
	}

	@Override
	public EventBaseBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

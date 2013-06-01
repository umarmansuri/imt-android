package its.my.time.data.ws.events;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetEvent extends WSGetBase<EventBaseBean>{

	public WSGetEvent(Activity context, int id, GetCallback<EventBaseBean> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "api/events/";
	}

	@Override
	public EventBaseBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

package its.my.time.data.ws.events.call;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.ws.events.WSGetEvent;
import android.app.Activity;

public class WSGetCallDetails extends WSGetEvent{

	public WSGetCallDetails(Activity context, int id, GetCallback<EventBaseBean> callBack) {
		super(context, id, callBack);
	}

}

package its.my.time.data.ws.events.task;

import its.my.time.data.ws.events.EventBeanWS;
import its.my.time.data.ws.events.WSGetEvent;
import android.app.Activity;

public class WSGetTaskDetails extends WSGetEvent{

	public WSGetTaskDetails(Activity context, int id, GetCallback<EventBeanWS> callBack) {
		super(context, id, callBack);
	}

}

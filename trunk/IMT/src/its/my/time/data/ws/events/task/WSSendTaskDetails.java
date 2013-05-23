package its.my.time.data.ws.events.task;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.events.WSSendEvent;
import android.app.Activity;

public class WSSendTaskDetails extends WSSendEvent{

	public WSSendTaskDetails(Activity context, PostCallback<EventBaseBean> callBack) {
		super(context, callBack);
	}

}

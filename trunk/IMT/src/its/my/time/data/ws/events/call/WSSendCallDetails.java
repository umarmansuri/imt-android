package its.my.time.data.ws.events.call;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.ws.events.WSSendEvent;
import android.app.Activity;

public class WSSendCallDetails extends WSSendEvent{

	public WSSendCallDetails(Activity context, EventBaseBean event, PostCallback<EventBaseBean> callBack) {
		super(context, event, callBack);
	}

}

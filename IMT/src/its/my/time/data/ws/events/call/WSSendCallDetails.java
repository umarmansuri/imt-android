package its.my.time.data.ws.events.call;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.events.WSSendEvent;
import android.app.Activity;

public class WSSendCallDetails extends WSSendEvent{

	public WSSendCallDetails(Activity context, PostCallback<EventBaseBean> callBack) {
		super(context, callBack);
	}

}

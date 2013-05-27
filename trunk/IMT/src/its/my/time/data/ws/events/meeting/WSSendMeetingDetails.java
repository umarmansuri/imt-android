package its.my.time.data.ws.events.meeting;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.events.WSSendEvent;
import android.app.Activity;

public class WSSendMeetingDetails extends WSSendEvent{

	public WSSendMeetingDetails(Activity context, EventBaseBean event, PostCallback<EventBaseBean> callBack) {
		super(context, event, callBack);
	}

}

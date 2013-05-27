package its.my.time.data.ws.events.meeting;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.events.WSGetEvent;
import android.app.Activity;

public class WSGetMeetingDetails extends WSGetEvent{

	public WSGetMeetingDetails(Activity context, int id, GetCallback<EventBaseBean> callBack) {
		super(context, id, callBack);
	}

}

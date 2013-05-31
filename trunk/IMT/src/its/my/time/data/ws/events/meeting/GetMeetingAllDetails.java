package its.my.time.data.ws.events.meeting;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.events.WSGetAllEvent;

import java.util.List;

import android.app.Activity;

public class GetMeetingAllDetails extends WSGetAllEvent{

	public GetMeetingAllDetails(Activity context, int id, GetCallback<List<EventBaseBean>> callBack) {
		super(context, id, callBack);
	}

}

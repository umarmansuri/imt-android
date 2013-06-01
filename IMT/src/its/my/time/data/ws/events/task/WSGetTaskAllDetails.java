package its.my.time.data.ws.events.task;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.ws.events.WSGetAllEvent;

import java.util.List;

import android.app.Activity;

public class WSGetTaskAllDetails extends WSGetAllEvent{

	public WSGetTaskAllDetails(Activity context, int id, GetCallback<List<EventBaseBean>> callBack) {
		super(context, id, callBack);
	}

}

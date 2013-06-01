package its.my.time.data.ws.events.call;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.ws.events.WSGetAllEvent;

import java.util.List;

import android.app.Activity;

public class WSGetCallAllDetails extends WSGetAllEvent{

	public WSGetCallAllDetails(Activity context, int id, GetCallback<List<EventBaseBean>> callBack) {
		super(context, id, callBack);
	}

}

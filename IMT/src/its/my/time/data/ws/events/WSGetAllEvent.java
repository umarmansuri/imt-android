package its.my.time.data.ws.events;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllEvent extends WSGetBase<List<EventBaseBean>>{

	public WSGetAllEvent(Activity context, int id, GetCallback<List<EventBaseBean>> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "api/events/";
	}

	@Override
	public List<EventBaseBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

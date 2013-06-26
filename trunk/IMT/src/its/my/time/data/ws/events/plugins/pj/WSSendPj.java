package its.my.time.data.ws.events.plugins.pj;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

public class WSSendPj extends WSPostBase<PjBean>{

	public WSSendPj(Context context, PjBean pj, PostCallback<PjBean> callBack) {
		super(context, pj, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/attachment.json";
	}
	
	@Override
	public BaseRepository<PjBean> getRepository() {
		return new PjRepository(getContext());
	}

	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
        //nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_allDay", String.valueOf(event.isAllDay())));

        return nameValuePairs;
	}

}

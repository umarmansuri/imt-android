package its.my.time.data.ws.events.plugins.odj;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class WSSendOdj extends WSPostBase<OdjBean>{

	public WSSendOdj(Context context, OdjBean odj, PostCallback<OdjBean> callBack) {
		super(context, odj, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/order.json";
	}

	
	@Override
	public BaseRepository<OdjBean> getRepository() {
		return new OdjRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams( List<NameValuePair> nameValuePairs) {
		EventBaseBean event = new EventBaseRepository(getContext()).getById(getObject().getEid());
        nameValuePairs.add(new BasicNameValuePair("order_id", String.valueOf(getObject().getIdDistant())));
        nameValuePairs.add(new BasicNameValuePair("order_content", getObject().getValue()));
        nameValuePairs.add(new BasicNameValuePair("order_state", String.valueOf(getObject().getState())));
        nameValuePairs.add(new BasicNameValuePair("order_position", String.valueOf(getObject().getOrder())));
        nameValuePairs.add(new BasicNameValuePair("order_event ", String.valueOf(event.getIdDistant())));
		return nameValuePairs;
	}

}

package its.my.time.data.ws.events.plugins.pj;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

public class WSSendPj extends WSPostBase<PjBean>{

	public WSSendPj(Activity context, PjBean pj, PostCallback<PjBean> callBack) {
		super(context, pj, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PjBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getIdParam() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BaseRepository<PjBean> getRepository() {
		return new PjRepository(getContext());
	}

	@Override
	public List<NameValuePair> intitialiseParams(
			List<NameValuePair> nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

}

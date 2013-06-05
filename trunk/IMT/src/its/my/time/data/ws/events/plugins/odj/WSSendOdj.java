package its.my.time.data.ws.events.plugins.odj;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

public class WSSendOdj extends WSPostBase<OdjBean>{

	public WSSendOdj(Activity context, OdjBean odj, PostCallback<OdjBean> callBack) {
		super(context, odj, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OdjBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdParam() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BaseRepository<OdjBean> getRepository() {
		return new OdjRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(
			List<NameValuePair> nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

}

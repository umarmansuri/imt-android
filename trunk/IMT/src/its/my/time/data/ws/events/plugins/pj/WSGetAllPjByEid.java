package its.my.time.data.ws.events.plugins.pj;

import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllPjByEid extends WSGetBase<List<PjBean>>{

	private int eid;

	public WSGetAllPjByEid(Activity context, int id, int eid, GetCallback<List<PjBean>> callBack) {
		super(context, id, callBack);
		this.eid = eid;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PjBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

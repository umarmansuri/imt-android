package its.my.time.data.ws.events.plugins.odj;

import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllOdjByEid extends WSGetBase<List<OdjBean>>{

	private int eid;

	public WSGetAllOdjByEid(Activity context, int id, int eid, GetCallback<List<OdjBean>> callBack) {
		super(context, id, callBack);
		this.eid = eid;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OdjBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

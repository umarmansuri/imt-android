package its.my.time.data.ws.events.plugins.participants;

import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllParticipantsByEid extends WSGetBase<List<ParticipantBean>>{

	private int eid;

	public WSGetAllParticipantsByEid(Activity context, int id, int eid, GetCallback<List<ParticipantBean>> callBack) {
		super(context, id, callBack);
		this.eid = eid;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParticipantBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

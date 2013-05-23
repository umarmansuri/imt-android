package its.my.time.data.ws.events.plugins.participants;

import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetParticipant extends WSGetBase<ParticipantBean>{

	public WSGetParticipant(Activity context, GetCallback<ParticipantBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticipantBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

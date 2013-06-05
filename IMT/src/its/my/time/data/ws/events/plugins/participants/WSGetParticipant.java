package its.my.time.data.ws.events.plugins.participants;

import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetParticipant extends WSGetBase<ParticipantBeanWS>{

	public WSGetParticipant(Activity context, int id, GetCallback<ParticipantBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participants/";
	}
}

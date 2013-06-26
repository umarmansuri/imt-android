package its.my.time.data.ws.events.plugins.participation;

import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetParticipation extends WSGetBase<ParticipationBeanWS>{

	public WSGetParticipation(Activity context, int id, GetCallback<ParticipationBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participants/";
	}
}

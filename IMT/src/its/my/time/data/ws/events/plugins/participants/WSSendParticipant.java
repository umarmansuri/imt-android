package its.my.time.data.ws.events.plugins.participants;

import its.my.time.data.bdd.events.plugins.participation.ParticipationBean;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

public class WSSendParticipant extends WSPostBase<ParticipationBean>{

	public WSSendParticipant(Activity context, PostCallback<ParticipationBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParticipationBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(
			List<NameValuePair> nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

}

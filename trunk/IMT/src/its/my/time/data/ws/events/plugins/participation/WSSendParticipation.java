package its.my.time.data.ws.events.plugins.participation;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.plugins.participation.ParticipationBean;
import its.my.time.data.bdd.events.plugins.participation.ParticipationRepository;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class WSSendParticipation extends WSPostBase<ParticipationBean>{

	public WSSendParticipation(Context context, ParticipationBean participant, PostCallback<ParticipationBean> callBack) {
		super(context, participant, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participant";
	}

	@Override
	public BaseRepository<ParticipationBean> getRepository() {
		return new ParticipationRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
		
		return nameValuePairs;
	}
}

package its.my.time.data.ws.events.plugins.participants;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendParticipant extends WSPostBase<ParticipantBean>{

	public WSSendParticipant(Activity context, ParticipantBean participant, PostCallback<ParticipantBean> callBack) {
		super(context, participant, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/participant";
	}

	@Override
	public ParticipantBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseRepository<ParticipantBean> getRepository() {
		return new ParticipantRepository(getContext());
	}
	
	@Override
	public String getIdParam() {
		return "idParticipant";
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
		 ParticipantBean participant = getObject();
		 

			if(participant.getDateSync().equals(DateUtil.createCalendar())) {
		        nameValuePairs.add(new BasicNameValuePair("idParticipant", "0"));
			} else {
		        nameValuePairs.add(new BasicNameValuePair("idParticipant", String.valueOf(participant.getId())));
			}
		 
        nameValuePairs.add(new BasicNameValuePair("idAccount", String.valueOf(participant.getCid())));
        nameValuePairs.add(new BasicNameValuePair("idContact", String.valueOf(participant.getIdContactInfo())));
        nameValuePairs.add(new BasicNameValuePair("idEvent", String.valueOf(participant.getEid())));
		return nameValuePairs;
	}
}

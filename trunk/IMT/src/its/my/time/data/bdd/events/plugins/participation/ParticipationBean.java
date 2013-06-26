package its.my.time.data.bdd.events.plugins.participation;

import java.util.List;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;


public class ParticipationBean extends PluginBaseBean{

	private TableAttribut<Long> uid;
	private TableAttribut<Integer> participation;
	private TableAttribut<String> participant;
	
	public ParticipationBean() {
		uid = new TableAttribut<Long>("uid", 0l);
		participation = new TableAttribut<Integer>("participation", 0);
		participant = new TableAttribut<String>("participant", "");
	}
	
	public long getUid() {
		return uid.getValue();
	}
	
	public void setUid(long uid) {
		this.uid.setValue(uid);
	}

	public long getParticipation() {
		return participation.getValue();
	}
	public void setParticipation(int participation) {
		this.participation.setValue(participation);
	}

	public String getParticipant() {
		return participant.getValue();
	}
	public void setParticipant(String participant) {
		this.participant.setValue(participant);
	}
	
	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(uid);
		list.add(participation);
		list.add(participant);
		return super.getAttributs(list);
	}
}

package its.my.time.data.bdd.events.plugins.participation;

import java.util.List;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;


public class ParticipationBean extends PluginBaseBean{

	private TableAttribut<Long> uid;
	private TableAttribut<Integer> participation;
	
	public ParticipationBean() {
		uid = new TableAttribut<Long>("uid", -1l);
		participation = new TableAttribut<Integer>("participation", -1);
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
	
	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(uid);
		list.add(participation);
		return super.getAttributs(list);
	}
}

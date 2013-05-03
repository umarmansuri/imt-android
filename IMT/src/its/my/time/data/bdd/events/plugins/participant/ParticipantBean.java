package its.my.time.data.bdd.events.plugins.participant;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;

import java.util.List;


public class ParticipantBean extends PluginBaseBean{

	private TableAttribut<Integer> idContactInfo;

	public ParticipantBean() {
		super();
		idContactInfo = new TableAttribut<Integer>("uid", -1);
	}
	public int getIdContactInfo() {
		return idContactInfo.getValue();
	}
	public void setIdContactInfo(int idContactInfo) {
		this.idContactInfo.setValue(idContactInfo);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(idContactInfo);
		return super.getAttributs(list);
	}
}

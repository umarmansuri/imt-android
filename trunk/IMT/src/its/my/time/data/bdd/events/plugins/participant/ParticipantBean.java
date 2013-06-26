package its.my.time.data.bdd.events.plugins.participant;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;

import java.util.List;


public class ParticipantBean extends PluginBaseBean{

	private TableAttribut<Integer> idContactInfo;
	private TableAttribut<Integer> cid;

	public ParticipantBean() {
		super();
		idContactInfo = new TableAttribut<Integer>("uid", 0);
		cid = new TableAttribut<Integer>("cid", 0);
	}
	public int getIdContactInfo() {
		return idContactInfo.getValue();
	}
	public void setIdContactInfo(int idContactInfo) {
		this.idContactInfo.setValue(idContactInfo);
	}
	public TableAttribut<Integer> getCid() {
		return cid;
	}
	public void setCid(TableAttribut<Integer> cid) {
		this.cid = cid;
	}
	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(idContactInfo);
		list.add(cid);
		return super.getAttributs(list);
	}
}

package its.my.time.data.bdd.events.plugins;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;

import java.util.List;

public abstract class PluginBaseBean extends BaseBean{

	private TableAttribut<Long> eid;

	public PluginBaseBean() {
		super();
		this.eid = new TableAttribut<Long>("eid", -1l);
	}

	public long getEid() {
		return this.eid.getValue();
	}

	public void setEid(int eid) {
		this.eid.setValue(eid);
	}

	
	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(eid);
		return list;
	}
}

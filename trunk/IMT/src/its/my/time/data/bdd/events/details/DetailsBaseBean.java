package its.my.time.data.bdd.events.details;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;

import java.util.List;

public abstract class DetailsBaseBean extends BaseBean{

	private TableAttribut<Long> eid;

	public DetailsBaseBean() {
		super();
		this.eid = new TableAttribut<Long>("eid", 0l);
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

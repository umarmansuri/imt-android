package its.my.time.data.bdd.events.details.call;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.details.DetailsBaseBean;

import java.util.List;

public class CallDetailsBean extends DetailsBaseBean{

	private TableAttribut<Long> duration;

	public CallDetailsBean() {
		super();
		this.duration = new TableAttribut<Long>("duration", -1l);
	}

	public long getAddress() {
		return this.duration.getValue();
	}

	public void setAddress(String comment) {
		this.duration.setValue(comment);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(duration);
		return super.getAttributs(list);
	}
}

package its.my.time.data.bdd.events.details.meeting;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.details.DetailsBaseBean;

import java.util.List;

public class MeetingDetailsBean extends DetailsBaseBean{

	private TableAttribut<String> address;

	public MeetingDetailsBean() {
		super();
		this.address = new TableAttribut<String>("address", "");
	}

	public String getAddress() {
		return this.address.getValue();
	}

	public void setAddress(String comment) {
		this.address.setValue(comment);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(address);
		return super.getAttributs(list);
	}
}

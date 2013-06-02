package its.my.time.data.bdd.events.details.call;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.details.DetailsBaseBean;

import java.util.List;

public class CallDetailsBean extends DetailsBaseBean{

	private TableAttribut<Long> duration;
	private TableAttribut<String> phone;
	private TableAttribut<String> identifiant;
	private TableAttribut<String> user;

	public CallDetailsBean() {
		super();
		this.duration = new TableAttribut<Long>("duration", 0l);
		this.phone = new TableAttribut<String>("phone", "");
		this.user = new TableAttribut<String>("user", "");
		this.identifiant = new TableAttribut<String>("identifiant", "");
	}

	public String getUser() {
		return user.getValue();
	}
	public void setUser(String user) {
		this.user.setValue(user);
	}
	public String getIdentifiant() {
		return identifiant.getValue();
	}
	public void setIdentifiant(String identifiant) {
		this.identifiant.setValue(identifiant);
	}
	public String getPhone() {
		return phone.getValue();
	}
	public void setPhone(String phone) {
		this.phone.setValue(phone);
	}
	
	public long getDuration() {
		return this.duration.getValue();
	}

	public void setDuration(long duration) {
		this.duration.setValue(duration);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(duration);
		list.add(phone);
		list.add(user);
		list.add(identifiant);
		return super.getAttributs(list);
	}
}

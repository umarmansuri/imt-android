package its.my.time.data.bdd.events.plugins.pj;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class PjBean extends PluginBaseBean{

	private TableAttribut<String> name;
	private TableAttribut<Calendar> date;
	private TableAttribut<String> link;
	private TableAttribut<Long> uid;

	public PjBean() {
		super();
		this.date = new TableAttribut<Calendar>("date", DateUtil.createCalendar());
		this.name = new TableAttribut<String>("name", "");
		this.link = new TableAttribut<String>("link", "");
		this.uid = new TableAttribut<Long>("uid", -1l);
	}


	public Calendar getDate() {
		return this.date.getValue();
	}

	public void setDate(Calendar date) {
		this.date.setValue(date);
	}

	public String getName() {
		return this.name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getLink() {
		return this.link.getValue();
	}

	public void setLink(String link) {
		this.link.setValue(link);
	}

	public long getUid() {
		return this.uid.getValue();
	}

	public void setUid(long uid) {
		this.uid.setValue(uid);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(name);
		list.add(date);
		list.add(link);
		list.add(uid);
		return super.getAttributs(list);
	}
}

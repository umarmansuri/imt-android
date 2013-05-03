package its.my.time.data.bdd.events.plugins.note;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;

import java.util.List;


public class NoteBean extends PluginBaseBean{

	private TableAttribut<String> name;
	private TableAttribut<String> html;
	private TableAttribut<Integer> uid;

	public NoteBean() {
		super();
		this.name = new TableAttribut<String>("name", "");
		this.html = new TableAttribut<String>("html", "");
		this.uid = new TableAttribut<Integer>("uid", -1);
	}


	public String getName() {
		return this.name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getHtml() {
		return this.html.getValue();
	}

	public void setHtml(String html) {
		this.html.setValue(html);
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
		list.add(html);
		list.add(uid);
		return super.getAttributs(list);
	}
}

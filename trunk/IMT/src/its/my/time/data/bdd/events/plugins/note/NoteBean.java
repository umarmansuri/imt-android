package its.my.time.data.bdd.events.plugins.note;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;

import java.util.List;


public class NoteBean extends PluginBaseBean{

	private TableAttribut<String> html;

	public NoteBean() {
		super();
		this.html = new TableAttribut<String>("html", "");
	}

	public String getHtml() {
		return this.html.getValue();
	}

	public void setHtml(String html) {
		this.html.setValue(html);
	}

	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(html);
		return super.getAttributs(list);
	}
}

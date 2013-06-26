package its.my.time.data.bdd.events.plugins.pj;

import its.my.time.data.bdd.base.TableAttribut;
import its.my.time.data.bdd.events.plugins.PluginBaseBean;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.List;

public class PjBean extends PluginBaseBean{

	private TableAttribut<String> name;
	private TableAttribut<String> base64;
	private TableAttribut<String> extension;
	private TableAttribut<String> mime;
	private TableAttribut<String> path;
	private TableAttribut<Long> uid;

	public PjBean() {
		super();
		this.name = new TableAttribut<String>("name", "");
		this.extension = new TableAttribut<String>("extension", "");
		this.base64 = new TableAttribut<String>("base64", "");
		this.mime = new TableAttribut<String>("mime", "");
		this.path = new TableAttribut<String>("path", "");
		this.uid = new TableAttribut<Long>("uid", 0l);
	}

	public String getName() {
		return this.name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getPath() {
		return this.path.getValue();
	}

	public void setPath(String path) {
		this.path.setValue(path);
	}


	public String getMime() {
		return this.mime.getValue();
	}

	public void setMime(String mime) {
		this.mime.setValue(mime);
	}

	
	public String getBase64() {
		return this.base64.getValue();
	}

	public void setBase64(String base64) {
		this.base64.setValue(base64);
	}

	public String getExtension() {
		return this.extension.getValue();
	}

	public void setExtension(String extension) {
		this.extension.setValue(extension);
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
		list.add(extension);
		list.add(base64);
		list.add(mime);
		list.add(path);
		list.add(uid);
		return super.getAttributs(list);
	}
}

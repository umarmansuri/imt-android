package its.my.time.data.bdd.events.plugins.pj;

import java.util.Calendar;

public class PjBean {

	private int id;
	private String name;
	private Calendar date;
	private String link;
	private long uid;
	private long eid;

	public PjBean(int id, Calendar cal, String name, String type, String link,
			long uid, long eid) {
		super();
		this.id = id;
		this.date = cal;
		this.name = name;
		this.link = link;
		this.uid = uid;
		this.eid = eid;
	}

	public PjBean() {
		this.id = -1;
		this.name = "";
		this.date = null;
		this.link = "";
		this.uid = -1;
		this.eid = -1;
	}

	public Calendar getDate() {
		return this.date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUid() {
		return this.uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getEid() {
		return this.eid;
	}

	public void setEid(long eid) {
		this.eid = eid;
	}

}

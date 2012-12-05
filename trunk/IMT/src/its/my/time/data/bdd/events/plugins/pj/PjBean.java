package its.my.time.data.bdd.events.plugins.pj;

import java.util.Calendar;



public class PjBean {
 
	private int id;
	private String name;
	private Calendar date;
	private String link;
	private long uid;
	private long eid;
	
	public PjBean(int id, Calendar cal, String name, String type, String link, long uid, long eid) {
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
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getEid() {
		return eid;
	}

	public void setEid(long eid) {
		this.eid = eid;
	}
	
	
}

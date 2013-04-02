package its.my.time.data.bdd.events.plugins.note;


public class NoteBean {

	private int id;
	private String name;
	private String html;
	private long uid;
	private long eid;

	public NoteBean(int id, String name, String type, String link,
			long uid, long eid) {
		super();
		this.id = id;
		this.name = name;
		this.html = link;
		this.uid = uid;
		this.eid = eid;
	}

	public NoteBean() {
		this.id = -1;
		this.name = "";
		this.html = "";
		this.uid = -1;
		this.eid = -1;
	}


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHtml() {
		return this.html;
	}

	public void setHtml(String link) {
		this.html = link;
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

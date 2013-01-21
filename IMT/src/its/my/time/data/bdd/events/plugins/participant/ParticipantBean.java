package its.my.time.data.bdd.events.plugins.participant;

public class ParticipantBean {

	private int id;
	private int uid;
	private int eid;

	public ParticipantBean(int id, String title, String comment, int uid,
			int eid) {
		super();
		this.id = id;
		this.uid = uid;
		this.eid = eid;
	}

	public ParticipantBean() {
		this.id = -1;
		this.uid = -1;
		this.eid = -1;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return this.uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getEid() {
		return this.eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

}

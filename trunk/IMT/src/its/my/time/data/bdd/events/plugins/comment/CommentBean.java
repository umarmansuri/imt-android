package its.my.time.data.bdd.events.plugins.comment;

import java.util.Calendar;

public class CommentBean {

	private long id;
	private String comment;
	private Calendar date;
	private long uid;
	private long eid;

	public CommentBean(int id, String title, String comment, int uid, int eid) {
		super();
		this.id = id;
		this.comment = comment;
		this.uid = uid;
		this.eid = eid;
	}

	public CommentBean() {
		this.id = -1;
		this.comment = "";
		this.uid = -1;
		this.eid = -1;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public void setEid(int eid) {
		this.eid = eid;
	}

	public Calendar getDate() {
		return this.date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
}

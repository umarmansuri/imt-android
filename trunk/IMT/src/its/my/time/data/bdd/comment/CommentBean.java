package its.my.time.data.bdd.comment;



public class CommentBean {

	private int id;
	private String title;
	private String comment;
	private int uid;
	private int eid;
	
	public CommentBean(int id, String title, String comment, int uid, int eid) {
		super();
		this.id = id;
		this.title = title;
		this.comment = comment;
		this.uid = uid;
		this.eid = eid;
	}

	public CommentBean() {
		this.id = -1;
		this.title = "";
		this.comment = "";
		this.uid = -1;
		this.eid = -1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}
	
	
}

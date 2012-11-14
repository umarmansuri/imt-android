package its.my.time.data.bdd.compte;



public class CompteBean {

	private int id;
	private String title;
	private int color;
	private int type;
	private int uid;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int  getColor() {
		return color;
	}
	public void setColor(int  c) {
		this.color = c;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
}

package its.my.time.data.bdd.compte;



public class CompteBean {

	private long id;
	private String title;
	private int color;
	private int type;
	private boolean isShowed;
	
	public boolean isShowed() {
		return isShowed;
	}
	public void setShowed(boolean isShowed) {
		this.isShowed = isShowed;
	}
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}

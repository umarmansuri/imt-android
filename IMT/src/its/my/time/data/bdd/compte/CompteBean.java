package its.my.time.data.bdd.compte;

public class CompteBean {

	private int id;
	private String title;
	private int color;
	private int type = -1;
	private boolean isShowed;
	private long uid;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getColor() {
		return this.color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isShowed() {
		return this.isShowed;
	}

	public void setShowed(boolean isShowed) {
		this.isShowed = isShowed;
	}

	public long getUid() {
		return this.uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

}

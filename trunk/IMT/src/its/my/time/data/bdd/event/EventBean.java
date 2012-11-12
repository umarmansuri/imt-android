package its.my.time.data.bdd.event;

import java.util.Calendar;


public class EventBean {
	
	private String details;
	private Calendar hDeb;
	private Calendar hFin;
	private int id;
	private String title;
	private int cid;
	
	
	
	public EventBean(String details, Calendar hDeb, Calendar hFin, int id,
			String title, int cid) {
		super();
		this.details = details;
		this.hDeb = hDeb;
		this.hFin = hFin;
		this.id = id;
		this.title = title;
		this.cid = cid;
	}
	
	public EventBean() {
		super();
		this.details = "";
		this.hDeb = null;
		this.hFin = null;
		this.id = -1;
		this.title = "";
		this.cid = -1;
	}
	
	
	public String getDetails() {
		return details;
	}
	public Calendar gethDeb() {
		return hDeb;
	}
	public Calendar gethFin() {
		return hFin;
	}
	public int getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public void sethDeb(Calendar hDeb) {
		this.hDeb = hDeb;
	}
	public void sethFin(Calendar hFin) {
		this.hFin = hFin;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	

}

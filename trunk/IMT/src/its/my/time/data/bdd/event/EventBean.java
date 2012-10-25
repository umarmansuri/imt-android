package its.my.time.data.bdd.event;

import java.util.Calendar;


public class EventBean {
	
	private String details;
	private Calendar hDeb;
	private Calendar hFin;
	private int id;
	private String title;
	private int cid;
	
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

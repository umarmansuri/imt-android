package its.my.time.data.bdd.event;

import java.util.GregorianCalendar;


public class EventBean {
	
	private String details;
	private GregorianCalendar hDeb;
	private GregorianCalendar hFin;
	private int id;
	private String title;
	private int cid;
	
	public String getDetails() {
		return details;
	}
	public GregorianCalendar gethDeb() {
		return hDeb;
	}
	public GregorianCalendar gethFin() {
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
	public void sethDeb(GregorianCalendar hDeb) {
		this.hDeb = hDeb;
	}
	public void sethFin(GregorianCalendar hFin) {
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

package its.my.time.data.bdd.coment;

import java.util.Calendar;



public class ComentBean {

	private long id;
	private String title;
	private Calendar date;
	private String coment;
	private long id_event;
	
	public long getId_event() {
		return id_event;
	}
	public void setId_event(long id_event) {
		this.id_event = id_event;
	}
	public String getComent() {
		return coment;
	}
	public void setComent(String coment) {
		this.coment = coment;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}

package its.my.time.data.bdd.events.plugins.odj;

public class OdjBean {

	private long id;
	private String value;
	private long eid;
	
	public OdjBean() {
		id = -1;
		value = "";
		eid = -1;
	}
	
	public OdjBean(long id, String value, long eid) {
		super();
		this.id = id;
		this.value = value;
		this.eid = eid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getEid() {
		return eid;
	}
	public void setEid(long eid) {
		this.eid = eid;
	}
	

	
}

package its.my.time.data.bdd.events.plugins.odj;

public class OdjBean {

	private long id;
	private String value;
	private int order;
	private long eid;

	public OdjBean() {
		this.id = -1;
		this.value = "";
		this.eid = -1;
		this.order = -1;
	}

	public OdjBean(long id, String value, long eid, int order) {
		super();
		this.id = id;
		this.value = value;
		this.eid = eid;
		this.order = order;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getEid() {
		return this.eid;
	}

	public void setEid(long eid) {
		this.eid = eid;
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}

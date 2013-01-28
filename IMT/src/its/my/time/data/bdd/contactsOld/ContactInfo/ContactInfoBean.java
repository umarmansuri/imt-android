package its.my.time.data.bdd.contactsOld.ContactInfo;

public class ContactInfoBean {
	public static final int TYPE_MAIL = 0;
	public static final int TYPE_PHONE = 1;
	
	private int id = -1;
	private String value;
	private int type;
	private long contactId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String values) {
		this.value = values;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getContactId() {
		return contactId;
	}
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	
}

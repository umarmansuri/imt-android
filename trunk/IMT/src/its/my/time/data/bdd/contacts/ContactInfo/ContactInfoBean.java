package its.my.time.data.bdd.contacts.ContactInfo;

import java.util.List;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.TableAttribut;

public class ContactInfoBean extends BaseBean{
	public static final int TYPE_MAIL = 0;
	public static final int TYPE_PHONE = 1;
	
	private TableAttribut<String> value;
	private TableAttribut<Integer> type;
	private TableAttribut<Long>  contactId;
	
	public ContactInfoBean() {
		super();
		value = new TableAttribut<String>("value", "");
		type = new TableAttribut<Integer>("type", -1);
		contactId = new TableAttribut<Long>("contactId", -1l);
	}
	
	public String getValue() {
		return value.getValue();
	}
	public void setValue(String values) {
		this.value.setValue(values);
	}
	public int getType() {
		return type.getValue();
	}
	public void setType(int type) {
		this.type.setValue(type);
	}
	public long getContactId() {
		return contactId.getValue();
	}
	public void setContactId(long contactId) {
		this.contactId.setValue(contactId);
	}
	
	@Override
	public List<TableAttribut<?>> getAttributs(List<TableAttribut<?>> list) {
		list.add(value);
		list.add(type);
		list.add(contactId);
		return list;
	}
}

package its.my.time.data.bdd.contactsOld.ContactInfo;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactInfoRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_VALUE = 1;
	public static final int KEY_INDEX_TYPE = 2;
	public static final int KEY_INDEX_CONTACT_ID = 3;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_VALUE = "KEY_VALUE";
	public static final String KEY_TYPE = "KEY_TYPE";
	public static final String KEY_CONTACT_ID = "KEY_CONTACT_ID";

	public static final String DATABASE_TABLE = "contact_info";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " integer primary key autoincrement," 
			+ KEY_VALUE + " text not null,"  
			+ KEY_TYPE + " integer not null,"  
			+ KEY_CONTACT_ID + " integer not null);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_VALUE, KEY_TYPE, KEY_CONTACT_ID};

	public ContactInfoRepository(Context context) {
		super(context);
	}

	public List<ContactInfoBean> convertCursorToListObject(Cursor c) {
		final List<ContactInfoBean> liste = new ArrayList<ContactInfoBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final ContactInfoBean contactInfo = convertCursorToObject(c);
			liste.add(contactInfo);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ContactInfoBean convertCursorToObject(Cursor c) {
		final ContactInfoBean contactInfo = new ContactInfoBean();
		contactInfo.setId(c.getInt(KEY_INDEX_ID));
		contactInfo.setValue(c.getString(KEY_INDEX_VALUE));
		contactInfo.setType(c.getInt(KEY_INDEX_TYPE));
		contactInfo.setContactId(c.getInt(KEY_INDEX_CONTACT_ID));
		return contactInfo;
	}

	public ContactInfoBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final ContactInfoBean contact = convertCursorToObject(c);
		c.close();
		return contact;
	}

	public long insertContactInfo(ContactInfoBean contact) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_VALUE, contact.getValue());
		initialValues.put(KEY_TYPE, contact.getType());
		initialValues.put(KEY_CONTACT_ID, contact.getContactId());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public long insertOrUpdate(ContactInfoBean contactInfo) {
		final ContentValues initialValues = new ContentValues();
		if(contactInfo.getId() != -1) {
			initialValues.put(KEY_ID, contactInfo.getId());
		}
		initialValues.put(KEY_VALUE, contactInfo.getValue());
		initialValues.put(KEY_TYPE, contactInfo.getType());
		initialValues.put(KEY_CONTACT_ID, contactInfo.getContactId());
		open();
		final long res = this.db.replace(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deleteContactInfo(long rowId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public boolean deleteAll(long contactId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE, KEY_CONTACT_ID+ "=?", new String[] { "" + contactId }) > 0;
		close();
		return res;
	}

	public List<ContactInfoBean> getAllContactInfo() {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, null,null, null, null, null);
		final List<ContactInfoBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public List<ContactInfoBean> getAllByContactId(long contactId) {
		open();
		final Cursor c = db.query(DATABASE_TABLE, allAttr, KEY_CONTACT_ID
				+ "=?", new String[] { "" + contactId }, null, null, null);
		final List<ContactInfoBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public ContactInfoBean getById(long id) {
		open();
		final Cursor c = db.query(DATABASE_TABLE, allAttr, KEY_ID
				+ "=?", new String[] { "" + id }, null, null, null);
		final ContactInfoBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public int update(ContactInfoBean contactInfo) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_VALUE, contactInfo.getValue());
		initialValues.put(KEY_TYPE, contactInfo.getType());
		initialValues.put(KEY_CONTACT_ID, contactInfo.getContactId());
		open();
		final int nbRow = db.update(DATABASE_TABLE, initialValues, KEY_ID
				+ "=?", new String[] { "" + contactInfo.getId() });
		close();
		return nbRow;
	}
}

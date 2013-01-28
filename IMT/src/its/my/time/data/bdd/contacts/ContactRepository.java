package its.my.time.data.bdd.contacts;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoBean;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoRepository;
import its.my.time.util.ContactsUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactRepository extends DatabaseHandler {

	public static final int KEY_INDEX_RAW_CONTACT_ID = 0;
	public static final int KEY_INDEX_NOM = 1;
	public static final int KEY_INDEX_PRENOM = 2;

	public static final String KEY_RAW_CONTACT_ID = "KEY_ID";
	public static final String KEY_NOM = "KEY_NOM";
	public static final String KEY_PRENOM = "KEY_PRENOM";

	public static final String DATABASE_TABLE = "contact";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_RAW_CONTACT_ID + " integer primary key," 
			+ KEY_NOM + " text,"  
			+ KEY_PRENOM + " text);";

	private final String[] allAttr = new String[] { KEY_RAW_CONTACT_ID, KEY_NOM,KEY_PRENOM};

	public ContactRepository(Context context) {
		super(context);
	}

	public List<ContactBean> convertCursorToListObject(Cursor c) {
		final List<ContactBean> liste = new ArrayList<ContactBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final ContactBean contact = convertCursorToObject(c);
			liste.add(contact);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ContactBean convertCursorToObject(Cursor c) {
		final ContactBean contact = new ContactBean();
		contact.setRawContactId(c.getInt(KEY_INDEX_RAW_CONTACT_ID));
		contact.setNom(c.getString(KEY_INDEX_NOM));
		contact.setPrenom(c.getString(KEY_INDEX_PRENOM));
		contact.setImage(ContactsUtil.loadContactPhoto(context, contact.getRawContactId()));
		contact.setInfos(new ContactInfoRepository(context).getAllByContactId(contact.getRawContactId()));
		return contact;
	}

	public ContactBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final ContactBean contact = convertCursorToObject(c);
		c.close();
		return contact;
	}

	public long insertContact(ContactBean contact) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_RAW_CONTACT_ID, contact.getRawContactId());
		initialValues.put(KEY_NOM, contact.getNom());
		initialValues.put(KEY_PRENOM, contact.getPrenom());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		ContactInfoRepository repoInfo = new ContactInfoRepository(context);
		for (ContactInfoBean contactInfo : contact.getInfos()) {
			repoInfo.insertOrUpdate(contactInfo);
		}
		return res;
	}

	public boolean deleteContact(long contactId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,KEY_RAW_CONTACT_ID + "=" + contactId, null) > 0;
		close();

		new ContactInfoRepository(context).deleteAll(contactId);
		
		return res;
	}


public List<ContactBean> getAllContact() {
	open();
	final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, null,
			null, null, null, null);
	final List<ContactBean> res = convertCursorToListObject(c);
	close();
	return res;
}

public ContactBean getById(long id) {
	open();
	final Cursor c = db.query(DATABASE_TABLE, allAttr, KEY_RAW_CONTACT_ID
			+ "=?", new String[] { "" + id }, null, null, null);
	final ContactBean res = convertCursorToOneObject(c);
	close();
	return res;
}

public int update(ContactBean contact) {
	final ContentValues initialValues = new ContentValues();
	initialValues.put(KEY_NOM, contact.getNom());
	initialValues.put(KEY_PRENOM, contact.getPrenom());
	open();
	final int nbRow = db.update(DATABASE_TABLE, initialValues, KEY_RAW_CONTACT_ID
			+ "=?", new String[] { "" + contact.getRawContactId() });
	close();
	ContactInfoRepository repoInfo = new ContactInfoRepository(context);
	for (ContactInfoBean contactInfo : contact.getInfos()) {
		repoInfo.insertOrUpdate(contactInfo);
	}
	return nbRow;
}
}

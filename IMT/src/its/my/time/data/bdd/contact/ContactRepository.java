package its.my.time.data.bdd.contact;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_NOM = 1;
	public static final int KEY_INDEX_PRENOM = 2;
	public static final int KEY_INDEX_MAIL = 3;
	public static final int KEY_INDEX_TEL = 4;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_NOM = "KEY_NOM";
	public static final String KEY_PRENOM = "KEY_PRENOM";
	public static final String KEY_MAIL = "KEY_MAIL";
	public static final String KEY_TEL = "KEY_TEL";

	public static final String DATABASE_TABLE = "contact";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " INTEGER primary key autoincrement," 
			+ KEY_NOM+ " TEXT," 
			+ KEY_PRENOM+ " TEXT," 
			+ KEY_MAIL + " TEXT," 
			+ KEY_TEL + " TEXT);";

	private final String[] allAttr = new String[] { 
			KEY_ID, 
			KEY_NOM, 
			KEY_PRENOM, 
			KEY_MAIL, 
			KEY_TEL
	};

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
			final ContactBean compte = convertCursorToObject(c);
			liste.add(compte);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ContactBean convertCursorToObject(Cursor c) {
		final ContactBean contact = new ContactBean();
		contact.setId(c.getInt(KEY_INDEX_ID));
		contact.setNom(c.getString(KEY_INDEX_NOM));
		contact.setPrenom(c.getString(KEY_INDEX_PRENOM));
		contact.setMail(c.getString(KEY_INDEX_MAIL));
		contact.setTel(c.getString(KEY_INDEX_TEL));
		return contact;
	}

	public ContactBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final ContactBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertContact(ContactBean contact) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOM, contact.getNom());
		initialValues.put(KEY_PRENOM, contact.getPrenom());
		initialValues.put(KEY_MAIL, contact.getMail());
		initialValues.put(KEY_TEL, contact.getTel());
		open();
		final int res = (int) this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public int update(ContactBean contact) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOM, contact.getNom());
		initialValues.put(KEY_PRENOM, contact.getPrenom());
		initialValues.put(KEY_MAIL, contact.getMail());
		initialValues.put(KEY_TEL, contact.getTel());
		open();
		final int nbRow = this.db.update(DATABASE_TABLE, initialValues, KEY_ID+ "=?", new String[] { "" + contact.getId() });
		close();
		return nbRow;
	}

	public boolean deleteContact(ContactBean contact) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,KEY_ID + "=" + contact.getId(), null) > 0;
		close();
		return res;
	}

	public ContactBean getById(long id) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		final ContactBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

}

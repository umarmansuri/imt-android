package its.my.time.data.bdd.event.pj;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PjRepository extends DatabaseHandler{

	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_COMMENT = "comment";
	public static final String KEY_UID = "uid";
	public static final String KEY_EID = "eid";

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_TITLE = 1;
	public static final int KEY_INDEX_COMMENT = 2;
	public static final int KEY_INDEX_UID = 3;
	public static final int KEY_INDEX_EID = 4;


	public static final String DATABASE_TABLE = "comment";

	public static final String CREATE_TABLE =  "create table " + DATABASE_TABLE + "("
			+ KEY_ID + " integer primary key autoincrement,"
			+ KEY_TITLE + " text not null,"
			+ KEY_COMMENT + " text,"
			+ KEY_UID + " INTEGER not null,"
			+ KEY_EID + " INTEGER not null);";

	private String[] allAttr = new String[]{KEY_ID, KEY_TITLE, KEY_COMMENT, KEY_UID, KEY_EID};

	public PjRepository(Context context) {
		super(context);
	}

	public List<PjBean> convertCursorToListObject(Cursor c) {
		List<PjBean> liste = new ArrayList<PjBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			PjBean comment = convertCursorToObject(c);
			liste.add(comment);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public PjBean convertCursorToObject(Cursor c) {
		PjBean comment = new PjBean();
		comment.setId(c.getInt(KEY_INDEX_ID));
		comment.setTitle(c.getString(KEY_INDEX_TITLE));
		comment.setComment(c.getString(KEY_INDEX_COMMENT));
		comment.setUid(c.getInt(KEY_INDEX_UID));
		comment.setEid(c.getInt(KEY_INDEX_EID));
		return comment;
	}

	public PjBean convertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		PjBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertcomment(PjBean comment){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, comment.getTitle());
		initialValues.put(KEY_COMMENT, comment.getComment());
		initialValues.put(KEY_EID, comment.getEid());
		initialValues.put(KEY_UID, comment.getUid());
		initialValues.put(KEY_ID, comment.getId());
		open();
		long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deletecomment(long rowId) {
		open();
		boolean res = this.db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public PjBean getAllcomment() {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, null, null, null, null, null);
		PjBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public PjBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		close();
		PjBean res = convertCursorToOneObject(c);
		return res;
	}
	
	public List<PjBean> getAllByEid(int eid) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_EID + "=?", new String[] { "" + eid }, null, null, null);
		List<PjBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

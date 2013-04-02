package its.my.time.data.bdd.events.plugins.note;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class NoteRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_TITLE = 1;
	public static final int KEY_INDEX_HTML = 2;
	public static final int KEY_INDEX_UID = 3;
	public static final int KEY_INDEX_EID = 4;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_TITLE = "KEY_TITLE";
	public static final String KEY_HTML = "KEY_HTML";
	public static final String KEY_UID = "KEY_UID";
	public static final String KEY_EID = "KEY_EID";

	public static final String DATABASE_TABLE = "note";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " integer primary key autoincrement," + KEY_TITLE
			+ " text not null," + KEY_HTML
			+ " text not null," + KEY_UID + " INTEGER not null," + KEY_EID
			+ " INTEGER not null);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_TITLE, KEY_HTML, KEY_UID, KEY_EID };

	public NoteRepository(Context context) {
		super(context);
	}

	public List<NoteBean> convertCursorToListObject(Cursor c) {
		final List<NoteBean> liste = new ArrayList<NoteBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final NoteBean note = convertCursorToObject(c);
			liste.add(note);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public NoteBean convertCursorToObject(Cursor c) {
		final NoteBean note = new NoteBean();
		note.setId(c.getInt(KEY_INDEX_ID));
		note.setName(c.getString(KEY_INDEX_TITLE));
		note.setHtml(c.getString(KEY_INDEX_HTML));
		note.setUid(c.getInt(KEY_INDEX_UID));
		note.setEid(c.getInt(KEY_INDEX_EID));
		return note;
	}

	public NoteBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final NoteBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertnote(NoteBean note) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, note.getName());
		initialValues.put(KEY_HTML, note.getHtml());
		initialValues.put(KEY_EID, note.getEid());
		initialValues.put(KEY_UID, note.getUid());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}
	

	public long updateNote(NoteBean note) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, note.getName());
		initialValues.put(KEY_HTML, note.getHtml());
		initialValues.put(KEY_EID, note.getEid());
		initialValues.put(KEY_UID, note.getUid());
		long id = note.getId();
		open();
		long res = this.db.update(DATABASE_TABLE, initialValues, KEY_ID
				+ "=?", new String[] { "" + id });
		close();
		return res;
	}

	public boolean deletenote(long rowId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,
				KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public NoteBean getAllnote() {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, null,
				null, null, null, null);
		final NoteBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public NoteBean getById(long id) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_ID
				+ "=?", new String[] { "" + id }, null, null, null);
		close();
		final NoteBean res = convertCursorToOneObject(c);
		return res;
	}


	public List<NoteBean> getAllByUid(int uid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_EID
				+ "=?", new String[] { "" + uid }, null, null, null);
		final List<NoteBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public NoteBean getAllByUidEid(int eid, long uid) {
		open();
		final Cursor c = this.db.query(
				DATABASE_TABLE, 
				this.allAttr, 
				KEY_EID + "=? AND " + KEY_UID + "=?", 
				new String[] { "" + eid ,"" + uid}, 
				null, null, null);
		final NoteBean res = convertCursorToOneObject(c);
		close();
		return res;
	}
}

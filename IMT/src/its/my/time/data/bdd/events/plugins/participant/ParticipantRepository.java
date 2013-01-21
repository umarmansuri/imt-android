package its.my.time.data.bdd.events.plugins.participant;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ParticipantRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_UID = 1;
	public static final int KEY_INDEX_EID = 2;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_UID = "KEY_UID";
	public static final String KEY_EID = "KEY_EID";

	public static final String DATABASE_TABLE = "participant";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " INTEGER primary key autoincrement," + KEY_UID
			+ " INTEGER not null," + KEY_EID + " INTEGER not null);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_UID, KEY_EID };

	public ParticipantRepository(Context context) {
		super(context);
	}

	public List<ParticipantBean> convertCursorToListObject(Cursor c) {
		final List<ParticipantBean> liste = new ArrayList<ParticipantBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final ParticipantBean comment = convertCursorToObject(c);
			liste.add(comment);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ParticipantBean convertCursorToObject(Cursor c) {
		final ParticipantBean comment = new ParticipantBean();
		comment.setId(c.getInt(KEY_INDEX_ID));
		comment.setUid(c.getInt(KEY_INDEX_UID));
		comment.setEid(c.getInt(KEY_INDEX_EID));
		return comment;
	}

	public ParticipantBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final ParticipantBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertcomment(ParticipantBean comment) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EID, comment.getEid());
		initialValues.put(KEY_UID, comment.getUid());
		initialValues.put(KEY_ID, comment.getId());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deletecomment(long rowId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,
				KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public ParticipantBean getAllcomment() {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, null,
				null, null, null, null);
		final ParticipantBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public ParticipantBean getById(long id) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_ID
				+ "=?", new String[] { "" + id }, null, null, null);
		close();
		final ParticipantBean res = convertCursorToOneObject(c);
		return res;
	}

	public List<ParticipantBean> getAllByEid(int eid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_EID
				+ "=?", new String[] { "" + eid }, null, null, null);
		final List<ParticipantBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

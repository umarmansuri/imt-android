package its.my.time.data.bdd.event.participant;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ParticipantRepository extends DatabaseHandler{

	public static final String KEY_ID = "id";
	public static final String KEY_UID = "uid";
	public static final String KEY_EID = "eid";

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_UID = 1;
	public static final int KEY_INDEX_EID = 2;


	public static final String DATABASE_TABLE = "participant";

	public static final String CREATE_TABLE =  "create table " + DATABASE_TABLE + "("
			+ KEY_ID + " INTEGER primary key autoincrement,"
			+ KEY_UID + " INTEGER not null,"
			+ KEY_EID + " INTEGER not null);";

	private String[] allAttr = new String[]{KEY_ID, KEY_UID, KEY_EID};

	public ParticipantRepository(Context context) {
		super(context);
	}

	public List<ParticipantBean> convertCursorToListObject(Cursor c) {
		List<ParticipantBean> liste = new ArrayList<ParticipantBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			ParticipantBean comment = convertCursorToObject(c);
			liste.add(comment);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ParticipantBean convertCursorToObject(Cursor c) {
		ParticipantBean comment = new ParticipantBean();
		comment.setId(c.getInt(KEY_INDEX_ID));
		comment.setUid(c.getInt(KEY_INDEX_UID));
		comment.setEid(c.getInt(KEY_INDEX_EID));
		return comment;
	}

	public ParticipantBean convertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		ParticipantBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertcomment(ParticipantBean comment){
		ContentValues initialValues = new ContentValues();
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

	public ParticipantBean getAllcomment() {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, null, null, null, null, null);
		ParticipantBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public ParticipantBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		close();
		ParticipantBean res = convertCursorToOneObject(c);
		return res;
	}
	
	public List<ParticipantBean> getAllByEid(int eid) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_EID + "=?", new String[] { "" + eid }, null, null, null);
		List<ParticipantBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

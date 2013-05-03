package its.my.time.data.bdd.events.plugins.participation;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.util.Log;

public class ParticipationRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_EID = 1;
	public static final int KEY_INDEX_UID = 2;
	public static final int KEY_INDEX_PARTICIPATION = 3;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_EID = "KEY_EID";
	public static final String KEY_UID = "KEY_UID";
	public static final String KEY_PARTICIPATION = "KEY_PARTICIPATION";

	public static final String DATABASE_TABLE = "participation";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " integer primary key,"
			+ KEY_EID + " INTEGER not null," 
			+ KEY_UID + " INTEGER not null," 
			+ KEY_PARTICIPATION + " INTEGER not null);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_EID, KEY_UID, KEY_PARTICIPATION};

	public ParticipationRepository(Context context) {
		super(context);
	}

	public List<ParticipationBean> convertCursorToListObject(Cursor c) {
		final List<ParticipationBean> liste = new ArrayList<ParticipationBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final ParticipationBean participant = convertCursorToObject(c);
			liste.add(participant);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ParticipationBean convertCursorToObject(Cursor c) {
		final ParticipationBean participant = new ParticipationBean();
		participant.setId(c.getInt(KEY_INDEX_ID));
		participant.setEid(c.getInt(KEY_INDEX_EID));
		participant.setUid(c.getInt(KEY_INDEX_UID));
		participant.setParticipation(c.getInt(KEY_INDEX_PARTICIPATION));
		return participant;
	}

	public ParticipationBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final ParticipationBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertParticipant(ParticipationBean participation) {
		final ContentValues initialValues = new ContentValues();
		if (participation.getId() != -1) {
			initialValues.put(KEY_ID, participation.getId());
		}
		initialValues.put(KEY_EID, participation.getEid());
		initialValues.put(KEY_UID, participation.getUid());
		initialValues.put(KEY_PARTICIPATION, participation.getParticipation());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}
	
	public long updateParticipation(ParticipationBean participation) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EID, participation.getEid());
		initialValues.put(KEY_UID, participation.getUid());
		initialValues.put(KEY_PARTICIPATION, participation.getParticipation());
		final long id = participation.getId();
		open();
		final long res = db.update(DATABASE_TABLE, initialValues, KEY_ID
				+ "=?", new String[] { ""+id });
		close();
		return res;
	}

	public boolean deleteParticipant(long eid,long uid) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,
				KEY_UID + "=" + uid + " AND " +KEY_EID + "=" + eid, null) > 0;
		close();
		return res;
	}
	
	public ParticipationBean getPartByUidEid(long eid, long uid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_EID
				+ "=? AND " + KEY_UID + "=?", new String[] {String.valueOf(eid),String.valueOf(uid)}, null, null, null);
		final ParticipationBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public List<ParticipationBean> getAllByEid(int eid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_EID
				+ "=?", new String[] { "" + eid }, null, null, null);
		final List<ParticipationBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

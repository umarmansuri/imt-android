package its.my.time.data.bdd.events.plugins.participant;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ParticipantRepository extends DatabaseHandler {

	public static final int KEY_INDEX_EID = 0;
	public static final int KEY_INDEX_UID = 1;
	public static final int KEY_INDEX_CONTACT_INFO_ID = 2;

	public static final String KEY_EID = "KEY_EID";
	public static final String KEY_UID = "KEY_UID";
	public static final String KEY_CONTACT_INFO_ID = "KEY_CONTACT_INFO_ID";

	public static final String DATABASE_TABLE = "participant";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_EID + " INTEGER not null," 
			+ KEY_UID + " INTEGER not null," 
			+ KEY_CONTACT_INFO_ID + " INTEGER not null);";

	private final String[] allAttr = new String[] { KEY_EID, KEY_CONTACT_INFO_ID, KEY_UID};

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
			final ParticipantBean participant = convertCursorToObject(c);
			liste.add(participant);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ParticipantBean convertCursorToObject(Cursor c) {
		final ParticipantBean participant = new ParticipantBean();
		participant.setEid(c.getInt(KEY_INDEX_EID));
		participant.setUid(c.getInt(KEY_INDEX_UID));
		participant.setIdContactInfo(c.getInt(KEY_INDEX_CONTACT_INFO_ID));
		return participant;
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

	public long insertParticipant(ParticipantBean participant) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EID, participant.getEid());
		initialValues.put(KEY_UID, participant.getUid());
		initialValues.put(KEY_CONTACT_INFO_ID, participant.getIdContactInfo());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deleteParticipant(long eid,long uid, long contactInfoId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,
				KEY_UID + "=" + uid + " AND " +KEY_EID + "=" + eid + " AND " + KEY_CONTACT_INFO_ID + "=" + contactInfoId, null) > 0;
		close();
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

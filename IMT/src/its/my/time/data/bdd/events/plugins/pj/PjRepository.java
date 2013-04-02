package its.my.time.data.bdd.events.plugins.pj;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class PjRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_TITLE = 1;
	public static final int KEY_INDEX_DATE = 2;
	public static final int KEY_INDEX_LINK = 3;
	public static final int KEY_INDEX_UID = 4;
	public static final int KEY_INDEX_EID = 5;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_TITLE = "KEY_TITLE";
	public static final String KEY_DATE = "KEY_DATE";
	public static final String KEY_LINK = "KEY_HTML";
	public static final String KEY_UID = "KEY_UID";
	public static final String KEY_EID = "KEY_EID";

	public static final String DATABASE_TABLE = "pj";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " integer primary key autoincrement," + KEY_TITLE
			+ " text not null," + KEY_DATE + " text not null," + KEY_LINK
			+ " text not null," + KEY_UID + " INTEGER not null," + KEY_EID
			+ " INTEGER not null);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_TITLE,
			KEY_DATE, KEY_LINK, KEY_UID, KEY_EID };

	public PjRepository(Context context) {
		super(context);
	}

	public List<PjBean> convertCursorToListObject(Cursor c) {
		final List<PjBean> liste = new ArrayList<PjBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final PjBean pj = convertCursorToObject(c);
			liste.add(pj);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public PjBean convertCursorToObject(Cursor c) {
		final PjBean pj = new PjBean();
		pj.setId(c.getInt(KEY_INDEX_ID));
		pj.setName(c.getString(KEY_INDEX_TITLE));
		pj.setDate(DateUtil.getDateFromISO(c.getString(KEY_INDEX_DATE)));
		pj.setLink(c.getString(KEY_INDEX_LINK));
		pj.setUid(c.getInt(KEY_INDEX_UID));
		pj.setEid(c.getInt(KEY_INDEX_EID));
		return pj;
	}

	public PjBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final PjBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertpj(PjBean pj) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, pj.getName());
		initialValues.put(KEY_DATE, DateUtil.getTimeInIso(pj.getDate()));
		initialValues.put(KEY_LINK, pj.getLink());
		initialValues.put(KEY_EID, pj.getEid());
		initialValues.put(KEY_UID, pj.getUid());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deletepj(long rowId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,
				KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public PjBean getAllpj() {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, null,
				null, null, null, null);
		final PjBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public PjBean getById(long id) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_ID
				+ "=?", new String[] { "" + id }, null, null, null);
		close();
		final PjBean res = convertCursorToOneObject(c);
		return res;
	}

	public List<PjBean> getAllByEid(int eid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_EID
				+ "=?", new String[] { "" + eid }, null, null, null);
		final List<PjBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public List<PjBean> getAllByUid(int uid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_EID
				+ "=?", new String[] { "" + uid }, null, null, null);
		final List<PjBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

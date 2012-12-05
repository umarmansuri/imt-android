package its.my.time.data.bdd.events.plugins.comment;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CommentRepository extends DatabaseHandler{

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_COMMENT = 1;
	public static final int KEY_INDEX_DATE = 2;
	public static final int KEY_INDEX_UID = 3;
	public static final int KEY_INDEX_EID = 4;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_COMMENT = "KEY_COMMENT";
	public static final String KEY_DATE = "KEY_DATE";
	public static final String KEY_UID = "KEY_UID";
	public static final String KEY_EID = "KEY_EID";


	public static final String DATABASE_TABLE = "comment";

	public static final String CREATE_TABLE =  "create table " + DATABASE_TABLE + "("
			+ KEY_ID + " integer primary key autoincrement,"
			+ KEY_COMMENT + " text not null,"
			+ KEY_DATE + " text not null,"
			+ KEY_UID + " INTEGER not null,"
			+ KEY_EID + " INTEGER not null);";

	private String[] allAttr = new String[]{
			KEY_ID, 
			KEY_COMMENT, 
			KEY_DATE, 
			KEY_UID, 
			KEY_EID};

	public CommentRepository(Context context) {
		super(context);
	}

	public List<CommentBean> convertCursorToListObject(Cursor c) {
		List<CommentBean> liste = new ArrayList<CommentBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			CommentBean comment = convertCursorToObject(c);
			liste.add(comment);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public CommentBean convertCursorToObject(Cursor c) {
		CommentBean comment = new CommentBean();
		comment.setId(c.getInt(KEY_INDEX_ID));
		comment.setComment(c.getString(KEY_INDEX_COMMENT));
		comment.setDate(DateUtil.getDateFromISO(c.getString(KEY_INDEX_DATE)));
		comment.setUid(c.getInt(KEY_INDEX_UID));
		comment.setEid(c.getInt(KEY_INDEX_EID));
		return comment;
	}

	public CommentBean convertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		CommentBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertComment(CommentBean comment){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_COMMENT, comment.getComment());
		initialValues.put(KEY_DATE, DateUtil.getTimeInIso(comment.getDate()));
		initialValues.put(KEY_EID, comment.getEid());
		initialValues.put(KEY_UID, comment.getUid());
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

	public CommentBean getAllcomment() {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, null, null, null, null, KEY_DATE);
		CommentBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public CommentBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		close();
		CommentBean res = convertCursorToOneObject(c);
		return res;
	}

	public List<CommentBean> getAllByEid(int eid) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_EID + "=?", new String[] { "" + eid }, null, null, KEY_DATE);
		List<CommentBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

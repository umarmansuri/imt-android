package its.my.time.data.bdd.events.plugins.odj;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class OdjRepository extends DatabaseHandler{

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_VALUE = 1;
	public static final int KEY_INDEX_EID = 2;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_VALUE = "KEY_VALUE";
	public static final String KEY_EID = "KEY_EID";


	public static final String DATABASE_TABLE = "odj";

	public static final String CREATE_TABLE =  "create table " + DATABASE_TABLE + " ("
			+ KEY_ID + " integer primary key autoincrement,"
			+ KEY_VALUE + " text not null,"
			+ KEY_EID + " INTEGER not null);";

	private String[] allAttr = new String[]{
			KEY_ID, 
			KEY_VALUE, 
			KEY_EID};

	public OdjRepository(Context context) {
		super(context);
	}

	public List<OdjBean> convertCursorToListObject(Cursor c) {
		List<OdjBean> liste = new ArrayList<OdjBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			OdjBean odj = convertCursorToObject(c);
			liste.add(odj);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public OdjBean convertCursorToObject(Cursor c) {
		OdjBean odj = new OdjBean();
		odj.setId(c.getInt(KEY_INDEX_ID));
		odj.setValue(c.getString(KEY_INDEX_VALUE));
		odj.setEid(c.getInt(KEY_INDEX_EID));
		return odj;
	}

	public OdjBean convertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		OdjBean odj = convertCursorToObject(c);
		c.close();
		return odj;
	}

	public long insertOdj(OdjBean odj){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_VALUE, odj.getValue());
		initialValues.put(KEY_EID, odj.getEid());
		open();
		long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deleteOdj(long rowId) {
		open();
		boolean res = this.db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public OdjBean getAllOdj() {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, null, null, null, null, null);
		OdjBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public OdjBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		close();
		OdjBean res = convertCursorToOneObject(c);
		return res;
	}

	public List<OdjBean> getAllByEid(int eid) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_EID + "=?", new String[] { "" + eid }, null, null, null);
		List<OdjBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

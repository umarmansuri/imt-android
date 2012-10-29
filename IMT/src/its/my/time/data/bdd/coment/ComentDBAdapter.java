package its.my.time.data.bdd.coment;

import its.my.time.data.bdd.DBAdapterBase;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ComentDBAdapter extends DBAdapterBase{

	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DATE = "date";
	public static final String KEY_COMENT = "coment";
	public static final String KEY_ID_EVENT = "id event";
	public static final String KEY_UID = "uid";
	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_TITLE = 1;
	public static final int KEY_INDEX_DATE = 2;
	public static final int KEY_INDEX_COMENT = 3;
	public static final int KEY_INDEX_ID_EVENT = 3;
	public static final int KEY_INDEX_UID = 5;


	public static final String DATABASE_TABLE = "Coment";

	private String[] allAttr = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_COMENT, KEY_ID_EVENT, KEY_UID};

	public ComentDBAdapter(Context context) {
		super(context);
	}

	public List<ComentBean> ConvertCursorToMapObject(Cursor c) {
		List<ComentBean> liste = new ArrayList<ComentBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			ComentBean Coment = ConvertCursorToObject(c);
			liste.add(Coment);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public ComentBean ConvertCursorToObject(Cursor c) {
		ComentBean Coment = new ComentBean();
		Coment.setId(c.getLong(KEY_INDEX_ID));
		Coment.setTitle(c.getString(KEY_INDEX_TITLE));
		Coment.setDate(DateUtil.getDateFromISO(c.getString(KEY_INDEX_DATE)));
		Coment.setComent(c.getString(KEY_INDEX_COMENT));
		Coment.setId_event(c.getLong(KEY_INDEX_ID_EVENT));
		return Coment;
	}

	public ComentBean ConvertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		ComentBean event = ConvertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertComent(ComentBean Coment, EventBean Event){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, Coment.getTitle());
		initialValues.put(KEY_DATE, DateUtil.getTimeInIso(Coment.getDate()));
		initialValues.put(KEY_COMENT, Coment.getComent());
		initialValues.put(KEY_ID_EVENT, Event.getId());
		open();
		long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deleteComent(long rowId) {
		open();
		boolean res = this.db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public Cursor getAllComent() {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, null, null, null, null, null);
		close();
		return c;
	}

	public ComentBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null);
		close();
		ComentBean res = ConvertCursorToOneObject(c);
		return res;
	}
	
	public Cursor getByIdEvent(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID_EVENT + "=?", null, null, null, null);
		close();
		return c;
	}
}

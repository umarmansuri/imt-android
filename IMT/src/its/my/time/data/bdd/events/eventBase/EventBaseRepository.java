package its.my.time.data.bdd.events.eventBase;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class EventBaseRepository extends DatabaseHandler{

	private static final int KEY_INDEX_ID = 0;
	private static final int KEY_INDEX_DETAILS = 1;
	private static final int KEY_INDEX_HDEB = 2;
	private static final int KEY_INDEX_HFIN = 3;
	private static final int KEY_INDEX_TITLE = 4;
	private static final int KEY_INDEX_CID = 5;
	private static final int KEY_INDEX_TYPE_ID = 6;
	private static final int KEY_INDEX_DETAILS_ID = 5;
	private static final int KEY_INDEX_ALL_DAY = 6;

	private static final String KEY_ID = "KEY_ID";
	private static final String KEY_DETAILS = "KEY_DETAILS";
	private static final String KEY_HDEB = "KEY_HDEB";
	private static final String KEY_HFIN = "KEY_HFIN";
	private static final String KEY_TITLE = "KEY_TITLE";
	private static final String KEY_CID = "KEY_CID";
	private static final String KEY_TYPE_ID = "KEY_TYPE_ID";
	private static final String KEY_DETAILS_ID = "KEY_DETAILS_ID";
	private static final String KEY_ALL_DAY = "KEY_ALL_DAY";

	public static final class Types {
		public static final int  TYPE_BASE = 0;
		public static final int  TYPE_TASK = 1;
		public static final int  TYPE_MEETING = 2;	
		public static final int  TYPE_CALL = 3;	
	}

	private static final String DATABASE_TABLE = "event";

	public static final String CREATE_TABLE =  "create table " + DATABASE_TABLE + "("
			+ KEY_ID + " integer primary key,"
			+ KEY_TITLE + " text not null,"
			+ KEY_DETAILS + " text,"
			+ KEY_HDEB + " text not null,"
			+ KEY_HFIN + " text not null,"
			+ KEY_CID + " integer not null,"
			+ KEY_TYPE_ID + " integer not null,"
			+ KEY_DETAILS_ID + " integer not null,"
			+ KEY_ALL_DAY + " integer not null);";

	private String[] allAttr = new String[]{
			KEY_ID, 
			KEY_DETAILS, 
			KEY_HDEB, 
			KEY_HFIN, 
			KEY_TITLE, 
			KEY_CID, 
			KEY_TYPE_ID, 
			KEY_DETAILS_ID, 
			KEY_ALL_DAY};

	public EventBaseRepository(Context context) {
		super(context);
	}

	public List<EventBaseBean> convertCursorToListObject(Cursor c) {
		List<EventBaseBean> liste = new ArrayList<EventBaseBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			EventBaseBean event = convertCursorToObject(c);
			liste.add(event);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public EventBaseBean convertCursorToObject(Cursor c) {
		EventBaseBean event = new EventBaseBean();
		event.setId(c.getInt(KEY_INDEX_ID));
		event.setCid(c.getInt(KEY_INDEX_CID));
		event.setTitle(c.getString(KEY_INDEX_TITLE));
		event.sethDeb(DateUtil.getDateFromISO(c.getString(KEY_INDEX_HDEB)));
		event.sethFin(DateUtil.getDateFromISO(c.getString(KEY_INDEX_HFIN)));
		event.setDetails(c.getString(KEY_INDEX_DETAILS));
		event.setDetailsId(c.getInt(KEY_INDEX_DETAILS_ID));
		event.setTypeId(c.getInt(KEY_INDEX_TYPE_ID));
		event.setAllDay(c.getInt(KEY_INDEX_ALL_DAY) == 1);
		return event;
	}

	public EventBaseBean convertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		EventBaseBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertEvent(EventBaseBean event){
		ContentValues initialValues = new ContentValues();
		if(event.getId() != -1){
			initialValues.put(KEY_ID, event.getId());
		}
		initialValues.put(KEY_TITLE, event.getTitle());
		initialValues.put(KEY_DETAILS, event.getDetails());
		initialValues.put(KEY_HDEB, DateUtil.getTimeInIso(event.gethDeb()));
		initialValues.put(KEY_HFIN, DateUtil.getTimeInIso(event.gethFin()));
		initialValues.put(KEY_CID, event.getCid());
		initialValues.put(KEY_DETAILS_ID, event.getDetailsId());
		initialValues.put(KEY_TYPE_ID, event.getTypeId());
		initialValues.put(KEY_ALL_DAY, event.isAllDay());
		open();
		long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public long updateEvent(EventBaseBean event){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, event.getTitle());
		initialValues.put(KEY_DETAILS, event.getDetails());
		initialValues.put(KEY_HDEB, DateUtil.getTimeInIso(event.gethDeb()));
		initialValues.put(KEY_HFIN, DateUtil.getTimeInIso(event.gethFin()));
		initialValues.put(KEY_CID, event.getCid());
		initialValues.put(KEY_DETAILS_ID, event.getDetailsId());
		initialValues.put(KEY_TYPE_ID, event.getTypeId());
		initialValues.put(KEY_ALL_DAY, event.isAllDay());
		long id = event.getId();
		open();
		long res = this.db.update(DATABASE_TABLE, initialValues, KEY_ID + "=?", new String[] { "" + id});
		close();
		return res;
	}

	public boolean deleteEvent(long rowId) {
		open();
		boolean res = this.db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public EventBaseBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		EventBaseBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public List<EventBaseBean> getAllEvent() {
		open();
		CompteRepository compteRepo = new CompteRepository(context);
		List<CompteBean> comptes = compteRepo.getVisibleCompteByUid(PreferencesUtil.getCurrentUid(context));
		ArrayList<EventBaseBean> res = new ArrayList<EventBaseBean>();
		for (CompteBean compteBean : comptes) {
			Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_CID + " =" + compteBean.getId(), null, null, null,  "" + KEY_INDEX_HDEB);
			res.addAll(convertCursorToListObject(c));
		}	
		close();
		return res;
	}

	public List<EventBaseBean> getAllEvents(Calendar calDeb, Calendar calFin) {
		String isoDeb = DateUtil.getTimeInIso(calDeb);
		String isoFin = DateUtil.getTimeInIso(calFin);
		open();
		CompteRepository compteRepo = new CompteRepository(context);
		List<CompteBean> comptes = compteRepo.getVisibleCompteByUid(PreferencesUtil.getCurrentUid(context));
		ArrayList<EventBaseBean> res = new ArrayList<EventBaseBean>();
		for (CompteBean compteBean : comptes) {
			Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_HDEB + " <= Datetime('"+ isoFin +"') AND " + KEY_HFIN + " >= Datetime('"+ isoDeb +"') AND " + KEY_CID + " =" + compteBean.getId(), null, null, null,  "" + KEY_INDEX_HDEB);
			res.addAll(convertCursorToListObject(c));
		}				
		close();
		return res;
	}

	public List<EventBaseBean> getAllNextFromNow() {
		open();
		CompteRepository compteRepo = new CompteRepository(context);
		List<CompteBean> comptes = compteRepo.getVisibleCompteByUid(PreferencesUtil.getCurrentUid(context));
		ArrayList<EventBaseBean> res = new ArrayList<EventBaseBean>();
		for (CompteBean compteBean : comptes) {
			Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_HFIN + " >= Datetime('now')  AND " + KEY_CID + " =" + compteBean.getId(), null, null, null,  "" + KEY_INDEX_HDEB);
			res.addAll(convertCursorToListObject(c));
		}
		close();
		return res;
	}
}

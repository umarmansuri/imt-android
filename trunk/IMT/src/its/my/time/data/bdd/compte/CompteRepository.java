package its.my.time.data.bdd.compte;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CompteRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_TITLE = 1;
	public static final int KEY_INDEX_COLOR = 2;
	public static final int KEY_INDEX_TYPE = 3;
	public static final int KEY_INDEX_SHOWED = 4;
	public static final int KEY_INDEX_UID = 5;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_TITLE = "KEY_TITLE";
	public static final String KEY_COLOR = "KEY_COLOR";
	public static final String KEY_TYPE = "KEY_TYPE";
	public static final String KEY_SHOWED = "KEY_SHOWED";
	public static final String KEY_UID = "KEY_UID";

	public static final String DATABASE_TABLE = "compte";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " INTEGER primary key autoincrement," + KEY_TITLE
			+ " TEXT not null," + KEY_COLOR + " INTEGER not null," + KEY_TYPE
			+ " INTEGER not null," + KEY_SHOWED + " INTEGER not null,"
			+ KEY_UID + " INTEGER not null);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_TITLE,
			KEY_COLOR, KEY_TYPE, KEY_SHOWED, KEY_UID };

	public CompteRepository(Context context) {
		super(context);
	}

	public List<CompteBean> convertCursorToListObject(Cursor c) {
		final List<CompteBean> liste = new ArrayList<CompteBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final CompteBean compte = convertCursorToObject(c);
			liste.add(compte);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public CompteBean convertCursorToObject(Cursor c) {
		final CompteBean compte = new CompteBean();
		compte.setId(c.getInt(KEY_INDEX_ID));
		compte.setTitle(c.getString(KEY_INDEX_TITLE));
		compte.setType(c.getInt(KEY_INDEX_TYPE));
		compte.setColor(c.getInt(KEY_INDEX_COLOR));
		compte.setUid(c.getInt(KEY_INDEX_UID));
		compte.setShowed(c.getInt(KEY_INDEX_SHOWED) == 1);
		return compte;
	}

	public CompteBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final CompteBean event = convertCursorToObject(c);
		c.close();
		return event;
	}

	public long insertCompte(CompteBean compte) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, compte.getTitle());
		initialValues.put(KEY_COLOR, compte.getColor());
		initialValues.put(KEY_TYPE, compte.getType());
		initialValues.put(KEY_UID, compte.getUid());
		initialValues.put(KEY_SHOWED, true);
		open();
		final int res = (int) this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		if(res != -1) {
			compte.setId(res);
			compteAdded(compte);
		}
		return res;
	}

	public int update(CompteBean compte) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, compte.getTitle());
		initialValues.put(KEY_COLOR, compte.getColor());
		initialValues.put(KEY_TYPE, compte.getType());
		initialValues.put(KEY_UID, compte.getUid());
		if (compte.isShowed()) {
			initialValues.put(KEY_SHOWED, 1);
		} else {
			initialValues.put(KEY_SHOWED, 0);
		}
		open();
		final int nbRow = this.db.update(DATABASE_TABLE, initialValues, KEY_ID+ "=?", new String[] { "" + compte.getId() });
		close();
		if(nbRow > 0) {
			compteUpdated(compte);
		}
		return nbRow;
	}

	public boolean deleteCompte(CompteBean compte) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,KEY_ID + "=" + compte.getId(), null) > 0;
		close();
		if(res == true) {
			compteUpdated(compte);
		}
		return res;
	}

	public List<CompteBean> getAllCompteByUid(long uid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_UID
				+ "=?", new String[] { "" + uid }, null, null, null);
		final List<CompteBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public List<CompteBean> getVisibleCompteByUid(long uid) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_UID
				+ "=? AND " + KEY_SHOWED + " =?", new String[] { "" + uid,
				String.valueOf(1) }, null, null, null);
		final List<CompteBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public CompteBean getById(long id) {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_ID
				+ "=?", new String[] { "" + id }, null, null, null);
		final CompteBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	private void compteAdded(CompteBean compte) {
		for (OnCompteChangedListener listener: onCompteChangedListeners) {
			listener.onCompteAdded(compte);
		}
	}

	private void compteRemoved(CompteBean compte) {
		for (OnCompteChangedListener listener: onCompteChangedListeners) {
			listener.onCompteRemoved(compte);
		}
	}

	private void compteUpdated(CompteBean compte) {
		for (OnCompteChangedListener listener: onCompteChangedListeners) {
			listener.onCompteUpdated(compte);
		}
	}


	private static List<OnCompteChangedListener> onCompteChangedListeners = new ArrayList<CompteRepository.OnCompteChangedListener>();
	public interface OnCompteChangedListener {
		public void onCompteAdded(CompteBean compte);
		public void onCompteUpdated(CompteBean compte);
		public void onCompteRemoved(CompteBean compte);
	}
	
	public void addOnCompteCHangedListener(OnCompteChangedListener listener) {
		onCompteChangedListeners.add(listener);
	}
	public void removeOnCompteChangedListener(OnCompteChangedListener listener) {
		onCompteChangedListeners.remove(listener);
	}
	
}

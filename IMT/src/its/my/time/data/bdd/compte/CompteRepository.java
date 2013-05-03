package its.my.time.data.bdd.compte;

import its.my.time.data.bdd.base.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class CompteRepository extends BaseRepository<CompteBean> {

	public CompteRepository(Context context) {
		super(context, CompteBean.class);
	}

	@Override
	public String getTableName() {
		return "compte";
	}

	public List<CompteBean> getAllByUid(long uid) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "uid"
				+ "=?", new String[] { "" + uid }, null, null, null);
		List<CompteBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public List<CompteBean> getVisibleCompteByUid(long currentUid) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "isShowed=?", new String[] { String.valueOf(1)}, null, null, null);
		List<CompteBean> res = convertCursorToListObject(c);
		close();
		return res;

	}


	protected void objectAdded(CompteBean object) {
		for (OnObjectChangedListener<CompteBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(CompteBean object) {
		for (OnObjectChangedListener<CompteBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(CompteBean object) {
		for (OnObjectChangedListener<CompteBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<CompteBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<CompteBean>>();


	public void addOnObjectChangedListener(OnObjectChangedListener<CompteBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<CompteBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
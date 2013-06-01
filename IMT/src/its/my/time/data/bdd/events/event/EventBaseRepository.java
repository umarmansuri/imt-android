
package its.my.time.data.bdd.events.event;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class EventBaseRepository extends BaseRepository<EventBaseBean>{

	public EventBaseRepository(Context context) {
		super(context, EventBaseBean.class);
	}

	@Override
	public String getTableName() {
		return "eventBase";
	}

	public List<EventBaseBean> getAllEvent() {
		open();
		final CompteRepository compteRepo = new CompteRepository(this.context);
		final List<CompteBean> comptes = compteRepo.getVisibleCompteByUid(PreferencesUtil.getCurrentUid());
		final ArrayList<EventBaseBean> res = new ArrayList<EventBaseBean>();
		for (final CompteBean compteBean : comptes) {
			final Cursor c = this.db.query(getTableName(), getAllAttr(),
					"cid =" + compteBean.getId(), null, null, null, "hDeb");
			res.addAll(convertCursorToListObject(c));
		}
		close();
		return res;
	}

	public List<EventBaseBean> getAllEvents(Calendar calDeb, Calendar calFin) {
		final String isoDeb = DateUtil.getTimeInIso(calDeb);
		final String isoFin = DateUtil.getTimeInIso(calFin);
		open();
		final ArrayList<EventBaseBean> res = new ArrayList<EventBaseBean>();
		final Cursor c = this.db.query(getTableName(), getAllAttr(),
				"hDeb <= Datetime('" + isoFin + "') AND hFin  >= Datetime('" + isoDeb + "')", null, null, null, "hDeb");
		res.addAll(convertCursorToListObject(c));

		close();
		return res;
	}

	public List<EventBaseBean> getAllNextFromNow(long uid) {
		open();
		final CompteRepository compteRepo = new CompteRepository(this.context);
		final List<CompteBean> comptes = compteRepo.getVisibleCompteByUid(uid);
		final ArrayList<EventBaseBean> res = new ArrayList<EventBaseBean>();
		for (final CompteBean compteBean : comptes) {
			final Cursor c = this.db.query(getTableName(), getAllAttr(),
					"hFin >= Datetime('now')  AND cid ="
							+ compteBean.getId(), null, null, null, "hDeb");
			res.addAll(convertCursorToListObject(c));
		}
		close();
		return res;
	}


	protected void objectAdded(EventBaseBean object) {
		for (OnObjectChangedListener<EventBaseBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(EventBaseBean object) {
		for (OnObjectChangedListener<EventBaseBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(EventBaseBean object) {
		for (OnObjectChangedListener<EventBaseBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<EventBaseBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<EventBaseBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<EventBaseBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<EventBaseBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
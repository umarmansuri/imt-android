package its.my.time.data.bdd.events.plugins.participation;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class ParticipationRepository extends PluginBaseRepository<ParticipationBean> {

	public ParticipationRepository(Context context) {
		super(context, ParticipationBean.class);
	}

	@Override
	public String getTableName() {
		return "participation";
	}


	protected void objectAdded(ParticipationBean object) {
		for (OnObjectChangedListener<ParticipationBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(ParticipationBean object) {
		for (OnObjectChangedListener<ParticipationBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(ParticipationBean object) {
		for (OnObjectChangedListener<ParticipationBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}

	private static List<OnObjectChangedListener<ParticipationBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<ParticipationBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<ParticipationBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<ParticipationBean> listener) {
		onObjectChangedListeners.remove(listener);
	}

	public ParticipationBean getByUidEid(int eid, long uid) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), "uid=? AND eid=?", 
				new String[] { String.valueOf(uid), String.valueOf(eid) }, null, null, null);
		ParticipationBean res = convertCursorToOneObject(c);
		close();
		return res;
	}
}

package its.my.time.data.bdd.events.plugins.note;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class NoteRepository extends PluginBaseRepository<NoteBean> {
	
	public NoteRepository(Context context) {
		super(context, NoteBean.class);
	}

	@Override
	public String getTableName() {
		return "note";
	}

	public NoteBean getByUidEid(int eid, long uid) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), 
				"eid =? AND uid=?", new String[] { String.valueOf(eid) , String.valueOf(uid)}, null, null, null);
		NoteBean res = convertCursorToOneObject(c);
		close();
		return res;
	}


	protected void objectAdded(NoteBean object) {
		for (OnObjectChangedListener<NoteBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(NoteBean object) {
		for (OnObjectChangedListener<NoteBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(NoteBean object) {
		for (OnObjectChangedListener<NoteBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<NoteBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<NoteBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<NoteBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<NoteBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
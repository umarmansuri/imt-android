package its.my.time.data.bdd.events.plugins.note;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class NoteRepository extends PluginBaseRepository<NoteBean> {
	
	public NoteRepository(Context context) {
		super(context, NoteBean.class);
	}

	@Override
	public String getTableName() {
		return "note";
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
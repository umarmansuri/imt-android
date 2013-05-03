package its.my.time.data.bdd.events.plugins.pj;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class PjRepository extends PluginBaseRepository<PjBean> {

	public PjRepository(Context context) {
		super(context, PjBean.class);
	}

	@Override
	public String getTableName() {
		return "pj";
	}


	protected void objectAdded(PjBean object) {
		for (OnObjectChangedListener<PjBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(PjBean object) {
		for (OnObjectChangedListener<PjBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(PjBean object) {
		for (OnObjectChangedListener<PjBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<PjBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<PjBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<PjBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<PjBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}

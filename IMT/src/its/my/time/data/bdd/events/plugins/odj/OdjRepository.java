package its.my.time.data.bdd.events.plugins.odj;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class OdjRepository extends PluginBaseRepository<OdjBean> {

	public OdjRepository(Context context) {
		super(context, OdjBean.class);
	}

	@Override
	public String getTableName() {
		return "odj";
	}


	protected void objectAdded(OdjBean object) {
		for (OnObjectChangedListener<OdjBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(OdjBean object) {
		for (OnObjectChangedListener<OdjBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(OdjBean object) {
		for (OnObjectChangedListener<OdjBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<OdjBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<OdjBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<OdjBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<OdjBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
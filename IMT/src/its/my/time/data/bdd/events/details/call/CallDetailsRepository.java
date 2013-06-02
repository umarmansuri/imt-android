package its.my.time.data.bdd.events.details.call;

import its.my.time.data.bdd.events.details.BaseDetailsRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class CallDetailsRepository extends BaseDetailsRepository<CallDetailsBean> {

	public CallDetailsRepository(Context context) {
		super(context, CallDetailsBean.class);
	}

	@Override
	public String getTableName() {
		return "call_details";
	}

	protected void objectAdded(CallDetailsBean object) {
		for (OnObjectChangedListener<CallDetailsBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(CallDetailsBean object) {
		for (OnObjectChangedListener<CallDetailsBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(CallDetailsBean object) {
		for (OnObjectChangedListener<CallDetailsBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<CallDetailsBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<CallDetailsBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<CallDetailsBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<CallDetailsBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
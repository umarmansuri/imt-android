package its.my.time.data.bdd.events.plugins.participant;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ParticipantRepository extends PluginBaseRepository<ParticipantBean>{

	public ParticipantRepository(Context context) {
		super(context, ParticipantBean.class);
	}

	@Override
	public String getTableName() {
		return "participants";
	}


	protected void objectAdded(ParticipantBean object) {
		for (OnObjectChangedListener<ParticipantBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(ParticipantBean object) {
		for (OnObjectChangedListener<ParticipantBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(ParticipantBean object) {
		for (OnObjectChangedListener<ParticipantBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<ParticipantBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<ParticipantBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<ParticipantBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<ParticipantBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
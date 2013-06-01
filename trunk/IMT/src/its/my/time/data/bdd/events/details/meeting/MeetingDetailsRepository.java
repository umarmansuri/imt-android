package its.my.time.data.bdd.events.details.meeting;

import its.my.time.data.bdd.events.details.DetailsBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class MeetingDetailsRepository extends DetailsBaseRepository<MeetingDetailsBean> {

	public MeetingDetailsRepository(Context context) {
		super(context, MeetingDetailsBean.class);
	}

	@Override
	public String getTableName() {
		return "meeting_details";
	}

	protected void objectAdded(MeetingDetailsBean object) {
		for (OnObjectChangedListener<MeetingDetailsBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(MeetingDetailsBean object) {
		for (OnObjectChangedListener<MeetingDetailsBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(MeetingDetailsBean object) {
		for (OnObjectChangedListener<MeetingDetailsBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<MeetingDetailsBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<MeetingDetailsBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<MeetingDetailsBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<MeetingDetailsBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
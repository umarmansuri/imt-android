package its.my.time.pages.editable.events.meeting.details;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.EventTypes;
import android.view.View;

public class MeetingDetailsFragment extends DetailsFragment {


	public MeetingDetailsFragment(EventBaseBean event) {
		super(event, EventTypes.TYPE_MEETING);
	}
	
	public View getCustomView() {
		return null;
	}

	@Override
	public String getTitle() {
		return "Réunion";
	}
	
	@Override
	public void launchEdit() {
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		super.launchSave();
	}
}
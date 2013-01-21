package its.my.time.pages.editable.events.meeting.details;

import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.EventTypes;

public class MeetingDetailsFragment extends DetailsFragment {

	@Override
	public String getTitle() {
		return "Réunion";
	}


	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(EventTypes.TYPE_MEETING);
		super.launchSave();
	}
}
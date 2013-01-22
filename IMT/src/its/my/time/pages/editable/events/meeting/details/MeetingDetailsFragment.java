package its.my.time.pages.editable.events.meeting.details;

import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.Types;

public class MeetingDetailsFragment extends DetailsFragment {

	@Override
	public String getTitle() {
		return "Réunion";
	}


	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(Types.Event.MEETING);
		super.launchSave();
	}
}
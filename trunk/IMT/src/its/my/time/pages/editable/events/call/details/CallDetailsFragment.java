package its.my.time.pages.editable.events.call.details;

import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.Types;

public class CallDetailsFragment extends DetailsFragment {

	@Override
	public String getTitle() {
		return "Appel";
	}

	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(Types.Event.CALL);
		super.launchSave();
	}

}
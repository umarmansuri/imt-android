package its.my.time.pages.editable.events.task.details;

import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.EventTypes;

public class TaskDetailsFragment extends DetailsFragment {

	@Override
	public String getTitle() {
		return "Tâche";
	}

	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(EventTypes.TYPE_TASK);
		super.launchSave();
	}

}
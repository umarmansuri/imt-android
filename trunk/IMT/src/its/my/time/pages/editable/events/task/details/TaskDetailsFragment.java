package its.my.time.pages.editable.events.task.details;

import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.Types;

public class TaskDetailsFragment extends DetailsFragment {

	@Override
	public String getTitle() {
		return "Tâche";
	}

	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(Types.Event.TASK);
		super.launchSave();
	}
 
}
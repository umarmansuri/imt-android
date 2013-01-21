package its.my.time.pages.editable.events.task;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;
import its.my.time.pages.editable.events.task.details.TaskDetailsFragment;

import java.util.ArrayList;

public class TaskActivity extends BaseEventActivity {

	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvelle tâche";
	}

	@Override
	public ArrayList<BasePluginFragment> getPages() {
		final ArrayList<BasePluginFragment> fragments = new ArrayList<BasePluginFragment>();

		BasePluginFragment fragment = new TaskDetailsFragment(this.event);
		fragments.add(fragment);
		fragment = new ParticipantsFragment(this.event.getId());
		fragments.add(fragment);
		fragment = new CommentairesFragment(this.event.getId());
		fragments.add(fragment);
		fragment = new PjFragment(this.event.getId());
		fragments.add(fragment);
		return fragments;
	}

}

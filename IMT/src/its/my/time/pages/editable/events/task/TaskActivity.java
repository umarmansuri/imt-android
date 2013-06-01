package its.my.time.pages.editable.events.task;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.PluginFragment;
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
	public ArrayList<PluginFragment> getPages() {
		final ArrayList<PluginFragment> fragments = new ArrayList<PluginFragment>();

		BasePluginFragment fragment = new TaskDetailsFragment();
		fragments.add(fragment);
		fragment = new ParticipantsFragment();
		fragments.add(fragment);
		fragment = new CommentairesFragment();
		fragments.add(fragment);
		fragment = new PjFragment();
		fragments.add(fragment);
		return fragments;
	}
	
	public boolean isUpdatable() {
		return false;
	}
}

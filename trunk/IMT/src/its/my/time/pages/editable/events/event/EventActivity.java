package its.my.time.pages.editable.events.event;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.PluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;
import its.my.time.util.Types;

import java.util.ArrayList;

public class EventActivity extends BaseEventActivity {

	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel évènement";
	}

	@Override
	public ArrayList<PluginFragment> getPages() {
		final ArrayList<PluginFragment> fragments = new ArrayList<PluginFragment>();
		BasePluginFragment fragment = new DetailsFragment(Types.Event.BASE);
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

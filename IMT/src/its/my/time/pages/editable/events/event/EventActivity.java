package its.my.time.pages.editable.events.event;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;
import its.my.time.util.EventTypes;

import java.util.ArrayList;

public class EventActivity extends BaseEventActivity {


	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel évènement";
	}

	@Override
	public ArrayList<BasePluginFragment> getPages() {
		ArrayList<BasePluginFragment> fragments = new ArrayList<BasePluginFragment>();
		BasePluginFragment fragment = new DetailsFragment(event, EventTypes.TYPE_BASE);
		fragments.add(fragment);
		fragment = new ParticipantsFragment(event.getId());
		fragments.add(fragment);
		fragment = new CommentairesFragment(event.getId());
		fragments.add(fragment);
		fragment = new PjFragment(event.getId());
		fragments.add(fragment);
		return fragments;
	}



}

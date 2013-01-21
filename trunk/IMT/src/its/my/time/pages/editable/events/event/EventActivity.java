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
		final ArrayList<BasePluginFragment> fragments = new ArrayList<BasePluginFragment>();
		BasePluginFragment fragment = new DetailsFragment(this.event,
				EventTypes.TYPE_BASE);
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

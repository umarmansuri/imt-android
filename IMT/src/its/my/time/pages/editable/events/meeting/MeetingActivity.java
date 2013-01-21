package its.my.time.pages.editable.events.meeting;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.meeting.details.MeetingDetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.odj.OdjFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;

import java.util.ArrayList;

public class MeetingActivity extends BaseEventActivity {

	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel appel";
	}

	@Override
	public ArrayList<BasePluginFragment> getPages() {
		final ArrayList<BasePluginFragment> fragments = new ArrayList<BasePluginFragment>();

		BasePluginFragment fragment = new MeetingDetailsFragment(this.event);
		fragments.add(fragment);
		fragment = new OdjFragment(this.event.getId());
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

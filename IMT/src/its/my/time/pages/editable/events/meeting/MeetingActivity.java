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
		ArrayList<BasePluginFragment> fragments = new ArrayList<BasePluginFragment>();
		
		BasePluginFragment fragment = new MeetingDetailsFragment(event);
		fragments.add(fragment);
		fragment = new OdjFragment(event.getId());
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

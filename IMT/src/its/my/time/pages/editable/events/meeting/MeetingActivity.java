package its.my.time.pages.editable.events.meeting;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.meeting.details.MeetingDetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.note.NoteFragment;
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

		BasePluginFragment fragment = new MeetingDetailsFragment();
		fragments.add(fragment);
		fragment = new OdjFragment();
		fragments.add(fragment);
		fragment = new ParticipantsFragment();
		fragments.add(fragment);
		fragment = new CommentairesFragment();
		fragments.add(fragment);
		fragment = new PjFragment();
		fragments.add(fragment);
		fragment = new NoteFragment();
		fragments.add(fragment);
		return fragments;
	}

}

package its.my.time.pages.editable.events.call;

import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.call.details.CallDetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;

import java.util.ArrayList;

public class CallActivity extends BaseEventActivity {

	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel appel";
	}

	@Override
	public ArrayList<BasePluginFragment> getPages() {
		final ArrayList<BasePluginFragment> fragments = new ArrayList<BasePluginFragment>();
		BasePluginFragment fragment = new CallDetailsFragment(this.event);
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

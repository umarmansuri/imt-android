package its.my.time.pages.editable.events.call;

import its.my.time.data.bdd.events.details.call.CallDetailsBean;
import its.my.time.data.bdd.events.details.call.CallDetailsRepository;
import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.call.details.CallDetailsFragment;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.pages.editable.events.plugins.PluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;

import java.util.ArrayList;

import android.os.Bundle;

public class CallActivity extends BaseEventActivity {

	private CallDetailsRepository callDetailsRepo;
	private CallDetailsBean callDetails;

	@Override
	public void onCreate(Bundle savedInstance) {
		callDetailsRepo = new CallDetailsRepository(this);
		super.onCreate(savedInstance);
	}
	
	public CallDetailsBean getCallDetails() {
		return callDetails;
	}
	
	public CallDetailsRepository getCallDetailsRepo() {
		return callDetailsRepo;
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel appel";
	}

	@Override
	public ArrayList<PluginFragment> getPages() {

		callDetails = callDetailsRepo.getByEid(getEvent().getId());
		if(callDetails == null) {
			callDetails = new CallDetailsBean();
			callDetails.setEid(getEvent().getId());
		}
		
		final ArrayList<PluginFragment> fragments = new ArrayList<PluginFragment>();
		BasePluginFragment fragment = new CallDetailsFragment();
		fragments.add(fragment);
		fragment = new ParticipantsFragment();
		fragments.add(fragment);
		fragment = new CommentairesFragment();
		fragments.add(fragment);
		fragment = new PjFragment();
		fragments.add(fragment);
		return fragments;
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}

}

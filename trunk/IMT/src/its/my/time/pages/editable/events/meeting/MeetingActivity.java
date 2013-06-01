package its.my.time.pages.editable.events.meeting;

import its.my.time.data.bdd.events.details.meeting.MeetingDetailsBean;
import its.my.time.data.bdd.events.details.meeting.MeetingDetailsRepository;
import its.my.time.pages.editable.events.BaseEventActivity;
import its.my.time.pages.editable.events.meeting.details.MeetingDetailsFragment;
import its.my.time.pages.editable.events.plugins.PluginFragment;
import its.my.time.pages.editable.events.plugins.commentaires.CommentairesFragment;
import its.my.time.pages.editable.events.plugins.map.MapFragment;
import its.my.time.pages.editable.events.plugins.note.NoteFragment;
import its.my.time.pages.editable.events.plugins.odj.OdjFragment;
import its.my.time.pages.editable.events.plugins.participants.ParticipantsFragment;
import its.my.time.pages.editable.events.plugins.pj.PjFragment;

import java.util.ArrayList;

import android.os.Bundle;

public class MeetingActivity extends BaseEventActivity {


	private MeetingDetailsRepository meetingDetailsRepo;
	private MeetingDetailsBean meetingDetails;

	
	@Override
	public void onCreate(Bundle savedInstance) {
		meetingDetailsRepo = new MeetingDetailsRepository(this);
		super.onCreate(savedInstance);
	}
	
	public MeetingDetailsBean getMeetingDetails() {
		return meetingDetails;
	}
	
	public MeetingDetailsRepository getMeetingDetailsRepo() {
		return meetingDetailsRepo;
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvelle réunion";
	}

	@Override
	public ArrayList<PluginFragment> getPages() {
		meetingDetails = meetingDetailsRepo.getByEid(getEvent().getId());
		if(meetingDetails == null) {
			meetingDetails = new MeetingDetailsBean();
			meetingDetails.setEid(getEvent().getId());
		}

		final ArrayList<PluginFragment> fragments = new ArrayList<PluginFragment>();

		PluginFragment fragment = new MeetingDetailsFragment();
		fragments.add(fragment);
		fragment = new MapFragment();
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

	public boolean isUpdatable() {
		return false;
	}
	
}

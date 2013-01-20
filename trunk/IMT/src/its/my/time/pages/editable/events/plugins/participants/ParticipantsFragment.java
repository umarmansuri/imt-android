package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.pages.editable.events.plugins.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ParticipantsFragment extends BaseFragment {

	private int eventId;

	public ParticipantsFragment() {
		this(-1);
	}
	
	public ParticipantsFragment(int eventId) {
		super();
		this.eventId = eventId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_participant, null);
		ListView mListParticipant = (ListView)mView.findViewById(R.id.event_participants_liste);
		mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(), eventId));
		
		return mView;
	}

	@Override
	public void launchEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}
}

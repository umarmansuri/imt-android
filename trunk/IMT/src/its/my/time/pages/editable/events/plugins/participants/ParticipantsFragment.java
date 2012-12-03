package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;

public class ParticipantsFragment extends SherlockFragment {

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
}

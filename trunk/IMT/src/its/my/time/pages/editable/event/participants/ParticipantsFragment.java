package its.my.time.pages.editable.event.participants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class ParticipantsFragment extends SherlockFragment {

	private View mParticipantsView;

	public ParticipantsFragment(int idEvent) {
		mParticipantsView = null;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mParticipantsView;
}

}

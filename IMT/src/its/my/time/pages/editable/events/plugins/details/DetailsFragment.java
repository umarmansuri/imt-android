package its.my.time.pages.editable.events.plugins.details;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.pages.editable.events.plugins.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class DetailsFragment extends BaseFragment {

	private DetailsView mDetailsView;
	private EventBaseBean event;

	public DetailsFragment(EventBaseBean event) {
		this.event = event;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDetailsView = new DetailsView(getActivity(), event);
		return mDetailsView;
	}

	@Override
	public void launchEdit() {
		Toast.makeText(getActivity(), "Edit is LAUNCHED!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
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
		return true;
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
package its.my.time.pages.editable.events.meeting.details;

import its.my.time.R;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.util.Types;
import android.view.View;
import android.widget.EditText;

public class MeetingDetailsFragment extends DetailsFragment {

	private static final String KEY_BUNDLE_ADDRESS = "KEY_BUNDLE_ADDRESS";
	private EditText mEditAddress;
	
	@Override
	public String getTitle() {
		return "Réunion";
	}

	@Override
	protected View getCustomView() {
		View v = View.inflate(getActivity(), R.layout.activity_event_meeting_details, null);
		mEditAddress = (EditText)v.findViewById(R.id.activity_event_details_text_lieu);
		return v;
	}

	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(Types.Event.MEETING);
		super.launchSave();
		getParentActivity().getMeetingDetails().setEid(getParentActivity().getEvent().getId());
		getParentActivity().getMeetingDetails().setAddress(mEditAddress.getText().toString());
		if(getParentActivity().getMeetingDetails().getId() <= 0) {
			long id = getParentActivity().getMeetingDetailsRepo().insert(getParentActivity().getMeetingDetails());
			getParentActivity().getMeetingDetails().setId((int)id);
		} else {
			getParentActivity().getMeetingDetailsRepo().update(getParentActivity().getMeetingDetails());
		}
	}
	
	public void initialiseValuesFromEvent() {
		mEditAddress.setText(getParentActivity().getMeetingDetails().getAddress());
		super.initialiseValuesFromEvent();
	}

	public void initialiseValueFromInstance() {
		mEditAddress.setText(state.getString(KEY_BUNDLE_ADDRESS));
		super.initialiseValueFromInstance();
	}

	@Override
	public MeetingActivity getParentActivity() {
		return (MeetingActivity)getActivity();
	}
}
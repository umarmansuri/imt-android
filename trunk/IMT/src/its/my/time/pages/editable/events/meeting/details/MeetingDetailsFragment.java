package its.my.time.pages.editable.events.meeting.details;

import its.my.time.R;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.util.Types;
import android.view.View;
import android.widget.EditText;

public class MeetingDetailsFragment extends DetailsFragment {

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
	
	public void refresh() {
		super.refresh();
		mEditAddress.setText(getParentActivity().getMeetingDetails().getAddress());
	}

	@Override
	public MeetingActivity getParentActivity() {
		return (MeetingActivity)getActivity();
	}
}
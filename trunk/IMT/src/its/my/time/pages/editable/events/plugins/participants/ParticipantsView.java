package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.contactsOld.ContactBean;
import its.my.time.data.bdd.contactsOld.ContactInfo.ContactInfoBean;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class ParticipantsView extends EditableLittleView {

	private ContactInfoBean contactInfo;
	private ContactBean contact;

	public ParticipantsView(Context context, 
			ContactBean contact, 
			ContactInfoBean contactInfo, 
			boolean isInEditMode) {
		super(context, isInEditMode);
		inflate(context, R.layout.activity_event_participant_little, this);
		setBackgroundColor(Color.WHITE);
		this.contact = contact;
		this.contactInfo = contactInfo;
		initialiseDetails();
	}

	private void initialiseDetails() {
		((TextView) findViewById(R.id.event_participant_title)).setText(contact.getNom() + " " + contact.getPrenom());
		((TextView) findViewById(R.id.event_participant_details)).setText(contactInfo.getValue());
	}
}

package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.contacts.ContactBean;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoBean;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fonts.mooncake.MooncakeIcone;

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
		super.initialiseValues();
		((TextView) findViewById(R.id.event_participant_title)).setText(contact.getNom());
		((TextView) findViewById(R.id.event_participant_details)).setText(contactInfo.getValue());
		
		ViewGroup imageParent = (ViewGroup)findViewById(R.id.event_participant_image);
		if(contact.getImage()!= null) {
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setImageBitmap(contact.getImage());

			imageParent.removeAllViews();
			imageParent.addView(imageView);
		} else {
			MooncakeIcone imageView = ((MooncakeIcone)(imageParent).getChildAt(0));
			imageView.setIconeRes(MooncakeIcone.icon_user);
		}
	}
}

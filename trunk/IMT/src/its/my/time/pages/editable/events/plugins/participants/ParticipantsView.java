package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.participation.ParticipationBean;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class ParticipantsView extends EditableLittleView {


	private ParticipationBean participationBean;

	public ParticipantsView(Context context, ParticipationBean participationBean, boolean isEditMode) {
		super(context, isEditMode);
		inflate(context, R.layout.activity_event_participant_little, this);
		setBackgroundColor(Color.WHITE);
		this.participationBean = participationBean;
		initialiseDetails();
	}

	private void initialiseDetails() {
		super.initialiseValues();
		((TextView) findViewById(R.id.event_participant_name)).setText(participationBean.getParticipant());
		
	}
}

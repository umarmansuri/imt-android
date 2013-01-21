package its.my.time.pages.calendar.list;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.util.ActivityUtil;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LittleEventView extends FrameLayout {

	private final EventBaseBean event;

	private final OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ActivityUtil.startEventActivity(getContext(),
					LittleEventView.this.event.getId(),
					LittleEventView.this.event.getTypeId());
		}
	};

	public LittleEventView(Context context, EventBaseBean event) {
		super(context);
		inflate(context, R.layout.activity_calendar_liste_event, this);

		this.event = event;

		initialiseDetails();

		setOnClickListener(this.listener);
	}

	private void initialiseDetails() {
		((TextView) findViewById(R.id.calendar_liste_event_date))
				.setText("Mardi 17 octobre à 17h30");
		((TextView) findViewById(R.id.calendar_liste_event_title))
				.setText(this.event.getTitle());
		((TextView) findViewById(R.id.calendar_liste_event_details))
				.setText(this.event.getDetails());
	}

}

package its.my.time.pages.calendar.list;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LittleEventView extends FrameLayout {

	private EventBean event;
	
	public LittleEventView(Context context, EventBean event) {
		super(context);
		inflate(context, R.layout.activity_calendar_liste_event, this);
		
		this.event = event;
		
		initialiseDetails();
	}

	private void initialiseDetails() {
		((TextView)findViewById(R.id.calendar_liste_event_date)).setText("Mardi 17 octobre à 17h30");
		((TextView)findViewById(R.id.calendar_liste_event_title)).setText(event.getTitle());
		((TextView)findViewById(R.id.calendar_liste_event_details)).setText(event.getDetails());
	}
	
}

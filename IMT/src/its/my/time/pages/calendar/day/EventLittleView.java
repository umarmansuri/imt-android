package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventLittleView extends TextView{

	private EventBean event;

	public EventLittleView(Context context, EventBean event, Calendar day) {
		super(context);
		this.event = event;	
		
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.startEventActivity(getContext(), 1);
			}
		});

		setText("Titre");
		float ligneHeight =  getResources().getDimension(R.dimen.view_day_height_ligne_heure);
		int height = (int) (DateUtil.getNbHeure(event.gethDeb(), event.gethFin(), day) * ligneHeight) ;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
		if(DateUtil.isInDay(event.gethDeb(), day)) {
			params.topMargin = ((int) (event.gethDeb().get(GregorianCalendar.HOUR_OF_DAY)  * ligneHeight + (((float)event.gethDeb().get(GregorianCalendar.MINUTE)/ 60) * ligneHeight)));
		}
		setLayoutParams(params);
		setBackgroundResource(R.drawable.form_activity_day_event_little);
		final float scale = getContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (5.5 * scale + 0.5f);
		setPadding(pixels, pixels, pixels, pixels);
	}
}

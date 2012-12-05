package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.anim.DraggedAnim;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventLittleView extends FrameLayout{

	private EventBaseBean event;
	private View mainView;
	private View mBottom;
	private float ligneHeight;
	private TextView mTitle;
	private TextView mContent;
	private int height;

	public EventLittleView(Context context, EventBaseBean event, Calendar day) {
		super(context);
		this.event = event;
		
		mainView = inflate(getContext(), R.layout.activity_calendar_day_event_little, null);
		addView(mainView);
		
		mTitle = (TextView)findViewById(R.id.activity_calendar_day_event_little_hour);
		mTitle.setText(DateUtil.getHourLabel(event.gethDeb(), event.gethFin()));
		mContent = (TextView)findViewById(R.id.activity_calendar_day_event_little_content);
		mContent.setText("Titre");
		mBottom = findViewById(R.id.activity_calendar_day_event_little_bottom);
		
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.startEventActivity(getContext(), 1);
			}
		});

		ligneHeight =  getResources().getDimension(R.dimen.view_day_height_ligne_heure);
		height = (int) (DateUtil.getNbHeure(event.gethDeb(), event.gethFin(), day) * ligneHeight) ;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
		if(DateUtil.isInDay(event.gethDeb(), day)) {
			params.topMargin = ((int) (event.gethDeb().get(GregorianCalendar.HOUR_OF_DAY)  * ligneHeight + (((float)event.gethDeb().get(GregorianCalendar.MINUTE)/ 60) * ligneHeight)));
		}
		setLayoutParams(params);
	}

	public EventBaseBean getEvent() {
		return event;
	}

	public void setEvent(EventBaseBean event) {
		this.event = event;
	}

	public View getBottomDraggable() {
		return mBottom;
	}

	public void changeDragged(boolean dragged) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
		if(dragged) {
			mainView.getBackground().setAlpha(100);
			DraggedAnim anim = new DraggedAnim(this, -10);
			anim.setDuration(100);
			startAnimation(anim);
		} else {
			mainView.getBackground().setAlpha(255);
			DraggedAnim anim = new DraggedAnim(this, 0);
			anim.setDuration(100);
			startAnimation(anim);
		}
		setLayoutParams(params);
	}

	public void updateFromLayout(RelativeLayout.LayoutParams layout) {
		setLayoutParams(layout);
		
		float nbHeure = layout.height / ligneHeight;
		float hourDeb = layout.topMargin / ligneHeight;

		event.gethDeb().set(Calendar.HOUR_OF_DAY, 0);
		event.gethDeb().set(Calendar.MINUTE, 0);
		event.gethDeb().set(Calendar.SECOND, 0);
		event.gethDeb().add(Calendar.SECOND, (int) (hourDeb * 3600));
		

		event.gethFin().set(Calendar.HOUR_OF_DAY, 0);
		event.gethFin().set(Calendar.MINUTE, 0);
		event.gethFin().set(Calendar.SECOND, 0);
		event.gethFin().add(Calendar.SECOND, (int) ((hourDeb + nbHeure)* 3600));
		
		mTitle.setText(DateUtil.getHourLabel(event.gethDeb(), event.gethFin()));
		
	}	
}

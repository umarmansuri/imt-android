package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.anim.DraggedAnim;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.util.ActivityUtil;
import its.my.time.util.ColorUtil;
import its.my.time.util.DateUtil;

import java.util.Calendar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventLittleView extends FrameLayout {

	private EventBaseBean event;
	private final View mBottom;
	private final float ligneHeight;
	private final TextView mTitle;
	private final TextView mContent;
	private final int height;

	public EventLittleView(Context context, EventBaseBean ev, Calendar day) {
		super(context);
		this.event = ev;

		View mainView = inflate(getContext(),R.layout.activity_calendar_day_event_little, null);
		addView(mainView);

		this.mTitle = (TextView) findViewById(R.id.activity_calendar_day_event_little_hour);
		this.mTitle.setText(DateUtil.getHourLabel(ev.gethDeb(), ev.gethFin()));
		this.mContent = (TextView) findViewById(R.id.activity_calendar_day_event_little_content);
		this.mContent.setText(this.event.getTitle());
		this.mBottom = findViewById(R.id.activity_calendar_day_event_little_bottom);

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityUtil.startEventActivity(getContext(),
						EventLittleView.this.event.getId(),
						EventLittleView.this.event.getTypeId());
			}
		});

		this.ligneHeight = getResources().getDimension(R.dimen.view_day_height_ligne_heure);
		this.height = (int) (DateUtil.getNbHeure(ev.gethDeb(), ev.gethFin(),day) * this.ligneHeight);
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, this.height);
		if (DateUtil.isInDay(ev.gethDeb(), day)) {
			params.topMargin = ((int) (ev.gethDeb().get(Calendar.HOUR_OF_DAY)* this.ligneHeight + (((float) ev.gethDeb().get(Calendar.MINUTE) / 60) * this.ligneHeight)));
		}
		setLayoutParams(params);
		initialiseBackground();
		invalidate();
	}

	private void initialiseBackground() {
		final int color = new CompteRepository(getContext()).getById(this.event.getCid()).getColor();
		
		int colorDarker = ColorUtil.getDarkerColor(color);

		LayerDrawable ld = (LayerDrawable) getResources().getDrawable(R.drawable.form_activity_day_event_little);
		ld = (LayerDrawable) ld.getConstantState().newDrawable();
		
		GradientDrawable dBorder =(GradientDrawable)ld.findDrawableByLayerId(R.id.drawable_day_event_little_border);
		dBorder.setColor(colorDarker);

		GradientDrawable dTop = (GradientDrawable)ld.findDrawableByLayerId(R.id.drawable_day_event_little_top);
		dTop.setColor(colorDarker);

		GradientDrawable dContent = (GradientDrawable)ld.findDrawableByLayerId(R.id.drawable_day_event_little_content);
		dContent.setColor(color);

		setBackground(ld);
	}

	public EventBaseBean getEvent() {
		return this.event;
	}

	public void setEvent(EventBaseBean event) {
		this.event = event;
	}

	public View getBottomDraggable() {
		return this.mBottom;
	}

	public void changeDragged(boolean dragged) {
		final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
		if (dragged) {
			getBackground().setAlpha(100);
		} else {
			getBackground().setAlpha(255);
		}
		setLayoutParams(params);
	}

	public void updateFromLayout(RelativeLayout.LayoutParams layout) {
		setLayoutParams(layout);

		final float nbHeure = layout.height / this.ligneHeight;
		final float hourDeb = layout.topMargin / this.ligneHeight;

		this.event.gethDeb().set(Calendar.HOUR_OF_DAY, 0);
		this.event.gethDeb().set(Calendar.MINUTE, 0);
		this.event.gethDeb().set(Calendar.SECOND, 0);
		this.event.gethDeb().add(Calendar.SECOND, (int) (hourDeb * 3600));

		this.event.gethFin().set(Calendar.HOUR_OF_DAY, 0);
		this.event.gethFin().set(Calendar.MINUTE, 0);
		this.event.gethFin().set(Calendar.SECOND, 0);
		this.event.gethFin().add(Calendar.SECOND,
				(int) ((hourDeb + nbHeure) * 3600));

		this.mTitle.setText(DateUtil.getHourLabel(this.event.gethDeb(),
				this.event.gethFin()));

	}
}
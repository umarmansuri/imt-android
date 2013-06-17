package its.my.time.pages.calendar.day;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ColumnEvent extends RelativeLayout {

	private final ArrayList<EventBaseBean> events = new ArrayList<EventBaseBean>();
	private final ArrayList<EventLittleView> eventViews = new ArrayList<EventLittleView>();

	public ColumnEvent(Context context) {
		super(context);
		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.weight = 1;
		setLayoutParams(params);
		setGravity(Gravity.CENTER_HORIZONTAL);
	}

	/**
	 * Tente d'ajouter l'evenement à la vue
	 * 
	 * @return la vue de l'event si l'évènment a été placé, sinon (si la place
	 *         est deja pise), return null
	 */
	public EventLittleView addEvent(EventBaseBean newEv, Calendar day) {
		for (final EventBaseBean event : this.events) {
			if (EventUtil.isAtSameTime(newEv, event)) {
				return null;
			}
		}
		final EventLittleView view = new EventLittleView(getContext(), newEv,day);
		this.eventViews.add(view);
		this.events.add(newEv);
		addView(view);
		return view;
	}

	public EventLittleView addEvent(EventLittleView view) {
		for (final EventBaseBean event : this.events) {
			if (EventUtil.isAtSameTime(view.getEvent(), event)) {
				return null;
			}
		}
		this.eventViews.add(view);
		this.events.add(view.getEvent());
		addView(view);
		return view;
	}

	public boolean unload(EventLittleView view) {
		if (this.events.contains(view.getEvent())) {
			this.events.remove(view.getEvent());
			removeView(view);
			refreshDrawableState();
			if (this.events.size() == 0) {
				((ViewGroup) getParent()).removeView(this);
			}
			invalidate();
			return true;
		}
		return false;
	}

}
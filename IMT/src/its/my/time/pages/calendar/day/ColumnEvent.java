package its.my.time.pages.calendar.day;

import its.my.time.data.bdd.event.EventBean;
import its.my.time.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class ColumnEvent extends RelativeLayout{

	private ArrayList<EventBean> events = new ArrayList<EventBean>();
	private ArrayList<EventLittleView> eventViews = new ArrayList<EventLittleView>();

	public ColumnEvent(Context context) {
		super(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;
		setLayoutParams(params);

	}

	/**
	 * Tente d'ajouter l'evenement à la vue
	 * @return la vue de l'event si l'évènment a été placé, sinon (si la place est deja pise), return null
	 */
	public EventLittleView addEvent(EventBean newEv, Calendar day) {
		for (EventBean event : events) {
			if(EventUtil.isAtSameTime(newEv, event)) {
				return null;
			}
		}
		EventLittleView view = new EventLittleView(getContext(), newEv, day);
		eventViews.add(view);
		events.add(newEv);
		addView(view);
		return view;
	}

	public EventLittleView addEvent(EventLittleView view) {
		for (EventBean event : events) {
			if(EventUtil.isAtSameTime(view.getEvent(), event)) {
				return null;
			}
		}
		eventViews.add(view);
		events.add(view.getEvent());
		addView(view);
		return view;
	}

	public boolean unload(EventLittleView view) {
		if(events.contains(view.getEvent())) {
			events.remove(view.getEvent());
			removeView(view);
			if(events.size() == 0) {
				((ViewGroup)getParent()).removeView(this);
			}
			return true;
		}
		return false;
	}



}
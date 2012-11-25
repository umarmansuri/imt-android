package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
	 * Tente d'ajouter l'evenement � la vue
	 * @return true si l'�v�nment a �t� plac�, sinon (si la place est deja pise), return false
	 */
	public boolean addEvent(EventBean newEv, Calendar day) {
		for (EventBean event : events) {
			if(EventUtil.isAtSameTime(newEv, event)) {
				return false;
			}
		}
		EventLittleView view = new EventLittleView(getContext(), newEv, day);
		eventViews.add(view);
		events.add(newEv);
		addView(view);
		return true;
	}

	
	
}
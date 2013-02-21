package its.my.time.pages.calendar.list;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.SeparatedListAdapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ListEventAdapter extends SeparatedListAdapter {

	public final static String EVENT_HOUR = "hour";
	public final static String EVENT_TITLE = "title";
	public final static String EVENT_DETAILS = "details";
	public final static String EVENT_COLOR = "color";
	public final static String EVENT_ID = "id";
	public final static String EVENT_TYPE_EVENT = "typeEvent";

	public ListEventAdapter(Context context) {
		super(context);
	}

	public Map<String, ?> createItem(EventBaseBean event, int color)  {
		Map<String,Object> item = new HashMap<String,Object>();
		item.put(EVENT_HOUR, (DateUtil.getHourLabelLong(event.gethDeb()).toString()));
		item.put(EVENT_TITLE, event.getTitle());
		item.put(EVENT_DETAILS, event.getDetails());
		item.put(EVENT_COLOR, color);
		item.put(EVENT_ID, event.getId());
		item.put(EVENT_TYPE_EVENT, event.getTypeId());
		return item;
	}

	@Override
	public void loadData() {

		List<EventBaseBean> events = new EventBaseRepository(getContext()).getAllNextFromNow(PreferencesUtil.getCurrentUid(getContext()));
		if(events.size() > 0) {
			Calendar lastCal = events.get(0).gethDeb();
			List<Map<String,?>> day = new LinkedList<Map<String,?>>();
			for (EventBaseBean event : events) {
				if(!DateUtil.isInDay(event, lastCal)) {
					addDay(day, lastCal);
					lastCal = event.gethDeb();
					day = new LinkedList<Map<String,?>>();
				}
				int color = new CompteRepository(getContext()).getById(event.getCid()).getColor();
				day.add(createItem(event, color));
			}
			addDay(day, lastCal);
		}
	}
	
	private void addDay(List<? extends Map<String, ?>> day, Calendar lastCal) {
		if(day.size() > 0) {
			addSection(
					DateUtil.getLongDate(lastCal).toString(), 
					new DayAdapter(
							getContext(), 
							day, 
							R.layout.activity_calendar_liste_event,
							new String[] { EVENT_HOUR, EVENT_TITLE, EVENT_DETAILS}, 
							new int[] { R.id.calendar_liste_event_heure, R.id.calendar_liste_event_title, R.id.calendar_liste_event_details}));
		}
	}

	private class DayAdapter extends SimpleAdapter {

		public DayAdapter(Context context, List<? extends Map<String, ?>> data,int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			@SuppressWarnings("unchecked")
			final Map<String, ?> map = (Map<String, ?>)getItem(position);
			int color = (Integer) map.get(EVENT_COLOR);
			view.setBackgroundColor(color);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int id = (Integer) map.get(EVENT_ID);
					int typeEvent = (Integer) map.get(EVENT_TYPE_EVENT);
					ActivityUtil.startEventActivity(getContext(), id, typeEvent);
				}
			});
			return view;
		}

	}


}
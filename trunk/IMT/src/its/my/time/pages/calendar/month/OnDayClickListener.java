package its.my.time.pages.calendar.month;

import its.my.time.data.bdd.events.event.EventBaseBean;

import java.util.GregorianCalendar;
import java.util.List;

import android.view.View;


	public interface OnDayClickListener {
		void onDayClickListener(View v, GregorianCalendar day, List<EventBaseBean> events);
		void onDayLongClickListener(View v, GregorianCalendar day, List<EventBaseBean> events);
	}
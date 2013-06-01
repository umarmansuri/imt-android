package its.my.time.pages.calendar.month;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.pages.calendar.base.BaseFragment;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.view.View;

public class MonthFragment extends BaseFragment {


	private Calendar calDeb;


	public MonthFragment(Calendar calDeb, Calendar calFin) {
		super(calDeb, calFin);
		this.calDeb = calDeb;
	}

	@Override
	protected BaseView createView(OnViewCreated onViewCreated) {
		return new MonthView(getSherlockActivity(), calDeb, this.dayListener, onViewCreated);
	}

	private final OnDayClickListener dayListener = new OnDayClickListener() {

		@Override
		public void onDayLongClickListener(View v, GregorianCalendar day, List<EventBaseBean> events) {
			ActivityUtil.startEventActivity(getSherlockActivity(), day, true);
		}

		@Override
		public void onDayClickListener(View v, GregorianCalendar day, List<EventBaseBean> events) {
			((CalendarActivity) getSherlockActivity()).showDays(day);
		}
	};
}

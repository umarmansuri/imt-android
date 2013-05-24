package its.my.time.pages.calendar.day;

import its.my.time.pages.calendar.base.BasePagerAdapter;
import its.my.time.util.DateUtil;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DayPagerAdapter extends BasePagerAdapter {

	public DayPagerAdapter(FragmentManager fm, Calendar cal) {
		super(fm, cal);
	}

	@Override
	protected Fragment getView(int incrementation) {
		Calendar displayedMonth = getCalendarAtIncrementation(incrementation);
		Calendar calDeb = (Calendar) displayedMonth.clone();
		calDeb.set(Calendar.HOUR_OF_DAY,0);
		calDeb.set(Calendar.MINUTE,0);
		calDeb.set(Calendar.SECOND,0);
		calDeb.set(Calendar.MILLISECOND,0);
		Calendar calFin = (Calendar) calDeb.clone();
		calFin.add(Calendar.DAY_OF_MONTH,1);
		calFin.add(Calendar.MILLISECOND, -1);
		return new DayFragment(calDeb, calFin);
	}

	@Override
	protected String getCustomTitle(int incrementation) {
		Calendar displayedDay = getCalendarAtIncrementation(incrementation);
		return DateUtil.getLongDate(displayedDay);
	}
	
	@Override
	protected Calendar getCalendarAtIncrementation(int incrementation) {
		Calendar displayedDay = (Calendar) getCurrentCalendar().clone();
		displayedDay.add(Calendar.DAY_OF_MONTH, incrementation);
		return displayedDay;
	}
}

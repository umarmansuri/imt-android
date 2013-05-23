package its.my.time.pages.calendar.month;

import its.my.time.pages.calendar.base.BasePagerAdapter;
import its.my.time.util.DateUtil;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MonthPagerAdapter extends BasePagerAdapter {

	public MonthPagerAdapter(FragmentManager fm, Calendar cal) {
		super(fm, cal);
	}

	@Override
	protected Fragment getView(int incrementation) {
		Calendar displayedMonth = getCalendarAtIncrementation(incrementation);
		return new MonthFragment(displayedMonth);
	}

	@Override
	protected String getCustomTitle(int incrementation) {
		Calendar displayedMonth = getCalendarAtIncrementation(incrementation);
		return DateUtil.getMonth(displayedMonth.get(Calendar.YEAR),
				displayedMonth.get(Calendar.MONTH));
	}
	
	@Override
	protected Calendar getCalendarAtIncrementation(int incrementation) {
		Calendar displayedMonth = (Calendar) getCurrentCalendar().clone();
		displayedMonth.add(Calendar.MONTH, incrementation);
		return displayedMonth;
	}
}

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
		Calendar calDeb = (Calendar) displayedMonth.clone();
		calDeb.set(Calendar.DAY_OF_MONTH,0);
		calDeb.set(Calendar.HOUR_OF_DAY,0);
		calDeb.set(Calendar.MINUTE,0);
		calDeb.set(Calendar.SECOND,0);
		calDeb.set(Calendar.MILLISECOND,0);
		Calendar calFin = (Calendar) calDeb.clone();
		calFin.add(Calendar.MONTH,1);
		calFin.add(Calendar.MILLISECOND, -1);
		return new MonthFragment(calDeb, calFin);
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

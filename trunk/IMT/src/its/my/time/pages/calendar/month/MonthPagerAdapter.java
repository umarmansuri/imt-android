package its.my.time.pages.calendar.month;

import its.my.time.pages.calendar.base.BasePagerAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class MonthPagerAdapter extends BasePagerAdapter {

	public MonthPagerAdapter(FragmentManager fm, Calendar cal) {
		super(fm, cal);
	}

	@Override
	protected Fragment getView(int incrementation) {
		Calendar displayedMonth = (Calendar) getCurrentCalendar().clone();
		displayedMonth.add(Calendar.MONTH, incrementation);
		return new MonthFragment(displayedMonth);
	}



}
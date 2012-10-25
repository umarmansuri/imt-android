package its.my.time.pages.calendar.day;

import its.my.time.pages.calendar.base.BasePagerAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DayPagerAdapter extends BasePagerAdapter{

	public DayPagerAdapter(FragmentManager fm, Calendar cal) {
		super(fm, cal);
	}

	@Override
	protected Fragment getView(int incrementation) {
		Calendar displayedDay = (Calendar) getCurrentCalendar().clone();
		displayedDay.add(Calendar.DAY_OF_MONTH, incrementation);
		return new DayFragment(displayedDay);
	}

}

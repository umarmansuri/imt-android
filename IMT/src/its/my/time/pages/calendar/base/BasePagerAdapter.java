package its.my.time.pages.calendar.base;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public abstract class BasePagerAdapter extends FragmentStatePagerAdapter {

	public static final int NB_PAGE = 1000;

	private Calendar cal;

	public BasePagerAdapter(FragmentManager fm, Calendar cal) {
		super(fm);
		this.cal = cal;
	}

	@Override
	public Fragment getItem(int position) {
		final int incrementation = getIncrementation(position);
		final Fragment fr = getView(incrementation);
		return fr;
	}

	@Override
	public int getCount() {
		return NB_PAGE;
	}

	public String getTitle(int position) {
		final int incrementation = getIncrementation(position);
		return getCustomTitle(incrementation);
	}

	public int getIncrementation(int position) {
		return position - NB_PAGE / 2;
	}
	
	public Calendar getCalendarAtPosition(int position) {
		return getCalendarAtIncrementation(getIncrementation(position));
	}
	
	protected abstract String getCustomTitle(int incrementation);
	protected abstract Calendar getCalendarAtIncrementation(int incrementation);
	protected abstract Fragment getView(int incrementation);

	public void setCurrentCalendar(Calendar cal2) {
		this.cal = cal2;
	}
	public Calendar getCurrentCalendar() {
		return cal;
	}

}

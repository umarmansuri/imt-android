package its.my.time.pages.calendar.base;

import its.my.time.pages.calendar.CalendarActivity;

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
		int incrementation = position - NB_PAGE / 2;
		return getView(incrementation);
	}

	@Override
	public int getCount() {return NB_PAGE;}

	protected abstract Fragment getView(int incrementation);

	public Calendar getCurrentCalendar() {
		cal = CalendarActivity.curentCal;
		return cal;
	}
	
	public void setCurrentCalendar(Calendar cal) {
		this.cal = cal;
		notifyDataSetChanged();
	}



}

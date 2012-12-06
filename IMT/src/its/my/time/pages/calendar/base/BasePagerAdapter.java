package its.my.time.pages.calendar.base;

import its.my.time.pages.calendar.CalendarActivity;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public abstract class BasePagerAdapter extends FragmentStatePagerAdapter {

	public static final int NB_PAGE = 1000;
	
	private Calendar cal;
	private int currentIndex = 0;

	public BasePagerAdapter(FragmentManager fm, Calendar cal) {
		super(fm);
		this.cal = cal;
	}

	@Override
	public Fragment getItem(int position) {
		int incrementation = position - NB_PAGE / 2;
		if(incrementation < currentIndex) {
			CalendarActivity.mTextTitle.setText(getTitle(getCurrentCalendar(), incrementation + 1));
			currentIndex += 1;
		} else {
			CalendarActivity.mTextTitle.setText(getTitle(getCurrentCalendar(), incrementation - 1));
			currentIndex -= 1;
		}
		Fragment fr = getView(incrementation);
		return fr;
	}

	@Override
	public int getCount() {return NB_PAGE;}

	protected abstract Fragment getView(int incrementation);
	protected abstract String getTitle(Calendar cal, int incrementation);

	public Calendar getCurrentCalendar() {
		cal = CalendarActivity.curentCal;
		return cal;
	}
	
	public void setCurrentCalendar(Calendar cal) {
		this.cal = cal;
		notifyDataSetChanged();
	}




}

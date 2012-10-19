package its.my.time.pages.calendar.day;

import java.util.GregorianCalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class DayFragment extends SherlockFragment{

	
	private GregorianCalendar cal;

	public DayFragment() {
		this(new GregorianCalendar());
	}
	
	public DayFragment(GregorianCalendar cal) {
		this.cal = cal;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return new DayView(getSherlockActivity(), cal);
	}
	
}

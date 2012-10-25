package its.my.time.pages.calendar.day;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class DayFragment extends SherlockFragment{

	
	private Calendar cal;

	public DayFragment() {
		this(Calendar.getInstance());
	}
	
	public DayFragment(Calendar cal) {
		this.cal = cal;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return new DayView(getSherlockActivity(), cal);
	}
	
}

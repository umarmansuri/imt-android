package its.my.time.pages.calendar.month;

import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.util.DateUtil;

import java.util.GregorianCalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class MonthFragment extends SherlockFragment {


	private GregorianCalendar cal;



	public MonthFragment() {
		this(new GregorianCalendar());
	}

	public MonthFragment(GregorianCalendar cal) {
		super();
		this.cal = cal;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return new MonthView(getSherlockActivity(), cal, dayListener);
	}

	private MonthView.OnDayClickListener dayListener = new MonthView.OnDayClickListener() {

		public void onDayLongClickListener(GregorianCalendar day) {
			
		}

		public void onDayClickListener(GregorianCalendar day) {
			
			((CalendarActivity)getSherlockActivity()).showDays(day);
		}
	}; 
}

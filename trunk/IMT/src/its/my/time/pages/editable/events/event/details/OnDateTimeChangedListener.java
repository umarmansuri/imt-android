package its.my.time.pages.editable.events.event.details;

import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class OnDateTimeChangedListener implements OnDateSetListener,
OnTimeSetListener {

	private TextView currentView;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	@Override
	public void onTimeSet(TimePicker picker, int hour, int minute) {
		mHour = hour;
		mMinute = minute;
		updateTime();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
		updateDate();
	}

	private void updateDate() {
		Calendar cal = new GregorianCalendar();
		cal.set(mYear, mMonth, mDay, mHour, mMinute);
		currentView.setText(DateUtil.getTime(cal));
	}

	private void updateTime() {
		Calendar cal = new GregorianCalendar();
		cal.set(mYear, mMonth, mDay, mHour, mMinute);
		currentView.setText(DateUtil.getDay(cal));
	}

	public TextView getCurrentView() {
		return currentView;
	}

	public void setCurrentView(TextView currentView) {
		this.currentView = currentView;
	}

}

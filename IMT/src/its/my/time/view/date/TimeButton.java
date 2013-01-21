package its.my.time.view.date;

import its.my.time.util.DateUtil;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class TimeButton extends Button implements OnTimeSetListener,
		OnClickListener {

	private TimePickerDialog dialog;
	private Calendar date;

	public TimeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(null);
	}

	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(null);
	}

	public TimeButton(Context context) {
		super(context);
		init(null);
	}

	private void init(Calendar initDate) {
		if (initDate != null) {
			this.date = initDate;
		} else {
			this.date = Calendar.getInstance();
		}
		setOnClickListener(this);
		setText(DateUtil.getTime(this.date));
	}

	@Override
	public void onTimeSet(TimePicker arg0, int hour, int minute) {
		this.date.set(Calendar.HOUR_OF_DAY, hour);
		this.date.set(Calendar.MINUTE, minute);
		setText(DateUtil.getTime(this.date));
	}

	@Override
	public void onClick(View arg0) {
		this.dialog = new TimePickerDialog(getContext(), this,
				this.date.get(Calendar.HOUR_OF_DAY),
				this.date.get(Calendar.MINUTE), true);
		this.dialog.show();
	}

	public Calendar getDate() {
		return this.date;
	}

	public void setDate(Calendar date) {
		init(date);
	}

}
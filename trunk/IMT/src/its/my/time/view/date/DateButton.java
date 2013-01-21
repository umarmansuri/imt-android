package its.my.time.view.date;

import its.my.time.util.DateUtil;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;

public class DateButton extends Button implements OnDateSetListener,
		OnClickListener {

	private DatePickerDialog dialog;
	private Calendar date;

	public DateButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(null);
	}

	public DateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(null);
	}

	public DateButton(Context context) {
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
		setText(DateUtil.getDayHourFrench(this.date));
	}

	@Override
	public void onDateSet(DatePicker arg0, int year, int month, int day) {
		this.date.set(Calendar.YEAR, year);
		this.date.set(Calendar.MONTH, month);
		this.date.set(Calendar.DAY_OF_MONTH, day);
		setText(DateUtil.getDayHourFrench(this.date));
	}

	@Override
	public void onClick(View arg0) {
		this.dialog = new DatePickerDialog(getContext(), this,
				this.date.get(Calendar.YEAR), this.date.get(Calendar.MONTH),
				this.date.get(Calendar.DAY_OF_MONTH));
		this.dialog.show();
	}

	public Calendar getDate() {
		return this.date;
	}

	public void setDate(Calendar date) {
		init(date);
	}

}
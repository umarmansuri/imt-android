package its.my.time.pages.calendar.month;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.IdUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MonthView extends BaseView {

	private MonthDisplayHelper helper;
	private OnDayClickListener listener;

	public MonthView(Context context, Calendar cal, OnDayClickListener listener) {
		super(context);
		this.helper = new MonthDisplayHelper(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), DateUtil.FIRST_DAY);
		this.listener = listener;
	}


	@Override
	protected View createView() {
		LinearLayout view = (LinearLayout) inflate(getContext(), R.layout.activity_calendar_month, null);
		createTabDay(view);
		return view;
	}

	@Override
	protected String getTopBarText() {
		return DateUtil.getMonth(helper.getYear(), helper.getMonth());
	}

	private void addStyleToday(TextView view) {
		view.getBackground().setColorFilter(Color.parseColor("#FFFFCC"), Mode.MULTIPLY);
	}
	private void createTabDay(LinearLayout view) {

		boolean isInMois = false;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		GregorianCalendar calDeb = new GregorianCalendar(helper.getYear(), helper.getMonth(), 1, 0, 0, 0);
		GregorianCalendar calFin = new GregorianCalendar(helper.getYear(), helper.getMonth(), helper.getNumberOfDaysInMonth() , 0, 0, 0);
		calFin.add(Calendar.DAY_OF_MONTH, 1);
		List<EventBaseBean> listEventsFinal = new ArrayList<EventBaseBean>();


		listEventsFinal = DatabaseUtil.Events.getEventRepository(getContext()).getAllEvents(calDeb, calFin);
		
		LinearLayout tabJour = (LinearLayout)view.findViewById(R.id.llTabJour);
		LinearLayout ligne;
		TextView txtVw;
		for (int i = 0; i < 5; i++){
			ligne = (LinearLayout) tabJour.getChildAt(i);

			//cellule du numério de semaine

			cal.set(helper.getYear(), helper.getMonth(), helper.getDayAt(i, 0));

			if (!isInMois){
				if (i <= 1) {
					cal.add(Calendar.MONTH, -1);
				} else {
					cal.add(Calendar.MONTH, 1);
				}
			}
			txtVw = (TextView) ligne.getChildAt(0);
			txtVw.setText(String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
			txtVw.setId(IdUtil.getWeekId(cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR)));

			//liste des jours
			for (int j = 1; j < 8; j++){
				//detecte si le jour est dans le mois en cours
				if (helper.getDayAt(i, j - 1) == 1){
					isInMois = !isInMois;
				}

				txtVw = (TextView) ligne.getChildAt(j);
				txtVw.setId(IdUtil.getDayId(helper.getYear(), helper.getMonth(), helper.getDayAt(i, j - 1)));
				txtVw.setEnabled(isInMois);
				txtVw.setText(String.valueOf(helper.getDayAt(i, j - 1)));
				if (!isInMois) {
					txtVw.setTextColor(getResources().getColor(R.color.light_grey));
				}

				GregorianCalendar today = new GregorianCalendar();

				if (today.get(Calendar.YEAR) == helper.getYear() 
						&& today.get(Calendar.MONTH) == helper.getMonth()
						&& today.get(Calendar.DAY_OF_MONTH) == helper.getDayAt(i, j - 1)
						&& isInMois) {
					addStyleToday(txtVw);
				}

				GregorianCalendar calDay = new GregorianCalendar(helper.getYear(), helper.getMonth(), helper.getDayAt(i, j - 1));
				int indexEv = 0;
				boolean hasEvent = false;
				while (hasEvent == false && indexEv < listEventsFinal.size()) {
					if(DateUtil.isInDay(listEventsFinal.get(indexEv), calDay)) {
						hasEvent = true;
						//TODO afficher les events
					}	
					indexEv++;
				}

				if(listener != null) {
					final GregorianCalendar calListener = (GregorianCalendar) calDay.clone();
					txtVw.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							listener.onDayClickListener(calListener);
						}
					});
					txtVw.setOnLongClickListener(new OnLongClickListener() {
						public boolean onLongClick(View v) {
							listener.onDayLongClickListener(calListener);
							return false;
						}
					});
				}
			}
		}
	}

	public interface OnDayClickListener {
		void onDayClickListener(GregorianCalendar day);
		void onDayLongClickListener(GregorianCalendar day);
	}

}

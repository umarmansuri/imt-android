package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class DayView extends BaseView{

	private GregorianCalendar cal;

	private ArrayList<EventBean> events;

	private LinearLayout llEvent;

	private Calendar firstCal;


	protected boolean isFinished;

	private LinearLayout view;

	public DayView(Context context, Calendar cal) {
		super(context);
		this.cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
	}

	@Override
	protected View createView() {
		view = (LinearLayout) inflate(getContext(), R.layout.activity_calendar_day, null);

		llEvent = (LinearLayout)view.findViewById(R.id.activity_calendar_day_layout_event);

		createTabHeure();

		OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				float ligneHeight =  getResources().getDimension(R.dimen.view_day_height_ligne_heure);
				firstCal.add(Calendar.MINUTE, -30);
				int scroll = ((int) (firstCal.get(GregorianCalendar.HOUR_OF_DAY)  * ligneHeight + (((float)firstCal.get(GregorianCalendar.MINUTE)/ 60) * ligneHeight)));
				ScrollView mainScroll = (ScrollView)view.findViewById(R.id.activity_calendar_day_mainscroll);
				mainScroll.smoothScrollTo(0, scroll);
			}
		};
		view.getViewTreeObserver().addOnGlobalLayoutListener(listener);

		return view;
	}

	@Override
	protected String getTopBarText() {
		return DateUtil.getLongDate(cal);
	}

	private void createTabHeure() {
		events = new ArrayList<EventBean>();
		/*TODO
		for (CompteBean compte : DataUtil.getInstance().getListeCompte().values()) {
			if(compte.isShowed()) {
				listeCompteShowed.add(compte.getId());
			}
		}					
		listEventsTmp = DataUtil.getInstance().getEventRepo().GetListEvent(listeCompteShowed, calDeb, calFin);
		events.addAll(listEventsTmp);
		 */
		EventBean bean;
		GregorianCalendar calDeb2;
		GregorianCalendar calFin2;
		bean = new EventBean();
		bean.setId(0);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8,30);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(1);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),9,0);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(2);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),9,30);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(3);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),9,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),11,0);
		bean.sethFin(calFin2);
		events.add(bean);

		for (EventBean event : events) {
			if(firstCal == null || event.gethDeb().before(firstCal)) {
				firstCal = event.gethDeb();
			}
			addEventView(event);
		}

	}

	private void addEventView(EventBean event) {
		ColumnEvent column;
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			if(column.addEvent(event, cal)) {
				return;
			}
		}
		column= new ColumnEvent(getContext());
		column.addEvent(event, cal);

		llEvent.addView(column);
		return;
	}

}
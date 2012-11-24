package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DayView extends BaseView{

	private GregorianCalendar cal;

	private ArrayList<EventBean> events;
	private HashMap<Integer, Boolean> mapRightUse;
	private HashMap<Integer, Boolean> mapBottomUse;
	private HashMap<EventBean, View> mapEventViews;

	private View.OnClickListener onEventClickLIstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ActivityUtil.startEventActivity(getContext(), 1);
		}
	};

	private RelativeLayout llHeure;

	public DayView(Context context, Calendar cal) {
		super(context);
		this.cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
	}

	@Override
	protected View createView() {
		LinearLayout view = (LinearLayout) inflate(getContext(), R.layout.activity_calendar_day, null);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

		initialiseListeHeure(view);
		createTabHeure();

		return view;
	}

	@Override
	protected String getTopBarText() {
		return DateUtil.getLongDate(cal);
	}

	private void initialiseListeHeure(ViewGroup view) {
		llHeure = (RelativeLayout)view.findViewById(R.id.activity_calendar_day_layoutHeure);
		TextView mTextView;
		for(int i = 0; i < 24; i++) {
			mTextView = ((TextView)llHeure.getChildAt(i));
			final int hour = i;
			mTextView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					GregorianCalendar calHeure = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), hour, cal.get(GregorianCalendar.MINUTE));
					ActivityUtil.startEventActivity(getContext(), calHeure);
					return true;
				}
			});
			if(i == 0 || i == 23) {
				mTextView.setText("");
			} else {
				mTextView.setText(String.valueOf(i));
			}
		}
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
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),10, 30);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(1);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8, 30);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),10, 30);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(2);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),9,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),10, 0);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(3);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),11,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),11, 30);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(4);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),11,15);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),13, 30);
		bean.sethFin(calFin2);
		events.add(bean);

		bean = new EventBean();
		bean.setId(5);
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),10, 30);
		bean.sethFin(calFin2);
		events.add(bean);

		
		
		LinearLayout eventView;
		mapBottomUse = new HashMap<Integer, Boolean>();
		mapEventViews = new HashMap<EventBean, View>();
		mapRightUse = new HashMap<Integer, Boolean>();

		View blankBefore;
		View blankAfter;
		for (EventBean event : events) {
			eventView = (LinearLayout) inflate(getContext(), R.layout.activity_calendar_day_little, null);
			int height = (int) (DateUtil.getNbHeure(event.gethDeb(), event.gethFin(), cal) * getContext().getResources().getDimension(R.dimen.view_day_height_ligne_heure));
			eventView.findViewById(R.id.activity_calendar_day_little_event).getLayoutParams().height = height;
			eventView.findViewById(R.id.activity_calendar_day_little_event).setOnClickListener(onEventClickLIstener);
			eventView.findViewById(R.id.activity_calendar_day_little_event).setId(event.getId());
			blankBefore = eventView.findViewById(R.id.activity_calendar_day_little_blank_before);
			for (EventBean prevEvent : mapEventViews.keySet()) {
				if(EventUtil.isAtSameTime(event, prevEvent)) {
					blankAfter = mapEventViews.get(prevEvent).findViewById(R.id.activity_calendar_day_little_blank_after);
					((LinearLayout.LayoutParams)blankAfter.getLayoutParams()).weight = ((LinearLayout.LayoutParams)blankAfter.getLayoutParams()).weight + 1;
					((LinearLayout.LayoutParams)blankBefore.getLayoutParams()).weight = ((LinearLayout.LayoutParams)blankBefore.getLayoutParams()).weight + 1;
				} else if(mapBottomUse.get(event.getId()) != null && mapBottomUse.get(event.getId()) && EventUtil.isUnder(event, prevEvent)) {
					((LinearLayout.LayoutParams)blankBefore.getLayoutParams()).weight = mapEventViews.get(prevEvent).findViewById(R.id.activity_calendar_day_little_blank_before).getLayoutParams().height;
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					params.topMargin = (int) ((event.gethDeb().get(Calendar.HOUR_OF_DAY) + event.gethDeb().get(Calendar.MINUTE)/60) * getContext().getResources().getDimension(R.dimen.view_day_height_ligne_heure));
					mapBottomUse.put(event.getId(), true);
				} else {

				}
			}

			mapBottomUse.put(event.getId(),false);
			mapRightUse.put(event.getId(),false);
			mapEventViews.put(event, eventView);
		}

		for(View v : mapEventViews.values()) {
			llHeure.addView(v);
		}
	}

}
package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DayView extends BaseView{

	private GregorianCalendar cal;

	private ArrayList<EventBean> events;

	private LinearLayout llEvent;

	private Calendar firstCal;


	protected boolean isFinished;

	private LinearLayout view;

	private float topMainScroll;

	private ScrollView mainScroll;

	private float ligneHeight;

	public DayView(Context context, Calendar cal) {
		super(context);
		this.cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
	}

	@Override
	protected View createView() {
		view = (LinearLayout) inflate(getContext(), R.layout.activity_calendar_day, null);

		llEvent = (LinearLayout)view.findViewById(R.id.activity_calendar_day_layout_event);
		mainScroll = (ScrollView)view.findViewById(R.id.activity_calendar_day_mainscroll);

		createTabHeure();

		ligneHeight =  getResources().getDimension(R.dimen.view_day_height_ligne_heure);

		OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				firstCal.add(Calendar.MINUTE, -30);
				int scroll = ((int) (firstCal.get(GregorianCalendar.HOUR_OF_DAY)  * ligneHeight + (((float)firstCal.get(GregorianCalendar.MINUTE)/ 60) * ligneHeight)));
			}
		};
		view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
		topMainScroll = getContext().getResources().getDimension(R.dimen.view_day_height_ligne_heure_half);
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
		calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),5,0);
		bean.sethDeb(calDeb2);
		calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),7,0);
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

	private EventLittleView addEventView(EventBean event) {
		ColumnEvent column;
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			EventLittleView eventView = column.addEvent(event, cal);

			if(eventView != null) {
				ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
				eventView.setOnLongClickListener(moveEventListener);
				ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(eventView);
				eventView.getBottomDraggable().setOnLongClickListener(changeEventDurationLIstener);
				return eventView;
			}
		}
		column= new ColumnEvent(getContext());
		EventLittleView eventView = column.addEvent(event, cal);

		ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
		eventView.setOnLongClickListener(moveEventListener);
		ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(eventView);
		eventView.getBottomDraggable().setOnLongClickListener(changeEventDurationLIstener);

		llEvent.addView(column);
		return eventView;
	}
	
	private EventLittleView addEventView(EventLittleView view) {
		ColumnEvent column;
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			EventLittleView eventView = column.addEvent(view);

			if(eventView != null) {
				return eventView;
			}
		}
		column= new ColumnEvent(getContext());
		EventLittleView eventView = column.addEvent(view);

		llEvent.addView(column);
		return eventView;
	}



	private void reloadEventLittleView(EventLittleView eventView) {
		ColumnEvent column;
		EventBean event = eventView.getEvent();
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			if(column.unload(eventView)) {
				addEventView(eventView);
				return;
			}
		}
	}

	public class ListenerMoveEvent implements OnTouchListener, OnLongClickListener {


		private int lastY=-100000;
		private EventLittleView draggedView;

		@Override
		public boolean onLongClick(View v) {
			draggedView = (EventLittleView)v;
			draggedView.onSetAlpha(100);
			mainScroll.requestDisallowInterceptTouchEvent(true);
			draggedView.setOnTouchListener(this);
			v.bringToFront();
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) v.getLayoutParams();
			if (event.getAction()==MotionEvent.ACTION_MOVE) {
				if(lastY!=-100000) {
					layout.topMargin = (int) (layout.topMargin + event.getRawY() - lastY);
					//layout.topMargin = (Math.round((layout.topMargin * 4)/100)) * 100 / 4;
					if(layout.topMargin < 0) {
						layout.topMargin = 0;
					}
					if(layout.topMargin + layout.height > mainScroll.getChildAt(0).getMeasuredHeight()) {
						layout.topMargin = mainScroll.getChildAt(0).getMeasuredHeight() - layout.height - 30;
					}
					if(layout.topMargin + layout.height > mainScroll.getScrollY() + mainScroll.getMeasuredHeight() + 10) {
						mainScroll.smoothScrollTo(0, (int) (layout.topMargin - mainScroll.getMeasuredHeight() + layout.height + 10));
						layout.topMargin += 30;
					}
					if(layout.topMargin < mainScroll.getScrollY() -10) {
						mainScroll.smoothScrollTo(0, (int) (layout.topMargin) - 10);
						layout.topMargin -= 30;
					}
					draggedView.updateFromLayout(layout);
				}
				lastY = (int)event.getRawY();
			} else if (event.getAction()==MotionEvent.ACTION_UP) {
				draggedView.onSetAlpha(255);
				draggedView.setOnTouchListener(null);
				lastY=-100000;
				mainScroll.requestDisallowInterceptTouchEvent(false);
				reloadEventLittleView(draggedView);
			}
			return true;
		}

	}



	public class ListenerChangeEventDuration implements OnTouchListener, OnLongClickListener {


		private int lastY=-100000;
		private EventLittleView parent;
		private Context context;


		public ListenerChangeEventDuration(EventLittleView parent) {
			this.parent = parent;
			this.context = parent.getContext();
		}

		@Override
		public boolean onLongClick(View v) {
			mainScroll.requestDisallowInterceptTouchEvent(true);
			v.setOnTouchListener(this);
			v.bringToFront();
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) parent.getLayoutParams();
			if (event.getAction()==MotionEvent.ACTION_MOVE) {
				if(lastY!=-100000) {
					layout.height = (int) (layout.height + event.getRawY() - lastY);
					//layout.height = (Math.round((layout.height * 4)/100)) * 100 / 4;
					if(layout.height < ligneHeight) {
						layout.height = (int) ligneHeight;
					}
				}
				lastY = (int)event.getRawY();
				parent.updateFromLayout(layout);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				v.setOnTouchListener(null);
				lastY=-100000;
				mainScroll.requestDisallowInterceptTouchEvent(false);
				//TODO enregistrer nouvelle heure de debut
			}
			return true;
		}
	}

}
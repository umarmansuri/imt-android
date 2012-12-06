package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DayView extends BaseView {

	private Calendar firstCal;

	private GregorianCalendar cal;
	private List<EventBaseBean> events;
	private LinearLayout llEvent;

	protected boolean isFinished;
	private LinearLayout view;
	private ScrollView mainScroll;

	private float ligneHeight;


	public DayView(Context context, Calendar cal) {
		super(context);
		this.cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),
				cal.get(GregorianCalendar.MONTH),
				cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
		firstCal = new GregorianCalendar(0, 0, 0, 8, 0);
	}

	@Override
	protected View createView() {
		view = (LinearLayout) inflate(getContext(),
				R.layout.activity_calendar_day, null);

		ViewGroup lignes = ((ViewGroup) view
				.findViewById(R.id.activity_calendar_day_layoutHeure));
		for (int i = 0; i < lignes.getChildCount(); i++) {
			if (lignes.getChildAt(i).getClass().isAssignableFrom(Ligne.class)) {
				lignes.getChildAt(i).setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						Ligne ligne = (Ligne)v;
						Calendar hour = new GregorianCalendar(cal
								.get(Calendar.YEAR), cal
								.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH), ligne
								.getHeure(), 0, 0);
						ActivityUtil.startEventActivity(
								getContext(), hour);
						return false;
					}
				});

			}
		}

		llEvent = (LinearLayout) view.findViewById(R.id.activity_calendar_day_layout_event);
		mainScroll = (ScrollView) view.findViewById(R.id.activity_calendar_day_mainscroll);

		createTabHeure();

		ligneHeight = getResources().getDimension(
				R.dimen.view_day_height_ligne_heure);

		OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				firstCal.add(Calendar.MINUTE, -30);
				int scroll = ((int) (firstCal
						.get(GregorianCalendar.HOUR_OF_DAY) * ligneHeight + (((float) firstCal
						.get(GregorianCalendar.MINUTE) / 60) * ligneHeight)));
				mainScroll.smoothScrollBy(scroll, 0);
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
		Calendar calDeb = new GregorianCalendar(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));
		calDeb.set(Calendar.HOUR_OF_DAY, 0);
		calDeb.set(Calendar.MINUTE, 0);
		calDeb.set(Calendar.SECOND, 0);
		Calendar calFin = new GregorianCalendar(calDeb.get(Calendar.YEAR),
				calDeb.get(Calendar.MONTH), calDeb.get(Calendar.DAY_OF_MONTH),
				calDeb.get(Calendar.HOUR_OF_DAY), calDeb.get(Calendar.MINUTE),
				calDeb.get(Calendar.SECOND));
		calFin.add(Calendar.DAY_OF_MONTH, 1);
		events = DatabaseUtil.Events.getEventRepository(getContext())
				.getAllEvents(calDeb, calFin);

		for (EventBaseBean event : events) {
			if (firstCal == null || event.gethDeb().before(firstCal)) {
				firstCal = event.gethDeb();
			}
			addEventView(event);
		}
	}

	private EventLittleView addEventView(EventBaseBean event) {
		ColumnEvent column;
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			EventLittleView eventView = column.addEvent(event, cal);

			if (eventView != null) {
				ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
				eventView.setOnLongClickListener(moveEventListener);
				ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(
						eventView);
				eventView.getBottomDraggable().setOnLongClickListener(
						changeEventDurationLIstener);
				return eventView;
			}
		}
		column = new ColumnEvent(getContext());
		EventLittleView eventView = column.addEvent(event, cal);

		ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
		eventView.setOnLongClickListener(moveEventListener);
		ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(
				eventView);
		eventView.getBottomDraggable().setOnLongClickListener(
				changeEventDurationLIstener);

		llEvent.addView(column);
		return eventView;
	}

	private EventLittleView addEventView(EventLittleView view) {
		ColumnEvent column;
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			EventLittleView eventView = column.addEvent(view);

			if (eventView != null) {
				return eventView;
			}
		}
		column = new ColumnEvent(getContext());
		EventLittleView eventView = column.addEvent(view);

		llEvent.addView(column);
		return eventView;
	}

	private void reloadEventLittleView(EventLittleView eventView) {
		ColumnEvent column;
		EventBaseBean event = eventView.getEvent();
		DatabaseUtil.Events.getEventRepository(getContext()).updateEvent(event);
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			column = (ColumnEvent) llEvent.getChildAt(i);
			if (column.unload(eventView)) {
				addEventView(eventView);
				return;
			}
		}
	}

	public class ListenerMoveEvent implements OnTouchListener,
			OnLongClickListener {

		private int lastY = -100000;
		private int lastLigne = -1;
		private EventLittleView draggedView;

		@Override
		public boolean onLongClick(View v) {
			draggedView = (EventLittleView) v;
			draggedView.changeDragged(true);
			mainScroll.requestDisallowInterceptTouchEvent(true);
			draggedView.setOnTouchListener(this);
			draggedView.bringToFront();
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) v
					.getLayoutParams();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lastY != -100000) {
					float topMargin = layout.topMargin + event.getRawY()
							- lastY;
					int nextLigne = Math.round(topMargin
							/ ((float) (ligneHeight / 2)));
					if (lastLigne != nextLigne) {
						lastLigne = nextLigne;

						layout.topMargin = (int) (lastLigne * (ligneHeight / 2));
						if (layout.topMargin < 0) {
							layout.topMargin = 0;
						}
						if (layout.topMargin + layout.height > mainScroll
								.getChildAt(0).getMeasuredHeight()) {
							layout.topMargin = mainScroll.getChildAt(0)
									.getMeasuredHeight() - layout.height - 30;
						}
						if (layout.topMargin + layout.height > mainScroll
								.getScrollY()
								+ mainScroll.getMeasuredHeight()
								+ 10) {
							mainScroll.smoothScrollTo(
									0,
									(int) (layout.topMargin
											- mainScroll.getMeasuredHeight()
											+ layout.height + 10));
							layout.topMargin += 30;
						}
						if (layout.topMargin < mainScroll.getScrollY() - 10) {
							mainScroll.smoothScrollTo(0,
									(int) (layout.topMargin) - 10);
							layout.topMargin -= 30;
						}
						draggedView.updateFromLayout(layout);
						lastY = (int) event.getRawY();
					}
				} else {
					lastY = (int) event.getRawY();
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				draggedView.changeDragged(false);
				draggedView.setOnTouchListener(null);
				lastY = -100000;
				lastLigne = -1;
				mainScroll.requestDisallowInterceptTouchEvent(false);
				reloadEventLittleView(draggedView);
			}
			return true;
		}

	}

	public class ListenerChangeEventDuration implements OnTouchListener,
			OnLongClickListener {

		private int lastY = -100000;
		private int lastLigne = -1;
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
			parent.bringToFront();
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) parent
					.getLayoutParams();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lastY != -100000) {
					float height = layout.height + event.getRawY() - lastY;
					int nextLigne = Math.round((layout.topMargin + height)
							/ ((float) (ligneHeight / 2)));
					if (lastLigne != nextLigne) {
						lastLigne = nextLigne;
						layout.height = (int) (lastLigne * (ligneHeight / 2) - layout.topMargin);
						if (layout.topMargin + layout.height > mainScroll
								.getChildAt(0).getMeasuredHeight()) {
							layout.height = mainScroll.getChildAt(0)
									.getMeasuredHeight()
									- layout.topMargin
									- 30;
						}
						if (layout.topMargin + layout.height > mainScroll
								.getScrollY()
								+ mainScroll.getMeasuredHeight()
								+ 10) {
							mainScroll.smoothScrollTo(
									0,
									(int) (layout.topMargin
											- mainScroll.getMeasuredHeight()
											+ layout.height + 10));
							layout.height += 30;
						}
						if (layout.topMargin + layout.height < mainScroll
								.getScrollY() - 10) {
							mainScroll
									.smoothScrollTo(
											0,
											(int) (layout.topMargin + layout.height) - 10);
							layout.height -= 30;
						}
						if (layout.height < ligneHeight / 2) {
							layout.height = (int) (ligneHeight / 2);
						}
						lastY = (int) event.getRawY();
						parent.updateFromLayout(layout);
					}
				} else {
					lastY = (int) event.getRawY();
				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setOnTouchListener(null);
				lastY = -100000;
				lastLigne = -1;
				mainScroll.requestDisallowInterceptTouchEvent(false);
				reloadEventLittleView(parent);
			}
			return true;
		}
	}
}
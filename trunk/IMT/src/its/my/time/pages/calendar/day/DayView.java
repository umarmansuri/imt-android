package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.calendar.base.BaseFragment.OnViewCreated;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
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

	private final GregorianCalendar cal;
	private List<EventBaseBean> events;
	private LinearLayout llEvent;

	protected boolean isFinished;
	private LinearLayout view;
	private ScrollView mainScroll;

	private List<EventLittleView> eventViews;
	private float ligneHeight;

	public DayView(Context context, Calendar cal, OnViewCreated onViewCreated) {
		super(context, onViewCreated);
		this.cal = new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
		firstCal = new GregorianCalendar(0, 0, 0, 8, 0);

		new Thread(new Runnable() {
			@Override
			public void run() {
				eventViews = new ArrayList<EventLittleView>();
				createView();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						addView(view);
					}
				});
				onViewCreated();
			}
		}).start();
	}


	protected View createView() {
		view = (LinearLayout) inflate(getContext(),R.layout.activity_calendar_day, null);

		final ViewGroup lignes = ((ViewGroup) view.findViewById(R.id.activity_calendar_day_layoutHeure));
		for (int i = 0; i < lignes.getChildCount(); i++) {
			if (lignes.getChildAt(i).getClass().isAssignableFrom(Ligne.class)) {
				lignes.getChildAt(i).setOnLongClickListener(
						new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								final Ligne ligne = (Ligne) v;
								final Calendar hour = new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),ligne.getHeure(), 0, 0);
								ActivityUtil.startEventActivity(getContext(),hour, false);
								return false;
							}
						});

			}
		}

		llEvent = (LinearLayout) view.findViewById(R.id.activity_calendar_day_layout_event);
		mainScroll = (ScrollView) view.findViewById(R.id.activity_calendar_day_mainscroll);

		createTabHeure();

		ligneHeight = getResources().getDimension(R.dimen.view_day_height_ligne_heure);

		final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				firstCal.add(Calendar.MINUTE, -30);
				final int scroll = ((int) (firstCal.get(Calendar.HOUR_OF_DAY) * ligneHeight + (((float) firstCal.get(Calendar.MINUTE) / 60) * ligneHeight)));
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
		final Calendar calDeb = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		calDeb.set(Calendar.HOUR_OF_DAY, 0);
		calDeb.set(Calendar.MINUTE, 0);
		calDeb.set(Calendar.SECOND, 0);

		final Calendar calFin = new GregorianCalendar(
				calDeb.get(Calendar.YEAR), calDeb.get(Calendar.MONTH),
				calDeb.get(Calendar.DAY_OF_MONTH),
				calDeb.get(Calendar.HOUR_OF_DAY), calDeb.get(Calendar.MINUTE),
				calDeb.get(Calendar.SECOND));
		calFin.add(Calendar.DAY_OF_MONTH, 1);
		events = new EventBaseRepository(getContext()).getAllEvents(calDeb, calFin);

		for (final EventBaseBean event : events) {
			if (firstCal == null || event.gethDeb().before(firstCal)) {
				firstCal = event.gethDeb();
			}
		}
	}

	@Override
	public View addEvent(EventBaseBean event, int color, int visibility) {
		for (int i = 0; i < llEvent.getChildCount(); i++) {
			ColumnEvent column = (ColumnEvent) llEvent.getChildAt(i);
			final EventLittleView eventView = column.addEvent(event, cal);

			if (eventView != null) {
				final ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
				eventView.setOnLongClickListener(moveEventListener);
				final ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(eventView);
				eventView.getBottomDraggable().setOnLongClickListener(changeEventDurationLIstener);
				eventView.setVisibility(visibility);
				eventViews.add(eventView);
				return eventView;
			}
		}
		final ColumnEvent column = new ColumnEvent(getContext());
		final EventLittleView eventView = column.addEvent(event, cal);

		final ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
		eventView.setOnLongClickListener(moveEventListener);
		final ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(eventView);
		eventView.getBottomDraggable().setOnLongClickListener(changeEventDurationLIstener);
		eventView.setVisibility(visibility);
		eventViews.add(eventView);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				llEvent.addView(column);	
			}
		});
		return eventView;
	}


	private void reloadEventLittleView(EventLittleView eventView) {
		final EventBaseBean event = eventView.getEvent();
		new EventBaseRepository(getContext()).update(event);

		ColumnEvent column = ((ColumnEvent)eventView.getParent());
		column.unload(eventView);

	}


	public class ListenerMoveEvent implements OnTouchListener,
	OnLongClickListener {

		private int lastY = -100000;
		private int lastLigne = -1;
		private EventLittleView draggedView;

		@Override
		public boolean onLongClick(View v) {
			v.setOnLongClickListener(null);
			draggedView = (EventLittleView) v;
			draggedView.changeDragged(true);
			mainScroll.requestDisallowInterceptTouchEvent(true);
			draggedView.setOnTouchListener(this);
			draggedView.bringToFront();
			return true;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) v.getLayoutParams();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lastY != -100000) {
					final float topMargin = layout.topMargin + event.getRawY()- lastY;
					final int nextLigne = Math.round(topMargin / (ligneHeight / 2));
					if (lastLigne != nextLigne) {
						lastLigne = nextLigne;

						layout.topMargin = (int) (lastLigne * (ligneHeight / 2));
						if (layout.topMargin < 0) {
							layout.topMargin = 0;
						}
						if (layout.topMargin + layout.height > mainScroll .getChildAt(0).getMeasuredHeight()) {
							layout.topMargin = mainScroll.getChildAt(0).getMeasuredHeight() - layout.height - 30;
						}
						if (layout.topMargin + layout.height > mainScroll.getScrollY()+ mainScroll.getMeasuredHeight() + 10) {
							mainScroll.smoothScrollTo(
									0,
									layout.topMargin
									- mainScroll
									.getMeasuredHeight()
									+ layout.height + 10);
							layout.topMargin += 30;
						}
						if (layout.topMargin < mainScroll.getScrollY() - 10) {
							mainScroll.smoothScrollTo(0,(layout.topMargin) - 10);
							layout.topMargin -= 30;
						}
						draggedView.setLayoutParams(layout);
						lastY = (int) event.getRawY();
					}
				} else {
					lastY = (int) event.getRawY();
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				draggedView.updateFromLayout(layout);
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
		private final EventLittleView parent;

		public ListenerChangeEventDuration(EventLittleView parent) {
			this.parent = parent;
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
			final RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) parent
					.getLayoutParams();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lastY != -100000) {
					final float height = layout.height + event.getRawY() - lastY;
					final int nextLigne = Math.round((layout.topMargin + height) / (ligneHeight / 2));
					if (lastLigne != nextLigne) {
						lastLigne = nextLigne;
						layout.height = (int) (lastLigne * (ligneHeight / 2) - layout.topMargin);
						if (layout.topMargin + layout.height > mainScroll.getChildAt(0).getMeasuredHeight()) {
							layout.height = mainScroll.getChildAt(0).getMeasuredHeight() - layout.topMargin - 30;
						}
						if (layout.topMargin + layout.height > mainScroll.getScrollY()+ mainScroll.getMeasuredHeight()+ 10) {
							mainScroll.smoothScrollTo(
									0,
									layout.topMargin
									- mainScroll
									.getMeasuredHeight()
									+ layout.height + 10);
							layout.height += 30;
						}
						if (layout.topMargin + layout.height < mainScroll
								.getScrollY() - 10) {
							mainScroll.smoothScrollTo(0,
									layout.topMargin + layout.height - 10);
							layout.height -= 30;
						}
						if (layout.height < ligneHeight / 2) {
							layout.height = (int) (ligneHeight / 2);
						}
						lastY = (int) event.getRawY();
						parent.setLayoutParams(layout);
					}
				} else {
					lastY = (int) event.getRawY();
				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				parent.updateFromLayout(layout);
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
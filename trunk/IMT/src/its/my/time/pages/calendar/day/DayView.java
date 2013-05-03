package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
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

	private final GregorianCalendar cal;
	private List<EventBaseBean> events;
	private LinearLayout llEvent;

	protected boolean isFinished;
	private LinearLayout view;
	private ScrollView mainScroll;

	private float ligneHeight;

	public DayView(Context context, Calendar cal) {
		super(context);
		this.cal = new GregorianCalendar(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0,
				0);
		this.firstCal = new GregorianCalendar(0, 0, 0, 8, 0);
	}

	@Override
	protected View createView() {
		this.view = (LinearLayout) inflate(getContext(),
				R.layout.activity_calendar_day, null);

		final ViewGroup lignes = ((ViewGroup) this.view
				.findViewById(R.id.activity_calendar_day_layoutHeure));
		for (int i = 0; i < lignes.getChildCount(); i++) {
			if (lignes.getChildAt(i).getClass().isAssignableFrom(Ligne.class)) {
				lignes.getChildAt(i).setOnLongClickListener(
						new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								final Ligne ligne = (Ligne) v;
								final Calendar hour = new GregorianCalendar(
										DayView.this.cal.get(Calendar.YEAR),
										DayView.this.cal.get(Calendar.MONTH),
										DayView.this.cal
												.get(Calendar.DAY_OF_MONTH),
										ligne.getHeure(), 0, 0);
								ActivityUtil.startEventActivity(getContext(),
										hour, false);
								return false;
							}
						});

			}
		}

		this.llEvent = (LinearLayout) this.view
				.findViewById(R.id.activity_calendar_day_layout_event);
		this.mainScroll = (ScrollView) this.view
				.findViewById(R.id.activity_calendar_day_mainscroll);

		createTabHeure();

		this.ligneHeight = getResources().getDimension(
				R.dimen.view_day_height_ligne_heure);

		final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				DayView.this.firstCal.add(Calendar.MINUTE, -30);
				final int scroll = ((int) (DayView.this.firstCal
						.get(Calendar.HOUR_OF_DAY) * DayView.this.ligneHeight + (((float) DayView.this.firstCal
						.get(Calendar.MINUTE) / 60) * DayView.this.ligneHeight)));
				DayView.this.mainScroll.smoothScrollBy(scroll, 0);
			}
		};
		this.view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
		return this.view;
	}

	@Override
	protected String getTopBarText() {
		return DateUtil.getLongDate(this.cal);
	}

	private void createTabHeure() {
		final Calendar calDeb = new GregorianCalendar(
				this.cal.get(Calendar.YEAR), this.cal.get(Calendar.MONTH),
				this.cal.get(Calendar.DAY_OF_MONTH),
				this.cal.get(Calendar.HOUR_OF_DAY),
				this.cal.get(Calendar.MINUTE), this.cal.get(Calendar.SECOND));
		calDeb.set(Calendar.HOUR_OF_DAY, 0);
		calDeb.set(Calendar.MINUTE, 0);
		calDeb.set(Calendar.SECOND, 0);

		final Calendar calFin = new GregorianCalendar(
				calDeb.get(Calendar.YEAR), calDeb.get(Calendar.MONTH),
				calDeb.get(Calendar.DAY_OF_MONTH),
				calDeb.get(Calendar.HOUR_OF_DAY), calDeb.get(Calendar.MINUTE),
				calDeb.get(Calendar.SECOND));
		calFin.add(Calendar.DAY_OF_MONTH, 1);
		this.events = new EventBaseRepository(getContext()).getAllEvents(calDeb, calFin);

		for (final EventBaseBean event : this.events) {
			if (this.firstCal == null || event.gethDeb().before(this.firstCal)) {
				this.firstCal = event.gethDeb();
			}
			addEventView(event);
		}
	}

	private EventLittleView addEventView(EventBaseBean event) {
		ColumnEvent column;
		for (int i = 0; i < this.llEvent.getChildCount(); i++) {
			column = (ColumnEvent) this.llEvent.getChildAt(i);
			final EventLittleView eventView = column.addEvent(event, this.cal);

			if (eventView != null) {
				final ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
				eventView.setOnLongClickListener(moveEventListener);
				final ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(
						eventView);
				eventView.getBottomDraggable().setOnLongClickListener(
						changeEventDurationLIstener);
				return eventView;
			}
		}
		column = new ColumnEvent(getContext());
		final EventLittleView eventView = column.addEvent(event, this.cal);

		final ListenerMoveEvent moveEventListener = new ListenerMoveEvent();
		eventView.setOnLongClickListener(moveEventListener);
		final ListenerChangeEventDuration changeEventDurationLIstener = new ListenerChangeEventDuration(
				eventView);
		eventView.getBottomDraggable().setOnLongClickListener(
				changeEventDurationLIstener);

		this.llEvent.addView(column);
		return eventView;
	}

	private EventLittleView addEventView(EventLittleView view) {
		ColumnEvent column;
		for (int i = 0; i < this.llEvent.getChildCount(); i++) {
			column = (ColumnEvent) this.llEvent.getChildAt(i);
			final EventLittleView eventView = column.addEvent(view);

			if (eventView != null) {
				return eventView;
			}
		}
		column = new ColumnEvent(getContext());
		final EventLittleView eventView = column.addEvent(view);

		this.llEvent.addView(column);
		return eventView;
	}

	private void reloadEventLittleView(EventLittleView eventView) {
		ColumnEvent column;
		final EventBaseBean event = eventView.getEvent();
		new EventBaseRepository(getContext()).update(event);
		for (int i = 0; i < this.llEvent.getChildCount(); i++) {
			column = (ColumnEvent) this.llEvent.getChildAt(i);
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
			this.draggedView = (EventLittleView) v;
			this.draggedView.changeDragged(true);
			DayView.this.mainScroll.requestDisallowInterceptTouchEvent(true);
			this.draggedView.setOnTouchListener(this);
			this.draggedView.bringToFront();
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) v.getLayoutParams();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (this.lastY != -100000) {
					final float topMargin = layout.topMargin + event.getRawY()
							- this.lastY;
					final int nextLigne = Math.round(topMargin
							/ (DayView.this.ligneHeight / 2));
					if (this.lastLigne != nextLigne) {
						this.lastLigne = nextLigne;

						layout.topMargin = (int) (this.lastLigne * (DayView.this.ligneHeight / 2));
						if (layout.topMargin < 0) {
							layout.topMargin = 0;
						}
						if (layout.topMargin + layout.height > DayView.this.mainScroll
								.getChildAt(0).getMeasuredHeight()) {
							layout.topMargin = DayView.this.mainScroll
									.getChildAt(0).getMeasuredHeight()
									- layout.height - 30;
						}
						if (layout.topMargin + layout.height > DayView.this.mainScroll
								.getScrollY()
								+ DayView.this.mainScroll.getMeasuredHeight()
								+ 10) {
							DayView.this.mainScroll.smoothScrollTo(
									0,
									layout.topMargin
											- DayView.this.mainScroll
													.getMeasuredHeight()
											+ layout.height + 10);
							layout.topMargin += 30;
						}
						if (layout.topMargin < DayView.this.mainScroll
								.getScrollY() - 10) {
							DayView.this.mainScroll.smoothScrollTo(0,
									(layout.topMargin) - 10);
							layout.topMargin -= 30;
						}
						this.draggedView.updateFromLayout(layout);
						this.lastY = (int) event.getRawY();
					}
				} else {
					this.lastY = (int) event.getRawY();
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				this.draggedView.changeDragged(false);
				this.draggedView.setOnTouchListener(null);
				this.lastY = -100000;
				this.lastLigne = -1;
				DayView.this.mainScroll.requestDisallowInterceptTouchEvent(false);
				reloadEventLittleView(this.draggedView);
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
			DayView.this.mainScroll.requestDisallowInterceptTouchEvent(true);
			v.setOnTouchListener(this);
			this.parent.bringToFront();
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			final RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) this.parent
					.getLayoutParams();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (this.lastY != -100000) {
					final float height = layout.height + event.getRawY()
							- this.lastY;
					final int nextLigne = Math
							.round((layout.topMargin + height)
									/ (DayView.this.ligneHeight / 2));
					if (this.lastLigne != nextLigne) {
						this.lastLigne = nextLigne;
						layout.height = (int) (this.lastLigne
								* (DayView.this.ligneHeight / 2) - layout.topMargin);
						if (layout.topMargin + layout.height > DayView.this.mainScroll
								.getChildAt(0).getMeasuredHeight()) {
							layout.height = DayView.this.mainScroll.getChildAt(
									0).getMeasuredHeight()
									- layout.topMargin - 30;
						}
						if (layout.topMargin + layout.height > DayView.this.mainScroll
								.getScrollY()
								+ DayView.this.mainScroll.getMeasuredHeight()
								+ 10) {
							DayView.this.mainScroll.smoothScrollTo(
									0,
									layout.topMargin
											- DayView.this.mainScroll
													.getMeasuredHeight()
											+ layout.height + 10);
							layout.height += 30;
						}
						if (layout.topMargin + layout.height < DayView.this.mainScroll
								.getScrollY() - 10) {
							DayView.this.mainScroll.smoothScrollTo(0,
									layout.topMargin + layout.height - 10);
							layout.height -= 30;
						}
						if (layout.height < DayView.this.ligneHeight / 2) {
							layout.height = (int) (DayView.this.ligneHeight / 2);
						}
						this.lastY = (int) event.getRawY();
						this.parent.updateFromLayout(layout);
					}
				} else {
					this.lastY = (int) event.getRawY();
				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setOnTouchListener(null);
				this.lastY = -100000;
				this.lastLigne = -1;
				DayView.this.mainScroll
						.requestDisallowInterceptTouchEvent(false);
				reloadEventLittleView(this.parent);
			}
			return true;
		}
	}
}
package its.my.time.pages.calendar.month;

import its.my.time.R;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.pages.calendar.base.BaseFragment.OnViewCreated;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
import its.my.time.util.ColorUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.IdUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.MonthDisplayHelper;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MonthView extends BaseView implements OnDayClickListener {

	private MonthDisplayHelper helper;
	private OnDayClickListener externListener;

	private SparseArray<ViewGroup> daysViews;
	private SparseArray<String> accountsColors;

	public MonthView(Context context, Calendar cal, OnDayClickListener listener) {
		this(context, cal, listener, null);
	}

	public MonthView(Context context, final Calendar cal, final OnDayClickListener listener,OnViewCreated onViewCreated) {
		super(context, onViewCreated);
		final Thread t = new Thread(new Runnable() {
			@Override public void run() {
				createTabDay(MonthView.this);
				onViewCreated();
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				helper = new MonthDisplayHelper(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH) + 1, DateUtil.FIRST_DAY);
				externListener = listener;
				daysViews = new SparseArray<ViewGroup>();
				final View v = inflate(getContext(),R.layout.activity_calendar_month, null);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						addView(v);
						t.start();
					}
				});

			}
		}).start();
	}

	@Override
	protected String getTopBarText() {
		return DateUtil.getMonth(helper.getYear(), helper.getMonth());
	}

	@SuppressWarnings("deprecation")
	private void addStyleToday(View view) {
		Drawable dr = view.getBackground();
		dr = dr.mutate();
		dr.setColorFilter(Color.parseColor("#FFFFCC"), Mode.MULTIPLY);
		view.setBackgroundDrawable(dr);
	}

	private void createTabDay(final ViewGroup view) {

		boolean isInMois = false;
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		final LinearLayout tabJour = (LinearLayout) view.findViewById(R.id.llTabJour);
		LinearLayout ligne;
		int nbMois = -1;
		for (int i = 0; i < 5; i++) {
			ligne = (LinearLayout) tabJour.getChildAt(i);

			// cellule du numério de semaine
			cal.set(helper.getYear(), helper.getMonth(),
					helper.getDayAt(i, 0));

			if (!isInMois) {
				if (i <= 1) {
					cal.add(Calendar.MONTH, -1);
				} else {
					cal.add(Calendar.MONTH, 1);
				}
			}

			final TextView txtVwSem = (TextView) ligne.getChildAt(0);
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					txtVwSem.setText(String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));	
				}
			});
			txtVwSem.setId(IdUtil.getWeekId(cal.get(Calendar.YEAR),cal.get(Calendar.WEEK_OF_YEAR)));

			// liste des jours
			for (int j = 1; j < 8; j++) {
				if (helper.getDayAt(i, j - 1) == 1) {
					nbMois++;
					isInMois = !isInMois;
				}

				final RelativeLayout layoutDay = (RelativeLayout) ligne.getChildAt(j);
				layoutDay.setEnabled(isInMois);
				initialiseListeners(layoutDay, helper.getDayAt(i, j - 1));
				if(isInMois) {
					daysViews.put(helper.getDayAt(i, j - 1), layoutDay);
				}

				final GregorianCalendar calDeb = new GregorianCalendar(
						this.helper.getYear(), this.helper.getMonth(),
						this.helper.getDayAt(i, j - 1), 0, 0, 0);
				calDeb.add(Calendar.MONTH, nbMois);
				final GregorianCalendar calListener = (GregorianCalendar) calDeb.clone();
				calListener.set(Calendar.HOUR_OF_DAY, 8);
				calListener.set(Calendar.HOUR_OF_DAY, 10);
				final TextView txtVwDay = (TextView) layoutDay.findViewById(R.id.activity_calendar_month_day_label);
				txtVwDay.setId(IdUtil.getDayId(helper.getYear(),helper.getMonth(), helper.getDayAt(i, j - 1)));


				final boolean dayIsInMois = isInMois;
				final String dayText = String.valueOf(helper.getDayAt(i, j - 1));
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						txtVwDay.setEnabled(dayIsInMois);
						txtVwDay.setText(dayText);
						if (!dayIsInMois) {
							txtVwDay.setTextColor(getResources().getColor(R.color.grey));
						}
					}
				});

				final GregorianCalendar today = new GregorianCalendar();

				if (today.get(Calendar.YEAR) == helper.getYear()
						&& today.get(Calendar.MONTH) == helper.getMonth()
						&& today.get(Calendar.DAY_OF_MONTH) == helper
						.getDayAt(i, j - 1) && isInMois) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							addStyleToday(layoutDay);
						}
					});
				}
			}
		}
	}

	private QuickAction quickAction;
	@Override
	public void onDayClickListener(View v, final GregorianCalendar day,final List<EventBaseBean> events) {
		if(events != null && events.size() > 0) {
			if(quickAction == null) {
				quickAction = new QuickAction(getContext());
				quickAction.setAnimStyle(QuickAction.ANIM_FADE);
			} else {
				quickAction.removeAllItems();
			}
			List<ActionItem> items = quickAction.getActionItems();
			int i = 0;
			for (EventBaseBean eventBaseBean : events) {
				Resources r = getResources();
				int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, r.getDisplayMetrics());
				int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
				GradientDrawable background = (GradientDrawable) getResources().getDrawable(ColorUtil.getDrawableRes(accountsColors.get(eventBaseBean.getCid())));
				background.setSize(width, height);
				ActionItem item = new ActionItem(i,eventBaseBean.getTitle(), background);
				item.setSticky(false);
				quickAction.addActionItem(item);
				i++;
			}
			if(items != null && items.size() > 0) {
				ActionItem item = new ActionItem(-1, "Ouvrir", getResources().getDrawable(android.R.drawable.ic_menu_more));
				item.setSticky(true);
				quickAction.addActionItem(item);
				quickAction.setOnActionItemClickListener(new OnActionItemClickListener() {
					@Override
					public void onItemClick(QuickAction source, int pos, int actionId) {
						if(actionId == -1) {
							if (externListener != null) {
								source.dismiss();
								externListener.onDayClickListener(null,day,null);
							}
						} else {
							source.dismiss();
							EventBaseBean eventBaseBean = events.get(pos);
							ActivityUtil.startEventActivity(getContext(), eventBaseBean.getId(), eventBaseBean.getTypeId());
						}
					}
				});
				quickAction.show(v);
			}
		} else {
			externListener.onDayClickListener(v, day,events);
		}
	}

	@Override
	public void onDayLongClickListener(View v, GregorianCalendar day, List<EventBaseBean> events) {
		if (externListener != null) {
			externListener.onDayLongClickListener(v, day,events);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public View addEvent(EventBaseBean event, String color, int visibility) {
		View res = null;
		if(accountsColors == null) {
			accountsColors = new SparseArray<String>();
		}
		accountsColors.put(event.getCid(), color);
		for(int i = 0; i< daysViews.size(); i++) {
			if(DateUtil.isInDay(event, new GregorianCalendar(helper.getYear(), helper.getMonth()+1, i))) {
				final ViewGroup parent =  daysViews.get(i);
				List<EventBaseBean> events = (ArrayList<EventBaseBean>) parent.getTag();
				LinearLayout layoutEvents = (LinearLayout)parent.findViewById(R.id.activity_calendar_month_day_events);
				boolean alreadyHave = false;
				if(events == null) {
					events = new ArrayList<EventBaseBean>();
				}
				for (EventBaseBean eventBaseBean : events) {
					if(eventBaseBean.getCid() == event.getCid()) {
						for(int j = 0; j < layoutEvents.getChildCount(); j++) {
							final View child = layoutEvents.getChildAt(j);
							if((Integer)child.getTag() == event.getCid()) {	
								alreadyHave = true;
								res = child;
							}
						}
					}
				}

				events.add(event);
				parent.setTag(events);
				initialiseListeners(parent,event.gethDeb().get(Calendar.DAY_OF_MONTH));		
				if(!alreadyHave) {
					TextView textView = new TextView(getContext());
					textView.setText(" ");
					textView.setVisibility(visibility);
					textView.setBackgroundResource(ColorUtil.getDrawableRes(color));		
					textView.setPadding(5, 5, 5, 5);
					textView.setTextColor(Color.WHITE);
					textView.setTag(event.getCid());
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					Resources r = getResources();
					int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
					params.setMargins(margin,0,margin,0);
					textView.setLayoutParams(params);
					layoutEvents.addView(textView,0);
					layoutEvents.setWeightSum(events.size());
					res = textView;
				}
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private void initialiseListeners(final ViewGroup parent, int day) {
		final GregorianCalendar calListener = new GregorianCalendar(helper.getYear(), helper.getMonth(), day);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDayClickListener(v, calListener, (List<EventBaseBean>) parent.getTag());
			}
		});

		parent.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				onDayLongClickListener(v, calListener, (List<EventBaseBean>) parent.getTag());
				return false;
			}
		});
	}

}

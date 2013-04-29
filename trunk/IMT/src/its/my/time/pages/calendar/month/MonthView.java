package its.my.time.pages.calendar.month;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.compte.CompteRepository.OnCompteChangedListener;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.IdUtil;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;
import net.londatiga.android.QuickAction.OnActionItemClickListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.MonthDisplayHelper;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MonthView extends BaseView implements OnCompteChangedListener, OnDayClickListener {

	private final MonthDisplayHelper helper;
	private final OnDayClickListener externListener;
	private SparseArray<CompteBean> comptes;
	private HashMap<ViewGroup,List<EventBaseBean>> mapEventByDay;
	private CompteRepository compteRepo;
	private EventBaseRepository eventBaseRepo;

	public MonthView(Context context, Calendar cal, OnDayClickListener listener) {
		super(context);
		this.helper = new MonthDisplayHelper(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), DateUtil.FIRST_DAY);
		this.externListener = listener;

		if(eventBaseRepo == null) {
			eventBaseRepo = new EventBaseRepository(context);
		}
		if(compteRepo == null) {
			compteRepo = new CompteRepository(getContext());
		}
		compteRepo.addOnCompteCHangedListener(this);
		List<CompteBean> listComptes = compteRepo.getAllCompteByUid(PreferencesUtil.getCurrentUid());
		compteRepo.addOnCompteCHangedListener(this);
		if(comptes == null) {
			comptes = new SparseArray<CompteBean>();
			for (CompteBean compteBean : listComptes) {
				comptes.put(compteBean.getId(), compteBean);
			}
			mapEventByDay = new HashMap<ViewGroup, List<EventBaseBean>>();
		}
	}

	@Override
	protected View createView() {

		final LinearLayout view = (LinearLayout) inflate(getContext(),R.layout.activity_calendar_month, null);
		createTabDay(view);

		return view;
	}

	@Override
	protected String getTopBarText() {
		return DateUtil.getMonth(this.helper.getYear(), this.helper.getMonth());
	}

	@SuppressWarnings("deprecation")
	private void addStyleToday(View view) {
		Drawable dr = view.getBackground();
		dr = dr.mutate();
		dr.setColorFilter(Color.parseColor("#FFFFCC"), Mode.MULTIPLY);
		view.setBackgroundDrawable(dr);
	}

	private void createTabDay(LinearLayout view) {

		boolean isInMois = false;
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		final LinearLayout tabJour = (LinearLayout) view
				.findViewById(R.id.llTabJour);
		LinearLayout ligne;
		RelativeLayout layoutDay;
		int nbMois = -1;
		for (int i = 0; i < 5; i++) {
			ligne = (LinearLayout) tabJour.getChildAt(i);

			// cellule du numério de semaine
			cal.set(this.helper.getYear(), this.helper.getMonth(),
					this.helper.getDayAt(i, 0));

			if (!isInMois) {
				if (i <= 1) {
					cal.add(Calendar.MONTH, -1);
				} else {
					cal.add(Calendar.MONTH, 1);
				}
			}

			final TextView txtVwSem = (TextView) ligne.getChildAt(0);
			txtVwSem.setText(String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));
			txtVwSem.setId(IdUtil.getWeekId(cal.get(Calendar.YEAR),cal.get(Calendar.WEEK_OF_YEAR)));

			// liste des jours
			for (int j = 1; j < 8; j++) {
				// detecte si le jour est dans le mois en cours
				if (this.helper.getDayAt(i, j - 1) == 1) {
					nbMois++;
					isInMois = !isInMois;
				}

				layoutDay = (RelativeLayout) ligne.getChildAt(j);
				final TextView txtVwDay = (TextView) layoutDay.findViewById(R.id.activity_calendar_month_day_label);
				txtVwDay.setId(IdUtil.getDayId(this.helper.getYear(),this.helper.getMonth(), this.helper.getDayAt(i, j - 1)));
				txtVwDay.setEnabled(isInMois);
				txtVwDay.setText(String.valueOf(this.helper.getDayAt(i, j - 1)));
				if (!isInMois) {
					txtVwDay.setTextColor(getResources().getColor(R.color.grey));
				}

				final GregorianCalendar today = new GregorianCalendar();

				if (today.get(Calendar.YEAR) == this.helper.getYear()
						&& today.get(Calendar.MONTH) == this.helper.getMonth()
						&& today.get(Calendar.DAY_OF_MONTH) == this.helper
						.getDayAt(i, j - 1) && isInMois) {
					addStyleToday(layoutDay);
				}

				//TODO gros soucis des la recuperation du mois c'est pas le BON!!
				final GregorianCalendar calDeb = new GregorianCalendar(
						this.helper.getYear(), this.helper.getMonth(),
						this.helper.getDayAt(i, j - 1), 0, 0, 0);
				calDeb.add(Calendar.MONTH, nbMois);
				final GregorianCalendar calFin = (GregorianCalendar) calDeb.clone();
				calFin.add(Calendar.DAY_OF_MONTH, 1);

				final List<EventBaseBean> listEventsFinal = eventBaseRepo.getAllEvents(calDeb, calFin);

				mapEventByDay.put(layoutDay, listEventsFinal);

				final GregorianCalendar calListener = (GregorianCalendar) calDeb.clone();
				calListener.set(Calendar.HOUR_OF_DAY, 8);
				calListener.set(Calendar.HOUR_OF_DAY, 10);

				OnClickListener clickListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						onDayClickListener(v, calListener, listEventsFinal);
					}
				};

				OnLongClickListener longCLickListener = new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						onDayLongClickListener(v, calListener, listEventsFinal);
						return false;
					}
				};
				layoutDay.setOnClickListener(clickListener );
				layoutDay.setOnLongClickListener(longCLickListener);
			}
		}

		reloadEvents();
	}

	@Override public void onCompteAdded(CompteBean compte) {}
	@Override public void onCompteRemoved(CompteBean compte) {}

	@Override
	public void onCompteUpdated(CompteBean compte) {
		comptes.remove(compte.getId());
		comptes.put(compte.getId(), compte);
		reloadEvents();
	}

	private void reloadEvents() {
		for(Entry<ViewGroup, List<EventBaseBean>> entry : mapEventByDay.entrySet()) {
			ViewGroup v = entry.getKey();
			List<EventBaseBean> events = entry.getValue();
			HashMap<CompteBean, List<EventBaseBean>> mapEventsByCompte = new HashMap<CompteBean, List<EventBaseBean>>();
			if(events != null) {
				int key = 0;
				for(int i = 0; i < comptes.size(); i++) {
					key = comptes.keyAt(i);
					CompteBean compte = comptes.get(key);
					if(compte.isShowed()) {
						mapEventsByCompte.put(compte, new ArrayList<EventBaseBean>());
					}
				}
				for (int nbEvents = 0; nbEvents < events.size(); nbEvents++) {
					EventBaseBean eventBaseBean = events.get(nbEvents);
					CompteBean compteEvent = comptes.get(eventBaseBean.getCid());
					if(compteEvent.isShowed()) {
						mapEventsByCompte.get(compteEvent).add(eventBaseBean);
					}
				}
			}
			LinearLayout layoutEvents = (LinearLayout)v.findViewById(R.id.activity_calendar_month_day_events);
			layoutEvents.removeAllViews();
			int weight = 0;
			for(Entry<CompteBean, List<EventBaseBean>> entry1 : mapEventsByCompte.entrySet()) {
				events = entry1.getValue();
				if(events != null && events.size() > 0) {
					TextView textView = new TextView(getContext());
					textView.setText(" ");


					textView.setBackgroundDrawable(createCompteDrawable(entry1.getKey()));		
					textView.setPadding(5, 5, 5, 5);
					textView.setTextColor(Color.WHITE);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					Resources r = getResources();
					int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
					params.setMargins(margin,0,margin,0);
					textView.setLayoutParams(params);
					layoutEvents.addView(textView);
					weight++;
				}
			}
			layoutEvents.setWeightSum(weight);
		}
	}

	private ShapeDrawable createCompteDrawable(CompteBean compte) {
		ShapeDrawable background = new ShapeDrawable();
		float[] radii = new float[8];
		radii[0] = 5;
		radii[1] = 5;
		radii[2] = 5;
		radii[3] = 5;
		radii[4] = 5;
		radii[5] = 5;
		radii[6] = 5;
		radii[7] = 5;
		background.setShape(new RoundRectShape(radii, null, null));
		int color = compte.getColor();
		background.getPaint().setColor(color);
		return background;
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
			int i = 0;
			for (EventBaseBean eventBaseBean : events) {
				if(comptes.get(eventBaseBean.getCid()).isShowed()) {
					ShapeDrawable background = createCompteDrawable(comptes.get(eventBaseBean.getCid()));
					Resources r = getResources();
					int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, r.getDisplayMetrics());
					int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
					background.setIntrinsicHeight(height);
					background.setIntrinsicWidth(width);
					ActionItem item = new ActionItem(i,eventBaseBean.getTitle(), background);
					item.setSticky(false);
					quickAction.addActionItem(item);
				}
				i++;
			}
			List<ActionItem> items = quickAction.getActionItems();
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
}

package its.my.time.pages.calendar.day;

import its.my.time.R;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.pages.calendar.base.BaseView;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.EventUtil;
import its.my.time.util.ViewUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DayViewOld extends BaseView{

	private GregorianCalendar cal;
	private List<EventBean> events;
	private HashMap<Integer, EventParams> eventsParams;
	private HashMap<Integer, View> eventsView;
	private float ligneHeight;
	private ArrayList<ArrayList<Integer>> listePoids;
	private float poidsMin;

	private View.OnClickListener onEventClickLIstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ActivityUtil.startEventActivity(getContext(), v.getId());
		}
	};
	
	private int totWidth;

	public DayViewOld(Context context, Calendar cal) {
		super(context);
		this.cal = new GregorianCalendar(cal.get(GregorianCalendar.YEAR), cal.get(GregorianCalendar.MONTH), cal.get(GregorianCalendar.DAY_OF_MONTH), 0, 0, 0);
	}

	@Override
	protected View createView() {
		LinearLayout view = (LinearLayout) inflate(getContext(), R.layout.activity_calendar_day, null);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		totWidth = displaymetrics.widthPixels;
		ligneHeight = getContext().getResources().getDimension(R.dimen.view_day_height_ligne_heure);

		initialiseListeHeure(view);
		createTabHeure();

		showEvents(view);
		
		return view;
	}
	
	@Override
	protected String getTopBarText() {
		return DateUtil.getLongDate(cal);
	}

	private void initialiseListeHeure(ViewGroup view) {
		LinearLayout llHeure = (LinearLayout)view.findViewById(R.id.liste_ligne_heure);
		TextView mTextView;
		for(int i = 0; i < llHeure.getChildCount(); i++) {
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
			if(i == 0) {
				mTextView.setText("Minuit");
			} else if(i == 12) {
				mTextView.setText("Midi");
			} else {
				mTextView.setText(i + "h");
			}
			
		}
	}

	private void addStyle(TextView mEventVw, Integer color) {
		Drawable dr = getResources().getDrawable(R.drawable.style_event);
		dr.setColorFilter(color, Mode.MULTIPLY);
		dr.setAlpha(200);
		mEventVw.setBackgroundDrawable(dr);
	}

	private void calculerPoids() {
		eventsParams = new HashMap<Integer, EventParams>();
		HashMap<Integer, Boolean> eventsUnderUses = new HashMap<Integer, Boolean>();
		HashMap<Integer, Boolean> eventsCoteUses = new HashMap<Integer, Boolean>();
		listePoids = new ArrayList<ArrayList<Integer>>();
		int currentListe = 0;
		EventParams params;

		boolean isFirstOk = false;
		int index = 0;
		boolean isUnder;
		boolean isCote;
		boolean isNew = false;
		for (EventBean event : events) {
			params = new EventParams();
			float nbH = DateUtil.getNbHeure(event.gethDeb(), event.gethFin(), cal);
			params.height = ((int)(nbH * ligneHeight));
			if(DateUtil.isInDay(event.gethDeb(), cal)) {
				params.margeTop = ((int) (event.gethDeb().get(GregorianCalendar.HOUR_OF_DAY)  * ligneHeight + (((float)event.gethDeb().get(GregorianCalendar.MINUTE)/ 60) * ligneHeight)));
			}
			params.indexListePoids = currentListe;
			boolean isCoteUsed = false;
			if(!isFirstOk) {
				listePoids.add(new ArrayList<Integer>());
				isFirstOk = true;
				params.indexPoids.add(index);
				listePoids.get(0).add(index, 1);
				params.margeLeft = ViewUtil.HEURE_WIDTH;
				index++;
			} else {
				isUnder = false;
				isCote = false;
				ArrayList<Integer> listePoidsCote = new ArrayList<Integer>();
				ArrayList<Integer> listePoidsSous = new ArrayList<Integer>();
				for (EventBean eventTmp : events) {
					if(eventsParams.containsKey(eventTmp.getId())) {
						if(EventUtil.isUnder(event, eventTmp)
								&& eventsUnderUses.get(eventTmp.getId()) == false) {
							params.margeLeft = eventsParams.get(eventTmp.getId()).margeLeft;
							if(params.indexListePoids == eventsParams.get(eventTmp.getId()).indexListePoids) {
								if(params.poidsGauche == null || isUnder == false) {
									params.poidsGauche = new ArrayList<Integer>();
									params.poidsGauche.addAll(eventsParams.get(eventTmp.getId()).poidsGauche);
								}
								if(totalPoids(currentListe, params.poidsGauche) + totalPoids(currentListe, listePoidsSous) == totalPoids(eventsParams.get(eventTmp.getId()).indexListePoids, eventsParams.get(eventTmp.getId()).poidsGauche) ) {
									eventsUnderUses.put(eventTmp.getId(),true);
									listePoidsSous.addAll(eventsParams.get(eventTmp.getId()).indexPoids);
								}
								isUnder = true;
							}
						} else if(EventUtil.isAtSameTime(event, eventTmp)
								&& isUnder) {
							isCoteUsed = true;
							isCote = true; 
						} else if(EventUtil.isAtSameTime(event, eventTmp)) {
							isCote = true;
							if(isUnder == false) {
								if(params.poidsGauche == null) {params.poidsGauche = new ArrayList<Integer>();}
								params.poidsGauche.addAll(eventsParams.get(eventTmp.getId()).indexPoids);
								if(eventsCoteUses.get(eventTmp.getId()) == false
										&& listePoidsCote.size() == 0) {
									eventsCoteUses.put(eventTmp.getId(),true);
									params.margeLeft = eventsParams.get(eventTmp.getId()).margeLeft;
									listePoidsCote = eventsParams.get(eventTmp.getId()).indexPoids;
								}
							}
						}
					}
				}
				if (isUnder && !isCote) {
					isNew = true;
					params.poidsGauche.clear();
					currentListe++;
					index = 0;
					params.indexListePoids = currentListe;
					params.indexPoids.add(index);
					listePoids.add(new ArrayList<Integer>());
					listePoids.get(currentListe).add(index,1);
					params.margeLeft = ViewUtil.HEURE_WIDTH;
					index++;
				} else if(isUnder && isCote) {
					params.indexPoids = listePoidsSous;
				} else {
					params.indexPoids = listePoidsCote;
					for (Integer x : params.indexPoids) {
						listePoids.get(currentListe).set(x, listePoids.get(currentListe).get(x) + 1);
					}
				}
				if(!isNew) {
					calculerPoidsMin(currentListe);
				}
			}
			eventsUnderUses.put(event.getId(), false);
			eventsCoteUses.put(event.getId(), isCoteUsed);
			eventsParams.put(event.getId(),params);
		}
	}

	private void calculerPoidsMin(int indexListePoids) {
		poidsMin = 0;
		for (int poids : listePoids.get(indexListePoids)) {
			if(poids > 1 && (poidsMin == 0 || poidsMin > poids)) {
				poidsMin = poids;
			}
		}
		if(poidsMin == 0) {
			poidsMin = 1;
		}
	}

	private void createTabHeure() {

		GregorianCalendar calDeb = new GregorianCalendar(
				cal.get(GregorianCalendar.YEAR),
				cal.get(GregorianCalendar.MONTH),
				cal.get(GregorianCalendar.DAY_OF_MONTH),
				0,
				0,
				0);
		GregorianCalendar calFin = new GregorianCalendar(
				cal.get(GregorianCalendar.YEAR),
				cal.get(GregorianCalendar.MONTH),
				cal.get(GregorianCalendar.DAY_OF_MONTH),
				0,
				0,
				0);
		calFin.add(GregorianCalendar.DAY_OF_MONTH, 1);

		List<EventBean> listEventsTmp;
		events = new ArrayList<EventBean>();
		List<Long> listeCompteShowed = new ArrayList<Long>();
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
		for (int i = 0;  i < 6; i++) {
			bean = new EventBean();
			bean.setId(i);
			calDeb2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),8,0);
			bean.sethDeb(calDeb2);
			calFin2 = new GregorianCalendar(cal.get(GregorianCalendar.YEAR),cal.get(GregorianCalendar.MONTH),cal.get(GregorianCalendar.DAY_OF_MONTH),9+i,0);
			bean.sethFin(calFin2);
			events.add(bean);
		}
		calculerPoids();
	}

	private void showEvents(ViewGroup view) {

		eventsView = new HashMap<Integer, View>();

		TextView mEventVw;
		EventBean event;
		for (int i = 0; i < events.size(); i++) {
			event = events.get(i);
			mEventVw = new TextView(getContext());
			mEventVw.setId(event.getId());
			mEventVw.setText(event.getTitle());
			mEventVw.setSingleLine(true);
			mEventVw.setEllipsize(TruncateAt.END);
			mEventVw.setBackgroundColor(Color.BLUE);
			addStyle(mEventVw, Color.BLUE);
			mEventVw.setBackgroundColor(Color.BLUE);
			eventsView.put(event.getId(), mEventVw);
			RelativeLayout.LayoutParams mLp = eventsParams.get(event.getId()).createLayout();
			((RelativeLayout)view.findViewById(R.id.rLlHeure)).addView(mEventVw, mLp);
			mEventVw.bringToFront();
			mEventVw.setOnClickListener(onEventClickLIstener);
		}
	}

	private int totalPoids(int indexListePoids, ArrayList<Integer> indexPoids) {
		int poids = 0;
		for (int i : indexPoids) {
			poids += listePoids.get(indexListePoids).get(i);
		}
		return poids;
	}


	private class EventParams{

		private int height;
		private int indexListePoids;

		private ArrayList<Integer> indexPoids;

		private int margeLeft;
		public int margeTop;
		private ArrayList<Integer> poidsGauche;
		private int width;

		public EventParams() {
			indexPoids = new ArrayList<Integer>();
			poidsGauche = new ArrayList<Integer>();
		}

		private RelativeLayout.LayoutParams createLayout(){
			float poids = 0;
			calculerPoidsMin(indexListePoids);

			poids = totalPoids(indexListePoids,indexPoids);
			RelativeLayout.LayoutParams mLp;
			if(poids > 1) {
				width = (int)(((poids / poidsMin) / poidsMin) * (totWidth - ViewUtil.HEURE_WIDTH));
			} else {
				width = totWidth - ViewUtil.HEURE_WIDTH;
			}
			mLp = new RelativeLayout.LayoutParams(width,height);
			mLp.topMargin = margeTop; 
			poids = totalPoids(indexListePoids,poidsGauche);
			mLp.leftMargin = margeLeft + (int)(((poids / poidsMin) / poidsMin) * (totWidth));

			return mLp;
		}
	}

}
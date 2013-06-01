package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.participation.ParticipationBean;
import its.my.time.data.bdd.events.plugins.participation.ParticipationRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.ViewUtil;
import its.my.time.view.Switcher;
import its.my.time.view.Switcher.OnStateChangedListener;
import its.my.time.view.date.DateButton;
import its.my.time.view.date.TimeButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsFragment extends BasePluginFragment {

	private static final String KEY_BUNDLE_DATE_DEB = "KEY_BUNDLE_DATE_DEB";
	private static final String KEY_BUNDLE_DATE_FIN = "KEY_BUNDLE_DATE_FIN";
	private static final String KEY_BUNDLE_TITLE = "KEY_BUNDLE_TITLE";
	private static final String KEY_BUNDLE_DETAILS = "KEY_BUNDLE_DETAILS";
	private static final String KEY_BUNDLE_ALL_DAY = "KEY_BUNDLE_ALL_DAY";
	private static final String KEY_BUNDLE_COMPTE = "KEY_BUNDLE_COMPTE";
	private static final String KEY_BUNDLE_RECURRENCE = "KEY_BUNDLE_RECURRENCE";
	private static final String KEY_BUNDLE_PARTICIPATION = "KEY_BUNDLE_PARTICIPATION";

	private TimeButton mTextHeureDeb;
	private TimeButton mTextHeureFin;
	private DateButton mTextJourDeb;
	private DateButton mTextJourFin;
	private Switcher mSwitchAllDay;
	private ArrayList<String> mListCompteLabels;
	private Spinner mSpinnerCompte;
	private Spinner mSpinnerRecurrence;
	private Spinner mSpinnerParticipation;
	private TextView mTextDetails;
	private String[] array_recurrence;
	private ParticipationBean participationBean;
	private ParticipationRepository participationRepo;

	private List<CompteBean> mListCompte;

	private View view;

	private EditText mTextTitle;

	//private int typeEvent;
	protected static Bundle state;

	public DetailsFragment() {
		//typeEvent = Types.Event.BASE;
	}

	public DetailsFragment(int typeEvent) {
		//typeEvent = typeEvent;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		participation = new Participation[] {
				new Participation(0, getResources().getString(R.string.invite)),
				new Participation(1, getResources().getString(R.string.participe)),
				new Participation(2, getResources().getString(R.string.participe_pas)),
				new Participation(3, getResources().getString(R.string.participe_nsp))
		};

		int eid = getParentActivity().getEvent().getId();
		long uid = PreferencesUtil.getCurrentUid();

		participationRepo = new ParticipationRepository(getActivity());
		participationBean = participationRepo.getByUidEid(eid, uid);

		if(participationBean == null) {
			participationBean = new ParticipationBean();
			participationBean.setParticipation(-1);
			participationBean.setEid(eid);
			participationBean.setUid(uid);
		}

		view = inflater.inflate(R.layout.activity_event_details, null);
		view.setBackgroundColor(Color.WHITE);

		final View customView = getCustomView();
		if (customView != null) {
			((FrameLayout) view.findViewById(R.id.include)).addView(customView);
		}

		mTextTitle = (EditText) view.findViewById(R.id.activity_event_details_text_title);
		mTextJourDeb = (DateButton) view.findViewById(R.id.activity_event_details_text_ddeb);
		mTextJourFin = (DateButton) view.findViewById(R.id.activity_event_details_text_dfin);
		mTextHeureDeb = (TimeButton) view				.findViewById(R.id.activity_event_details_text_hdeb);
		mTextHeureFin = (TimeButton) view
				.findViewById(R.id.activity_event_details_text_hfin);
		mSpinnerCompte = (Spinner) view
				.findViewById(R.id.activity_event_details_spinner_compte);
		mSpinnerRecurrence = (Spinner) view
				.findViewById(R.id.activity_event_details_spinner_recurrence);
		mSpinnerParticipation = (Spinner) view
				.findViewById(R.id.activity_event_details_spinner_participation);
		mTextDetails = (TextView) view
				.findViewById(R.id.activity_event_details_text_details);
		mSwitchAllDay = (Switcher) view
				.findViewById(R.id.activity_event_details_switcher_allDay);

		if (state == null) {
			initialiseValuesFromEvent();
		} else {
			initialiseValueFromInstance();
		}
		initialiseActions();

		ViewUtil.enableAllView(view, false);

		return view;
	}

	protected View getCustomView() {
		return null;
	}

	public void initialiseValuesFromEvent() {
		mTextTitle.setText(getParentActivity().getEvent().getTitle());
		mTextJourDeb.setDate(getParentActivity().getEvent().gethDeb());
		mTextHeureDeb.setDate(getParentActivity().getEvent().gethDeb());
		mTextJourFin = (DateButton) view.findViewById(R.id.activity_event_details_text_dfin);

		if (getParentActivity().getEvent().gethFin() != null) {
			mTextHeureFin.setDate(getParentActivity().getEvent().gethFin());
			mTextJourFin.setDate(getParentActivity().getEvent().gethFin());
		}
		mTextDetails.setText(getParentActivity().getEvent().getDetails());
		if (getParentActivity().getEvent().isAllDay()) {
			mSwitchAllDay.changeState(getParentActivity().getEvent().isAllDay(), true);
		}
	}

	public void initialiseValueFromInstance() {
		mTextTitle.setText(state.getString(KEY_BUNDLE_TITLE));

		Calendar cal = DateUtil.getDateFromISO(state
				.getString(KEY_BUNDLE_DATE_DEB));
		mTextJourDeb.setDate(cal);
		mTextHeureDeb.setDate(cal);

		cal = DateUtil.getDateFromISO(state.getString(KEY_BUNDLE_DATE_FIN));
		mTextHeureFin.setDate(cal);
		mTextJourFin.setDate(cal);

		mTextDetails.setText(state.getString(KEY_BUNDLE_DETAILS));
		if (getParentActivity().getEvent().isAllDay()) {
			mSwitchAllDay.changeState(state.getBoolean(KEY_BUNDLE_ALL_DAY), false);
		}
		mSpinnerCompte.setSelection(state.getInt(KEY_BUNDLE_COMPTE));
		mSpinnerRecurrence.setSelection(state.getInt(KEY_BUNDLE_RECURRENCE));
		mSpinnerParticipation.setSelection(state.getInt(KEY_BUNDLE_PARTICIPATION));
	}

	public void initialiseActions() {
		mListCompte = new CompteRepository(getActivity()).getAllByUid(PreferencesUtil.getCurrentUid());
		mListCompteLabels = new ArrayList<String>();
		int comptePosition = 0;
		int i = 0;
		for (final CompteBean compte : mListCompte) {
			if (compte.getId() == getParentActivity().getEvent().getCid()) {
				comptePosition = i;
			}
			i++;
			mListCompteLabels.add(compte.getTitle());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,mListCompteLabels);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerCompte.setAdapter(adapter);
		mSpinnerCompte.setSelection(comptePosition);
		mSpinnerCompte.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> container,
					View view, int position, long id) {
				getParentActivity().getEvent().setCid(mListCompte.get(position).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> container) {
				getParentActivity().getEvent().setCid(-1);
			}
		});

		array_recurrence = getResources().getStringArray(
				R.array.array_recurrence);
		final ArrayAdapter<Object> adapter_recurrence = new CustomAdapter(
				getActivity(), android.R.layout.simple_spinner_item,
				array_recurrence, 0);
		adapter_recurrence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerRecurrence.setAdapter(adapter_recurrence);


		final ArrayAdapter<Object> adapter_participation = new CustomAdapter(getActivity(), android.R.layout.simple_spinner_item,
				participation, 0);
		adapter_participation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerParticipation.setAdapter(adapter_participation);
		mSpinnerParticipation.setSelection((int)participationBean.getParticipation());
		mSpinnerParticipation.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> container, View view, int position, long id) {
				participationBean.setParticipation((int)id);
			}

			@Override
			public void onNothingSelected(AdapterView<?> container) {}
		});


		mSwitchAllDay.setOnStateChangedListener(new OnStateChangedListener() {
			@Override
			public void onStateCHangedListener(Switcher switcher,
					boolean isChecked) {
				if (isChecked == true) {
					mTextJourDeb.setEnabled(false);
					mTextJourFin.setEnabled(false);
					mTextHeureDeb.setEnabled(false);
					mTextHeureFin.setEnabled(false);

					mTextJourFin.setText(mTextJourDeb.getText());
					Calendar calDeb = getParentActivity().getEvent().gethDeb();
					calDeb.set(Calendar.HOUR_OF_DAY, 0);
					calDeb.set(Calendar.MINUTE, 0);
					calDeb.set(Calendar.SECOND, 0);
					mTextHeureDeb.setDate(calDeb);

					Calendar calFin = getParentActivity().getEvent().gethFin();
					calFin.set(Calendar.HOUR_OF_DAY, 23);
					calFin.set(Calendar.MINUTE, 59);
					calFin.set(Calendar.SECOND, 59);
					mTextHeureFin.setDate(calFin);
				} else {
					mTextJourDeb.setEnabled(true);
					mTextHeureDeb.setEnabled(true);
					mTextJourFin.setEnabled(true);
					mTextHeureFin.setEnabled(true);
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		state = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Calendar cal = new GregorianCalendar(mTextJourDeb.getDate().get(
				Calendar.YEAR),
				mTextJourDeb.getDate().get(Calendar.MONTH),
				mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureDeb.getDate().get(Calendar.MINUTE));
		outState.putString(KEY_BUNDLE_DATE_DEB, DateUtil.getTimeInIso(cal));

		cal = new GregorianCalendar(mTextJourFin.getDate().get(
				Calendar.YEAR),
				mTextJourFin.getDate().get(Calendar.MONTH),
				mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureFin.getDate().get(Calendar.MINUTE));
		outState.putString(KEY_BUNDLE_DATE_FIN, DateUtil.getTimeInIso(cal));

		outState.putString(KEY_BUNDLE_TITLE, mTextTitle.getText()
				.toString());
		outState.putString(KEY_BUNDLE_DETAILS, mTextDetails.getText()
				.toString());
		outState.putBoolean(KEY_BUNDLE_ALL_DAY, mSwitchAllDay.isChecked());
		outState.putInt(KEY_BUNDLE_COMPTE,mSpinnerCompte.getSelectedItemPosition());
		outState.putInt(KEY_BUNDLE_RECURRENCE, mSpinnerRecurrence.getSelectedItemPosition());
		outState.putInt(KEY_BUNDLE_PARTICIPATION, mSpinnerParticipation.getSelectedItemPosition());
		state = outState;

	}

	public EventBaseBean getEvent() {
		return getParentActivity().getEvent();
	}

	public void setEvent(EventBaseBean event) {
		getParentActivity().setEvent(event);
	}

	@Override
	public String getTitle() {
		return "Evénement";
	}

	@Override
	public void launchEdit() {
		ViewUtil.enableAllView(view, true);
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		ViewUtil.enableAllView(view, false);
		getParentActivity().getEvent().setAllDay(mSwitchAllDay.isChecked());
		getParentActivity().getEvent().setCid(mListCompte.get(mSpinnerCompte.getSelectedItemPosition()).getId());
		getParentActivity().getEvent().setDetails(mTextDetails.getText().toString());

		Calendar cal = new GregorianCalendar(mTextJourDeb.getDate().get(
				Calendar.YEAR),
				mTextJourDeb.getDate().get(Calendar.MONTH),
				mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureDeb.getDate().get(Calendar.MINUTE));
		getParentActivity().getEvent().sethDeb(cal);
		cal = new GregorianCalendar(mTextJourFin.getDate().get(
				Calendar.YEAR),
				mTextJourFin.getDate().get(Calendar.MONTH),
				mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureFin.getDate().get(Calendar.MINUTE));
		getParentActivity().getEvent().sethFin(cal);
		getParentActivity().getEvent().setTitle(mTextTitle.getText().toString());
		if(getParentActivity().getEvent().getId() == -1) {
			getParentActivity().getEvent().setId((int) new EventBaseRepository(getActivity()).insert(getParentActivity().getEvent()));
			participationBean.setEid(getParentActivity().getEvent().getId());
			participationRepo.insert(participationBean);
		} else {
			new EventBaseRepository(getActivity()).update(getParentActivity().getEvent());
			participationRepo.update(participationBean);		
		}
		getParentActivity().setEvent(getParentActivity().getEvent());
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		if(getParentActivity().getEvent().getId() == -1) {
			getSherlockActivity().finish();
		} else {
			ViewUtil.enableAllView(view, false);
			initialiseValuesFromEvent();
		}
		super.launchCancel();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}


	private Participation[] participation;

	public class Participation {
		private int id;
		private String label;
		public Participation(int id, String label) {
			super();
			id = id;
			label = label;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private class CustomArrayAdapter extends ArrayAdapter<Participation> {

		public CustomArrayAdapter(Context context,int simpleSpinnerItem, Participation[] participation) {
			super(context, simpleSpinnerItem, participation);
		}

		@Override public boolean hasStableIds() {return true;}

		@Override
		public long getItemId(int position) {
			return getItem(position).getId();
		}

	}
}
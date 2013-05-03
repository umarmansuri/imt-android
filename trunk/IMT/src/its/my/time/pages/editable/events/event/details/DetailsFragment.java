package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
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
	private static Bundle state;

	public DetailsFragment() {
		//this.typeEvent = Types.Event.BASE;
	}

	public DetailsFragment(int typeEvent) {
		//this.typeEvent = typeEvent;
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

		this.view = inflater.inflate(R.layout.activity_event_details, null);
		this.view.setBackgroundColor(Color.WHITE);

		final View customView = getCustomView();
		if (customView != null) {
			((FrameLayout) this.view.findViewById(R.id.include)).addView(customView);
		}

		this.mTextTitle = (EditText) this.view.findViewById(R.id.activity_event_details_text_title);
		this.mTextJourDeb = (DateButton) this.view.findViewById(R.id.activity_event_details_text_ddeb);
		this.mTextJourFin = (DateButton) this.view.findViewById(R.id.activity_event_details_text_dfin);
		this.mTextHeureDeb = (TimeButton) this.view				.findViewById(R.id.activity_event_details_text_hdeb);
		this.mTextHeureFin = (TimeButton) this.view
				.findViewById(R.id.activity_event_details_text_hfin);
		this.mSpinnerCompte = (Spinner) this.view
				.findViewById(R.id.activity_event_details_spinner_compte);
		this.mSpinnerRecurrence = (Spinner) this.view
				.findViewById(R.id.activity_event_details_spinner_recurrence);
		this.mSpinnerParticipation = (Spinner) this.view
				.findViewById(R.id.activity_event_details_spinner_participation);
		this.mTextDetails = (TextView) this.view
				.findViewById(R.id.activity_event_details_text_details);
		this.mSwitchAllDay = (Switcher) this.view
				.findViewById(R.id.activity_event_details_switcher_allDay);

		if (state == null) {
			initialiseValuesFromEvent();
		} else {
			initialiseValueFromInstance();
		}
		initialiseActions();

		ViewUtil.enableAllView(view, false);

		return this.view;
	}

	private View getCustomView() {
		return null;
	}

	public void initialiseValuesFromEvent() {
		this.mTextTitle.setText(getParentActivity().getEvent().getTitle());
		this.mTextJourDeb.setDate(getParentActivity().getEvent().gethDeb());
		this.mTextHeureDeb.setDate(getParentActivity().getEvent().gethDeb());
		this.mTextJourFin = (DateButton) this.view.findViewById(R.id.activity_event_details_text_dfin);

		if (getParentActivity().getEvent().gethFin() != null) {
			this.mTextHeureFin.setDate(getParentActivity().getEvent().gethFin());
			this.mTextJourFin.setDate(getParentActivity().getEvent().gethFin());
		}
		this.mTextDetails.setText(getParentActivity().getEvent().getDetails());
		if (getParentActivity().getEvent().isAllDay()) {
			this.mSwitchAllDay.changeState(getParentActivity().getEvent().isAllDay(), true);
		}
	}

	public void initialiseValueFromInstance() {
		this.mTextTitle.setText(state.getString(KEY_BUNDLE_TITLE));

		Calendar cal = DateUtil.getDateFromISO(state
				.getString(KEY_BUNDLE_DATE_DEB));
		this.mTextJourDeb.setDate(cal);
		this.mTextHeureDeb.setDate(cal);

		cal = DateUtil.getDateFromISO(state.getString(KEY_BUNDLE_DATE_FIN));
		this.mTextHeureFin.setDate(cal);
		this.mTextJourFin.setDate(cal);

		this.mTextDetails.setText(state.getString(KEY_BUNDLE_DETAILS));
		if (getParentActivity().getEvent().isAllDay()) {
			this.mSwitchAllDay.changeState(state.getBoolean(KEY_BUNDLE_ALL_DAY), false);
		}
		this.mSpinnerCompte.setSelection(state.getInt(KEY_BUNDLE_COMPTE));
		this.mSpinnerRecurrence.setSelection(state.getInt(KEY_BUNDLE_RECURRENCE));
		this.mSpinnerParticipation.setSelection(state.getInt(KEY_BUNDLE_PARTICIPATION));
	}

	public void initialiseActions() {
		this.mListCompte = new CompteRepository(getActivity()).getAllByUid(PreferencesUtil.getCurrentUid());
		this.mListCompteLabels = new ArrayList<String>();
		int comptePosition = 0;
		int i = 0;
		for (final CompteBean compte : this.mListCompte) {
			if (compte.getId() == getParentActivity().getEvent().getCid()) {
				comptePosition = i;
			}
			i++;
			this.mListCompteLabels.add(compte.getTitle());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				this.mListCompteLabels);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.mSpinnerCompte.setAdapter(adapter);
		this.mSpinnerCompte.setSelection(comptePosition);
		this.mSpinnerCompte.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> container,
					View view, int position, long id) {
				getParentActivity().getEvent().setCid(DetailsFragment.this.mListCompte.get(position).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> container) {
				getParentActivity().getEvent().setCid(-1);
			}
		});

		array_recurrence = getResources().getStringArray(
				R.array.array_recurrence);
		final ArrayAdapter<String> adapter_recurrence = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				this.array_recurrence);
		adapter_recurrence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.mSpinnerRecurrence.setAdapter(adapter_recurrence);


		final ArrayAdapter<Participation> adapter_participation = new CustomArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,
				participation) {
			public View getDropDownView(int position, View convertView, ViewGroup parent)
			{
				View v = null;
				if (position == 0) {
					TextView tv = new TextView(getContext());
					tv.setHeight(0);
					tv.setVisibility(View.GONE);
					v = tv;
				}
				else {
					v = super.getDropDownView(position, null, parent);
				}
				parent.setVerticalScrollBarEnabled(false);
				return v;
			}
		};
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


		this.mSwitchAllDay.setOnStateChangedListener(new OnStateChangedListener() {
			@Override
			public void onStateCHangedListener(Switcher switcher,
					boolean isChecked) {
				if (isChecked == true) {
					DetailsFragment.this.mTextJourDeb.setEnabled(false);
					DetailsFragment.this.mTextJourFin.setEnabled(false);
					DetailsFragment.this.mTextHeureDeb.setEnabled(false);
					DetailsFragment.this.mTextHeureFin.setEnabled(false);

					DetailsFragment.this.mTextJourFin.setText(DetailsFragment.this.mTextJourDeb.getText());
					Calendar calDeb = getParentActivity().getEvent().gethDeb();
					calDeb.set(Calendar.HOUR_OF_DAY, 0);
					calDeb.set(Calendar.MINUTE, 0);
					calDeb.set(Calendar.SECOND, 0);
					DetailsFragment.this.mTextHeureDeb.setDate(calDeb);

					Calendar calFin = getParentActivity().getEvent().gethFin();
					calFin.set(Calendar.HOUR_OF_DAY, 23);
					calFin.set(Calendar.MINUTE, 59);
					calFin.set(Calendar.SECOND, 59);
					DetailsFragment.this.mTextHeureFin.setDate(calFin);
				} else {
					DetailsFragment.this.mTextJourDeb.setEnabled(true);
					DetailsFragment.this.mTextHeureDeb.setEnabled(true);
					DetailsFragment.this.mTextJourFin.setEnabled(true);
					DetailsFragment.this.mTextHeureFin.setEnabled(true);
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
		Calendar cal = new GregorianCalendar(this.mTextJourDeb.getDate().get(
				Calendar.YEAR),
				this.mTextJourDeb.getDate().get(Calendar.MONTH),
				this.mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				this.mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				this.mTextHeureDeb.getDate().get(Calendar.MINUTE));
		outState.putString(KEY_BUNDLE_DATE_DEB, DateUtil.getTimeInIso(cal));

		cal = new GregorianCalendar(this.mTextJourFin.getDate().get(
				Calendar.YEAR),
				this.mTextJourFin.getDate().get(Calendar.MONTH),
				this.mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				this.mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				this.mTextHeureFin.getDate().get(Calendar.MINUTE));
		outState.putString(KEY_BUNDLE_DATE_FIN, DateUtil.getTimeInIso(cal));

		outState.putString(KEY_BUNDLE_TITLE, this.mTextTitle.getText()
				.toString());
		outState.putString(KEY_BUNDLE_DETAILS, this.mTextDetails.getText()
				.toString());
		outState.putBoolean(KEY_BUNDLE_ALL_DAY, this.mSwitchAllDay.isChecked());
		outState.putInt(KEY_BUNDLE_COMPTE,this.mSpinnerCompte.getSelectedItemPosition());
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
		getParentActivity().getEvent().setAllDay(this.mSwitchAllDay.isChecked());
		getParentActivity().getEvent().setCid(this.mListCompte.get(this.mSpinnerCompte.getSelectedItemPosition()).getId());
		getParentActivity().getEvent().setDetails(this.mTextDetails.getText().toString());

		Calendar cal = new GregorianCalendar(this.mTextJourDeb.getDate().get(
				Calendar.YEAR),
				this.mTextJourDeb.getDate().get(Calendar.MONTH),
				this.mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				this.mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				this.mTextHeureDeb.getDate().get(Calendar.MINUTE));
		getParentActivity().getEvent().sethDeb(cal);
		cal = new GregorianCalendar(this.mTextJourFin.getDate().get(
				Calendar.YEAR),
				this.mTextJourFin.getDate().get(Calendar.MONTH),
				this.mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				this.mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				this.mTextHeureFin.getDate().get(Calendar.MINUTE));
		getParentActivity().getEvent().sethFin(cal);
		getParentActivity().getEvent().setTitle(this.mTextTitle.getText().toString());
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
			this.id = id;
			this.label = label;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
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
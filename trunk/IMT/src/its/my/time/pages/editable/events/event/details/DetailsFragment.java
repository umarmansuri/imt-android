package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.DateUtil;
import its.my.time.util.EventTypes;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.Switcher;
import its.my.time.view.Switcher.OnStateChangedListener;
import its.my.time.view.date.DateButton;
import its.my.time.view.date.TimeButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

	private EventBaseBean event;

	private TimeButton mTextHeureDeb;
	private TimeButton mTextHeureFin;
	private DateButton mTextJourDeb;
	private DateButton mTextJourFin;
	private Switcher mSwitchAllDay;
	private ArrayList<String> mListCompteLabels;
	private Spinner mSpinnerCompte;
	private Spinner mSpinnerRecurrence;
	private TextView mTextDetails;
	private String[] array_recurrence;

	private List<CompteBean> mListCompte;

	private View view;

	private EditText mTextTitle;

	private int typeEvent;
	private static Bundle state;

	public DetailsFragment(EventBaseBean event) {
		this.event = event;
		this.typeEvent = EventTypes.TYPE_BASE;
	}

	public DetailsFragment(EventBaseBean event, int typeEvent) {
		this.event = event;
		this.typeEvent = typeEvent;
	}

	public DetailsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.activity_event_details, null);
		this.view.setBackgroundColor(Color.WHITE);

		final View customView = getCustomView();
		if (customView != null) {
			((FrameLayout) this.view.findViewById(R.id.include))
					.addView(customView);
		}

		this.mTextTitle = (EditText) this.view
				.findViewById(R.id.activity_event_details_text_title);
		this.mTextJourDeb = (DateButton) this.view
				.findViewById(R.id.activity_event_details_text_ddeb);
		this.mTextJourFin = (DateButton) this.view
				.findViewById(R.id.activity_event_details_text_dfin);
		this.mTextHeureDeb = (TimeButton) this.view
				.findViewById(R.id.activity_event_details_text_hdeb);
		this.mTextHeureFin = (TimeButton) this.view
				.findViewById(R.id.activity_event_details_text_hfin);
		this.mSpinnerCompte = (Spinner) this.view
				.findViewById(R.id.activity_event_details_spinner_compte);
		this.mSpinnerRecurrence = (Spinner) this.view
				.findViewById(R.id.activity_event_details_spinner_recurrence);
		this.mTextDetails = (TextView) this.view
				.findViewById(R.id.activity_event_details_text_details);
		this.mSwitchAllDay = (Switcher) this.view
				.findViewById(R.id.activity_event_details_switcher_allDay);

		if (state == null) {
			Log.d("DetailsFragment", "Launch from bean");
			initialiseValuesFromEvent();
		} else {
			Log.d("DetailsFragment", "Launch from state");
			initialiseValueFromInstance();
		}
		initialiseActions();
		return this.view;
	}

	private View getCustomView() {
		return null;
	}

	public void initialiseValuesFromEvent() {
		this.mTextTitle.setText(this.event.getTitle());
		this.mTextJourDeb.setDate(this.event.gethDeb());
		this.mTextHeureDeb.setDate(this.event.gethDeb());
		this.mTextJourFin = (DateButton) this.view
				.findViewById(R.id.activity_event_details_text_dfin);

		if (this.event.gethFin() != null) {
			this.mTextHeureFin.setDate(this.event.gethFin());
			this.mTextJourFin.setDate(this.event.gethFin());
		}
		this.mTextDetails.setText(this.event.getDetails());
		if (this.event.isAllDay()) {
			this.mSwitchAllDay.changeState(this.event.isAllDay(), true);
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
		if (this.event.isAllDay()) {
			this.mSwitchAllDay.changeState(
					state.getBoolean(KEY_BUNDLE_ALL_DAY), false);
		}
		this.mSpinnerCompte.setSelection(state.getInt(KEY_BUNDLE_COMPTE));
		this.mSpinnerRecurrence.setSelection(state
				.getInt(KEY_BUNDLE_RECURRENCE));
	}

	public void initialiseActions() {
		this.mListCompte = new CompteRepository(getActivity())
				.getAllCompteByUid(PreferencesUtil.getCurrentUid(getActivity()));
		this.mListCompteLabels = new ArrayList<String>();
		int comptePosition = 0;
		int i = 0;
		for (final CompteBean compte : this.mListCompte) {
			if (compte.getId() != this.event.getCid()) {
				comptePosition = i;
			}
			i++;
			this.mListCompteLabels.add(compte.getTitle());
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				this.mListCompteLabels);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.mSpinnerCompte.setAdapter(adapter);
		this.mSpinnerCompte.setSelection(comptePosition);
		this.mSpinnerCompte
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> container,
							View view, int position, long id) {
						DetailsFragment.this.event
								.setCid(DetailsFragment.this.mListCompte.get(
										position).getId());
					}

					@Override
					public void onNothingSelected(AdapterView<?> container) {
						DetailsFragment.this.event.setCid(-1);
					}
				});

		this.array_recurrence = getResources().getStringArray(
				R.array.array_recurrence);

		final ArrayAdapter<String> adapter_recurrence = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				this.array_recurrence);
		adapter_recurrence
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.mSpinnerRecurrence.setAdapter(adapter_recurrence);

		this.mSwitchAllDay
				.setOnStateChangedListener(new OnStateChangedListener() {
					@Override
					public void onStateCHangedListener(Switcher switcher,
							boolean isChecked) {
						if (isChecked == true) {
							DetailsFragment.this.mTextJourDeb.setEnabled(false);
							DetailsFragment.this.mTextJourFin.setEnabled(false);
							DetailsFragment.this.mTextHeureDeb
									.setEnabled(false);
							DetailsFragment.this.mTextHeureFin
									.setEnabled(false);

							DetailsFragment.this.mTextJourFin
									.setText(DetailsFragment.this.mTextJourDeb
											.getText());
							DetailsFragment.this.mTextHeureDeb.setText("00:00");
							DetailsFragment.this.mTextHeureFin.setText("23:59");
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
		outState.putInt(KEY_BUNDLE_COMPTE,
				this.mSpinnerCompte.getSelectedItemPosition());
		outState.putInt(KEY_BUNDLE_RECURRENCE,
				this.mSpinnerRecurrence.getSelectedItemPosition());

		state = outState;

	}

	public EventBaseBean getEvent() {
		return this.event;
	}

	public void setEvent(EventBaseBean event) {
		this.event = event;
	}

	@Override
	public String getTitle() {
		return "Evénement";
	}

	@Override
	public void launchEdit() {
	}

	@Override
	public void launchSave() {
		this.event.setAllDay(this.mSwitchAllDay.isChecked());
		this.event.setCid(this.mListCompte.get(
				this.mSpinnerCompte.getSelectedItemPosition()).getId());
		this.event.setDetails(this.mTextDetails.getText().toString());

		Calendar cal = new GregorianCalendar(this.mTextJourDeb.getDate().get(
				Calendar.YEAR),
				this.mTextJourDeb.getDate().get(Calendar.MONTH),
				this.mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				this.mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				this.mTextHeureDeb.getDate().get(Calendar.MINUTE));
		this.event.sethDeb(cal);
		cal = new GregorianCalendar(this.mTextJourFin.getDate().get(
				Calendar.YEAR),
				this.mTextJourFin.getDate().get(Calendar.MONTH),
				this.mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				this.mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				this.mTextHeureFin.getDate().get(Calendar.MINUTE));
		this.event.sethFin(cal);
		this.event.setTitle(this.mTextTitle.getText().toString());
		this.event.setTypeId(this.typeEvent);
		this.event.setId((int) new EventBaseRepository(getActivity())
				.insertEvent(this.event));
	}

	@Override
	public void launchCancel() {
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
}
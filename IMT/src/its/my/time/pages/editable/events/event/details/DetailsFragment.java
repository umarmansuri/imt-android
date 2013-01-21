package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.DateUtil;
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

	public DetailsFragment(EventBaseBean event, int typeEvent) {
		this.event = event;
		this.typeEvent = typeEvent;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_event_details, null);
		view.setBackgroundColor(Color.WHITE);

		View customView = getCustomView();
		if(customView != null) {
			((FrameLayout)view.findViewById(R.id.include)).addView(customView);
		}

		mTextTitle = (EditText)view.findViewById(R.id.activity_event_details_text_title);
		mTextJourDeb = (DateButton) view.findViewById(R.id.activity_event_details_text_ddeb);	
		mTextJourFin = (DateButton) view.findViewById(R.id.activity_event_details_text_dfin);	
		mTextHeureDeb = (TimeButton) view.findViewById(R.id.activity_event_details_text_hdeb);
		mTextHeureFin = (TimeButton) view.findViewById(R.id.activity_event_details_text_hfin);
		mSpinnerCompte = (Spinner) view.findViewById(R.id.activity_event_details_spinner_compte);
		mSpinnerRecurrence = (Spinner) view.findViewById(R.id.activity_event_details_spinner_recurrence);
		mTextDetails = (TextView) view.findViewById(R.id.activity_event_details_text_details);
		mSwitchAllDay = (Switcher) view.findViewById(R.id.activity_event_details_switcher_allDay);

		if(state == null) {
			Log.d("DetailsFragment","Launch from bean");
			initialiseValuesFromEvent();
		} else {
			Log.d("DetailsFragment","Launch from state");
			initialiseValueFromInstance();
		}
		initialiseActions();
		return view ;
	}

	private View getCustomView() {
		return null;
	}

	public void initialiseValuesFromEvent() {
		mTextTitle.setText(event.getTitle());
		mTextJourDeb.setDate(event.gethDeb());
		mTextHeureDeb.setDate(event.gethDeb());
		mTextJourFin = (DateButton) view.findViewById(R.id.activity_event_details_text_dfin);

		if (event.gethFin() != null) {
			mTextHeureFin.setDate(event.gethFin());
			mTextJourFin.setDate(event.gethFin());
		}
		mTextDetails.setText(event.getDetails());
		if(event.isAllDay()) {
			mSwitchAllDay.changeState(event.isAllDay(), true);
		}
	}

	public void initialiseValueFromInstance() {
		mTextTitle.setText(state.getString(KEY_BUNDLE_TITLE));

		Calendar cal = DateUtil.getDateFromISO(state.getString(KEY_BUNDLE_DATE_DEB));
		mTextJourDeb.setDate(cal);
		mTextHeureDeb.setDate(cal);

		cal = DateUtil.getDateFromISO(state.getString(KEY_BUNDLE_DATE_FIN));
		mTextHeureFin.setDate(cal);
		mTextJourFin.setDate(cal);
		
		mTextDetails.setText(state.getString(KEY_BUNDLE_DETAILS));
		if(event.isAllDay()) {
			mSwitchAllDay.changeState(state.getBoolean(KEY_BUNDLE_ALL_DAY), false);
		}
		mSpinnerCompte.setSelection(state.getInt(KEY_BUNDLE_COMPTE));
		mSpinnerRecurrence.setSelection(state.getInt(KEY_BUNDLE_RECURRENCE));
	}

	public void initialiseActions() {
		mListCompte = new CompteRepository(getActivity())
				.getAllCompteByUid(PreferencesUtil.getCurrentUid(getActivity()));
		mListCompteLabels = new ArrayList<String>();
		int comptePosition = 0;
		int i = 0;
		for (CompteBean compte : mListCompte) {
			if(compte.getId() != event.getCid()) {
				comptePosition=i;
			}
			i++;
			mListCompteLabels.add(compte.getTitle());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mListCompteLabels);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerCompte.setAdapter(adapter);
		mSpinnerCompte.setSelection(comptePosition);
		mSpinnerCompte.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> container, View view,
					int position, long id) {
				event.setCid(mListCompte.get(position).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> container) {
				event.setCid(-1);
			}
		});



		array_recurrence = getResources().getStringArray(R.array.array_recurrence);
		
		ArrayAdapter<String> adapter_recurrence = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,array_recurrence);
		adapter_recurrence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerRecurrence.setAdapter(adapter_recurrence);

		mSwitchAllDay.setOnStateChangedListener(new OnStateChangedListener() {
			public void onStateCHangedListener(Switcher switcher,
					boolean isChecked) {
				if (isChecked == true) {
					mTextJourDeb.setEnabled(false);
					mTextJourFin.setEnabled(false);
					mTextHeureDeb.setEnabled(false);
					mTextHeureFin.setEnabled(false);

					mTextJourFin.setText(mTextJourDeb.getText());
					mTextHeureDeb.setText("00:00");
					mTextHeureFin.setText("23:59");
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
		Calendar cal = new GregorianCalendar(
				mTextJourDeb.getDate().get(Calendar.YEAR),
				mTextJourDeb.getDate().get(Calendar.MONTH),
				mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureDeb.getDate().get(Calendar.MINUTE));
		outState.putString(KEY_BUNDLE_DATE_DEB, DateUtil.getTimeInIso(cal));

		cal = new GregorianCalendar(
				mTextJourFin.getDate().get(Calendar.YEAR),
				mTextJourFin.getDate().get(Calendar.MONTH),
				mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureFin.getDate().get(Calendar.MINUTE));
		outState.putString(KEY_BUNDLE_DATE_FIN, DateUtil.getTimeInIso(cal));


		outState.putString(KEY_BUNDLE_TITLE, mTextTitle.getText().toString());
		outState.putString(KEY_BUNDLE_DETAILS, mTextDetails.getText().toString());
		outState.putBoolean(KEY_BUNDLE_ALL_DAY, mSwitchAllDay.isChecked());
		outState.putInt(KEY_BUNDLE_COMPTE, mSpinnerCompte.getSelectedItemPosition());
		outState.putInt(KEY_BUNDLE_RECURRENCE, mSpinnerRecurrence.getSelectedItemPosition());
		
		state = outState;

	}

	public EventBaseBean getEvent() {
		return event;
	}

	public void setEvent(EventBaseBean event) {
		this.event = event;
	}

	@Override
	public void launchEdit() {
	}

	@Override
	public void launchSave() {
		event.setAllDay(mSwitchAllDay.isChecked());
		event.setCid(mListCompte.get(mSpinnerCompte.getSelectedItemPosition()).getId());
		event.setDetails(mTextDetails.getText().toString());

		Calendar cal = new GregorianCalendar(
				mTextJourDeb.getDate().get(Calendar.YEAR),
				mTextJourDeb.getDate().get(Calendar.MONTH),
				mTextJourDeb.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureDeb.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureDeb.getDate().get(Calendar.MINUTE));
		event.sethDeb(cal);
		cal = new GregorianCalendar(
				mTextJourFin.getDate().get(Calendar.YEAR),
				mTextJourFin.getDate().get(Calendar.MONTH),
				mTextJourFin.getDate().get(Calendar.DAY_OF_MONTH),
				mTextHeureFin.getDate().get(Calendar.HOUR_OF_DAY),
				mTextHeureFin.getDate().get(Calendar.MINUTE));
		event.sethFin(cal);
		event.setTitle(mTextTitle.getText().toString());
		event.setTypeId(typeEvent);
		event.setId((int) new EventBaseRepository(getActivity()).insertEvent(event));
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
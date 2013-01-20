package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.Switcher;
import its.my.time.view.Switcher.OnStateChangedListener;
import its.my.time.view.date.DateButton;
import its.my.time.view.date.TimeButton;

import java.util.ArrayList;
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
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsFragment extends BasePluginFragment {

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

	private Context context;

	private View view;

	public DetailsFragment(EventBaseBean event) {
		this.event = event;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_event_details, null);
		view.setBackgroundColor(Color.WHITE);
		initialiseValues();
		
		return view ;
	}

	private void initialiseValues() {
		mTextJourDeb = (DateButton) view.findViewById(R.id.activity_event_details_text_ddeb);
		mTextJourDeb.setDate(event.gethDeb());
		
		mTextHeureDeb = (TimeButton) view.findViewById(R.id.activity_event_details_text_hdeb);
		mTextHeureDeb.setDate(event.gethDeb());

		mTextHeureFin = (TimeButton) view.findViewById(R.id.activity_event_details_text_hfin);
		mTextJourFin = (DateButton) view.findViewById(R.id.activity_event_details_text_dfin);


		if (event.gethFin() != null) {
			mTextHeureFin.setDate(event.gethFin());
			mTextJourFin.setDate(event.gethFin());
		}

		mListCompte = DatabaseUtil.getCompteRepository(getActivity())
				.getAllCompteByUid(PreferencesUtil.getCurrentUid(getActivity()));
		mListCompteLabels = new ArrayList<String>();
		for (CompteBean compte : mListCompte) {
			mListCompteLabels.add(compte.getTitle());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, mListCompteLabels);
		mSpinnerCompte = (Spinner) view.findViewById(R.id.activity_event_details_spinner_compte);
		mSpinnerCompte.setAdapter(adapter);
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
		Spinner mSpinnerRecurrence = (Spinner) view.findViewById(R.id.activity_event_details_spinner_recurrence);
		mSpinnerRecurrence.setAdapter(adapter_recurrence);

		mTextDetails = (TextView) view.findViewById(R.id.activity_event_details_text_details);
		mTextDetails.setText(event.getDetails());

		mSwitchAllDay = (Switcher) view.findViewById(R.id.activity_event_details_switcher_allDay);
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
		if(event.isAllDay()) {
			mSwitchAllDay.changeState(event.isAllDay(), true);
		}


	};
	@Override
	public void launchEdit() {
	}

	@Override
	public void launchSave() {
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
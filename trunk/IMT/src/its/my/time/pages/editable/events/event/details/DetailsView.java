package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsView extends FrameLayout {

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

	private List<CompteBean> mListCompte;

	public DetailsView(Context context, EventBaseBean event) {
		super(context);
		addView(inflate(context, R.layout.activity_event_details, null));
		setBackgroundColor(Color.WHITE);

		this.event = event;

		initialiseValues();
	}

	private void initialiseValues() {
		mTextJourDeb = (DateButton) findViewById(R.id.activity_event_details_text_ddeb);
		mTextJourDeb.setDate(event.gethDeb());
		mTextHeureDeb = (TimeButton) findViewById(R.id.activity_event_details_text_hdeb);
		mTextHeureDeb.setDate(event.gethDeb());

		mTextHeureFin = (TimeButton) findViewById(R.id.activity_event_details_text_hfin);
		mTextJourFin = (DateButton) findViewById(R.id.activity_event_details_text_dfin);


		if (event.gethFin() != null) {
			mTextHeureFin.setDate(event.gethFin());
			mTextJourFin.setDate(event.gethFin());
		}

		mListCompte = DatabaseUtil.getCompteRepository(getContext())
				.getAllCompteByUid(PreferencesUtil.getCurrentUid(getContext()));
		mListCompteLabels = new ArrayList<String>();
		for (CompteBean compte : mListCompte) {
			mListCompteLabels.add(compte.getTitle());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, mListCompteLabels);
		mSpinnerCompte = (Spinner) findViewById(R.id.activity_event_details_spinner_compte);
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

		String[] array_recurrence;
		array_recurrence = getResources().getStringArray(R.array.array_recurrence);

		ArrayAdapter<String> adapter_recurrence = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,array_recurrence);
		Spinner mSpinnerRecurrence = (Spinner) findViewById(R.id.activity_event_details_spinner_recurrence);
		mSpinnerRecurrence.setAdapter(adapter_recurrence);

		mTextDetails = (TextView) findViewById(R.id.activity_event_details_text_details);
		mTextDetails.setText(event.getDetails());

		mSwitchAllDay = (Switcher) findViewById(R.id.activity_event_details_switcher_allDay);
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
}

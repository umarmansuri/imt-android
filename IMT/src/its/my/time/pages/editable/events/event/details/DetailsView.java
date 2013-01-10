package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsView extends FrameLayout implements OnClickListener {

	private EventBaseBean event;

	private TextView mTextHeureDeb;
	private TextView mTextHeureFin;
	private TextView mTextJourDeb;
	private TextView mTextJourFin;
	private ArrayList<String> mListCompteLabels;
	private Spinner mSpinnerCompte;
	private TextView mTextDetails;

	private List<CompteBean> mListCompte;

	private DatePickerDialog datePicker;
	private TimePickerDialog timePicker;

	private OnDateTimeChangedListener onDateTimeChangedListener;
	
	public DetailsView(Context context, EventBaseBean event) {
		super(context);
		addView(inflate(context, R.layout.activity_event_details, null));
		setBackgroundColor(Color.WHITE);

		this.event = event;

		initialiseValues();
	}

	private void initialiseValues() {
		mTextJourDeb = (TextView) findViewById(R.id.activity_event_details_text_ddeb);
		mTextJourDeb.setText(DateUtil.getDay(event.gethDeb()));
		mTextHeureDeb = (TextView) findViewById(R.id.activity_event_details_text_hdeb);
		mTextHeureDeb.setText(DateUtil.getTime(event.gethDeb()));

		mTextHeureFin = (TextView) findViewById(R.id.activity_event_details_text_hfin);
		mTextJourFin = (TextView) findViewById(R.id.activity_event_details_text_dfin);

		if (event.gethFin() != null) {
			mTextHeureFin.setText(DateUtil.getTime(event.gethFin()));
			mTextJourFin.setText(DateUtil.getDay(event.gethFin()));
		}

		mTextJourDeb.setOnClickListener(this);
		mTextHeureDeb.setOnClickListener(this);

		mTextJourFin.setOnClickListener(this);
		mTextHeureFin.setOnClickListener(this);

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

		mTextDetails = (TextView) findViewById(R.id.activity_event_details_text_details);
		mTextDetails.setText(event.getDetails());
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_event_details_text_ddeb:
			onDateTimeChangedListener.setCurrentView(mTextJourDeb);
			datePicker = new DatePickerDialog(getContext(), onDateTimeChangedListener,
					2012, 01, 12);
			datePicker.show();
			break;
		case R.id.activity_event_details_text_dfin:
			onDateTimeChangedListener.setCurrentView(mTextJourFin);
			datePicker = new DatePickerDialog(getContext(), onDateTimeChangedListener,
					2012, 01, 12);
			datePicker.show();
			break;
		case R.id.activity_event_details_text_hdeb:			
			onDateTimeChangedListener.setCurrentView(mTextHeureDeb);
			timePicker = new TimePickerDialog(getContext(), onDateTimeChangedListener,
					12, 0, true);
			timePicker.show();
			break;
		case R.id.activity_event_details_text_hfin:
			onDateTimeChangedListener.setCurrentView(mTextHeureFin);
			timePicker = new TimePickerDialog(getContext(), onDateTimeChangedListener,
					13, 0, true);
			timePicker.show();
			break;
		}
	}
}

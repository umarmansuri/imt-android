package its.my.time.pages.editable.events.event.details;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsView extends FrameLayout{

	private EventBaseBean event;

	private TextView mTextDateDeb;
	private TextView mTextDateFin;
	private ArrayList<String> mListCompteLabels;
	private Spinner mSpinnerCompte;
	private TextView mTextDetails;

	private List<CompteBean> mListCompte;
	
	private static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;

	public DetailsView(Context context, EventBaseBean event) {
		super(context);
		addView(inflate(context, R.layout.activity_event_details, null));
		setBackgroundColor(Color.WHITE);

		this.event = event;

		initialiseValues();
	}

	private void initialiseValues() {
		mTextDateDeb = (TextView)findViewById(R.id.activity_event_details_text_hdeb);
		mTextDateDeb.setText(DateUtil.getLongDateTime(event.gethDeb()));

		mTextDateFin = (TextView)findViewById(R.id.activity_event_details_text_hfin);

		if(event.gethFin() != null) {
			mTextDateFin.setText(DateUtil.getDayHour(event.gethFin()));
		}
		
		
		mTextDateDeb.setOnTouchListener(new TextView.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if (MotionEvent.ACTION_DOWN == event.getAction()) {

			        } else if (MotionEvent.ACTION_UP == event.getAction()) {
			        	//showDialog(DATE_DIALOG_ID);
			        }

			        return true;
			}
	    });
		
		
		
		// get the current date
	    final Calendar c = Calendar.getInstance();
	    mYear = c.get(Calendar.YEAR);
	    mMonth = c.get(Calendar.MONTH);
	    mDay = c.get(Calendar.DAY_OF_MONTH);

	    // display the current date
	    updateDisplay();


		mListCompte = DatabaseUtil.getCompteRepository(getContext()).getAllCompteByUid(PreferencesUtil.getCurrentUid(getContext()));
		mListCompteLabels = new ArrayList<String>();
		for (CompteBean compte : mListCompte) {
			mListCompteLabels.add(compte.getTitle());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mListCompteLabels);
		mSpinnerCompte = (Spinner)findViewById(R.id.activity_event_details_spinner_compte);
		mSpinnerCompte.setAdapter(adapter);
		mSpinnerCompte.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> container, View view, int position, long id) {
				event.setCid(mListCompte.get(position).getId());				
			}

			@Override
			public void onNothingSelected(AdapterView<?> container) {
				event.setCid(-1);
			}
		});


		mTextDetails = (TextView)findViewById(R.id.activity_event_details_text_details);
		mTextDetails.setText(event.getDetails());
	}
	
	protected Dialog onCreateDialog(int id) {
	       switch (id) {
	       case DATE_DIALOG_ID:
	    	   Log.d("TAG","MotionEvent UP");
	          return new DatePickerDialog(getContext(),
	                    mDateSetListener,
	                    mYear, mMonth, mDay);
	       }
	       return null;
	    }

	private void updateDisplay() {
	    this.mTextDateDeb.setText(
	        new StringBuilder()
	                // Month is 0 based so add 1
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" "));
	    Log.d("TAG","MotionEvent UP");
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
		    new DatePickerDialog.OnDateSetListener() {
		        public void onDateSet(DatePicker view, int year, 
		                              int monthOfYear, int dayOfMonth) {
		            mYear = year;
		            mMonth = monthOfYear;
		            mDay = dayOfMonth;
		            Log.d("TAG","MotionEvent UP");
		            updateDisplay();
		        }
		    };
	
		    

	
}

package its.my.time.receivers;

import its.my.time.Consts;
import its.my.time.R;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.Types;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ValidateParticipationActivity extends Activity implements OnClickListener{

	private TextView mTitle;
	private Button mOui;
	private Button mDetail;
	private TextView mDate;
	private Button mNon;
	private Button mPe;
	private EventBaseBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		bean = new EventBaseRepository(this).getById(getIntent().getIntExtra(Consts.EXTRA_EID, -1));
		
		
		if(bean == null) {
			finish();
		} else {
			setContentView(R.layout.activity_validate);

			mTitle = (TextView)findViewById(R.id.title);
			mDate = (TextView)findViewById(R.id.date);
			mDetail = (Button)findViewById(R.id.btnDetails);
			mOui = (Button)findViewById(R.id.btnOui);
			mNon = (Button)findViewById(R.id.btnNon);
			mPe = (Button)findViewById(R.id.btnPeutEtre);

			mTitle.setText(bean.getTitle());
			mDate.setText(DateUtil.getLongDateTime(bean.gethDeb()));
			mDetail.setOnClickListener(this);
			mOui.setOnClickListener(this);
			mNon.setOnClickListener(this);
			mPe.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		if(v== mDetail) {
			ActivityUtil.startEventActivity(this, bean.getId(), bean.getTypeId());
		} else if(v== mOui) {
			//TODO call to WS pour validation
		} else if(v== mNon) {
			//TODO call to WS pour validation
		} else if(v== mPe) {
			//TODO call to WS pour validation
		}
		finish();
	}
}

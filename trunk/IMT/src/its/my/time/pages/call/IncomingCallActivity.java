package its.my.time.pages.call;

import its.my.time.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class IncomingCallActivity extends Activity implements OnClickListener{

	private Button mBtnDec;
	private Button mBtnRac;
	private TextView mTxtCaller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_incoming_call);
		mBtnDec = (Button)findViewById(R.id.btnDecrocher);
		mBtnRac = (Button)findViewById(R.id.btnRaccrocher);
		mBtnDec.setOnClickListener(this);
		mBtnRac.setOnClickListener(this);
		mTxtCaller = (TextView)findViewById(R.id.textCaller);
		
		//TODO remettre pour call String caller = CallManager.getCallerLabel();
		//TODO remettre pour call mTxtCaller.setText(caller);
	}
	
	@Override
	public void onClick(View v) {
		if(v == mBtnDec) {
			//TODO remettre pour call CallManager.answer();
			
		} else {
			//TODO remettre pour call CallManager.hangup();
		}
		finish();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}
}

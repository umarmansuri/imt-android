package its.my.time.pages.reporting;

import its.my.time.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CheckBox;

public class ReportingActivity extends Activity{

	protected static final String FILTER_ACCOUNT = "FILTER_ACCOUNT";
	protected static final String FILTER_DATE = "FILTER_DATE";
	protected static final String FILTER_TYPE = "FILTER_TYPE";

	private static final String URL = "http://app.my-time.fr/IMT/reporting/mobile";
	public static void showChoiceDialog(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		final View dialogView = View.inflate(context, R.layout.activity_reporting_choice, null);
		builder.setView(dialogView);
		final AlertDialog dialog = builder.create();
		dialogView.findViewById(R.id.valider).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox mCheckAccount = (CheckBox)dialogView.findViewById(R.id.checkAccount);
				CheckBox mCheckDate = (CheckBox)dialogView.findViewById(R.id.checkDate);
				CheckBox mCheckType = (CheckBox)dialogView.findViewById(R.id.checkType);
			
				dialog.dismiss();
				Intent intent = new Intent(context, ReportingActivity.class);
				intent.putExtra(FILTER_ACCOUNT, mCheckAccount.isChecked());
				intent.putExtra(FILTER_DATE, mCheckDate.isChecked());
				intent.putExtra(FILTER_TYPE, mCheckType.isChecked());
				context.startActivity(intent);
			}
		});		
		dialog.show();
	}

	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Bundle extra = getIntent().getExtras();
		boolean isByAccount = extra.getBoolean(FILTER_ACCOUNT);
		boolean isByDate = extra.getBoolean(FILTER_DATE);
		boolean isByType= extra.getBoolean(FILTER_TYPE);
		
		mWebView = new WebView(this);
		mWebView.getSettings().setEnableSmoothTransition(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("https://google-developers.appspot.com/chart/interactive/docs/gallery/piechart#Example");
		setContentView(mWebView);
	}
	
}

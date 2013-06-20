package its.my.time.pages.reporting;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.ws.WSBase;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.date.DateButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.app.SherlockFragmentActivity;

@SuppressLint("SetJavaScriptEnabled")
public class ReportingActivity extends SherlockFragmentActivity {


	private WebView mWebView;
	private LinearLayout layoutCompte;

	private DateButton mTextJourDeb;
	private DateButton mTextJourFin;

	private RadioGroup layoutType;
	private List<CompteBean> comptes;
	private String[] labels;
	private String selectedType;
	private List<Integer> selecteCid = new ArrayList<Integer>();

	private Button mBtnValidate;

	private ProgressDialog dialog;

	private ViewSwitcher mSwitcher;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSwitcher = (ViewSwitcher)View.inflate(this, R.layout.activity_reporting, null);
		setContentView(mSwitcher);

		mTextJourDeb = (DateButton)findViewById(R.id.ddeb);
		mTextJourFin = (DateButton)findViewById(R.id.dfin);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setEnableSmoothTransition(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSavePassword(false);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.setInitialScale(100);
		mWebView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);

		mBtnValidate = (Button)findViewById(R.id.buttonValidate);
		mBtnValidate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(selecteCid.size() > 0 && selectedType != null) {
					Calendar calDeb = mTextJourDeb.getDate();
					Calendar calFin = mTextJourFin.getDate();
					Calendar calFinTest = new GregorianCalendar(
							calFin.get(Calendar.YEAR),
							calFin.get(Calendar.MONTH),
							calFin.get(Calendar.DAY_OF_MONTH),0,0,0);
					calFinTest.add(Calendar.MONTH, -1);
					calFinTest.add(Calendar.MINUTE, -1);
					if(calDeb.after(calFin)) {
						Toast.makeText(ReportingActivity.this, "La date de début de la période doit être antérieur à sa date de fin!", Toast.LENGTH_SHORT).show();
					} else if(calDeb.before(calFinTest)) {
						Toast.makeText(ReportingActivity.this, "La période est trop importante. (1 mois maximum)", Toast.LENGTH_SHORT).show();
					} else {
						loadReporting();	
					}
				} else {
					Toast.makeText(ReportingActivity.this, "Tous les champs ne sont pas remplis!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		layoutCompte = (LinearLayout)findViewById(R.id.reportingLayoutCompte);
		comptes = new CompteRepository(this).getAllByUid(PreferencesUtil.getCurrentUid());
		for (int i = 0; i < comptes.size(); i++) {
			final int j = i; 
			CheckBox checkBox = new CheckBox(this);
			checkBox.setText(comptes.get(i).getTitle());
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked) {
						selecteCid.add(comptes.get(j).getId());
					} else {
						selecteCid.remove(comptes.get(j).getId());
					}
				}
			});
			layoutCompte.addView(checkBox);
		}


		mTextJourDeb = (DateButton) findViewById(R.id.ddeb);
		mTextJourFin = (DateButton) findViewById(R.id.dfin);

		layoutType = (RadioGroup)findViewById(R.id.reportingLayoutType);

		labels = new String[] {
				getResources().getString(R.string.label_event_base),
				getResources().getString(R.string.label_event_meeting),
				//context.getResources().getString(R.string.label_event_task),
				getResources().getString(R.string.label_event_call) 
		};
		for (int i = 0; i < labels.length; i++) {
			final int j = i;
			RadioButton radioButton = new RadioButton(this);
			radioButton.setText(labels[i]);
			radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					selectedType = labels[j];
				}
			});
			layoutType.addView(radioButton);
		}
	}



	@SuppressWarnings("deprecation")
	private void loadReporting() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("Patience");
		dialog.setMessage("Création du rapport en cours...");
		dialog.setCancelable(false);
		dialog.show();




		WebViewClient webViewClient =  new WebViewClient() {
			private int count = 0;

			@Override
			public void onPageFinished(WebView view, String url) {
				if ( url.startsWith(WSBase.URL_LOGIN) ) {
					if(count >= 1) {
						mWebView.stopLoading();
						return;
					}
					count++;
					mWebView.loadUrl("javascript: document.getElementsByTagName('form')[0].elements['input-username'].value = 'ad.hugon';");
					mWebView.loadUrl("javascript: document.getElementsByTagName('form')[0].elements['input-password'].value = 'azerazer';");
					mWebView.loadUrl("javascript: document.getElementsByTagName('form')[0].submit();");
				} else if ( url.startsWith(WSBase.URL_REPORTING) ) {
					mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							dialog.hide();
							mSwitcher.showNext();
							toggleFullscreen(true);
						}
					});
				} else {
					super.onPageFinished(view, url);
				}
			}
		};
		mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		mWebView.setWebViewClient(webViewClient);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		String idsString = Arrays.toString(selecteCid.toArray(new Integer[]{})); 
		mWebView.postUrl(WSBase.URL_REPORTING,EncodingUtils.getBytes(
				"datedebut=" + DateUtil.getDayHourFrenchSlash(mTextJourDeb.getDate()) +
				"&datefin=" + DateUtil.getDayHourFrenchSlash(mTextJourFin.getDate()) +
				"&account=" + selecteCid.toArray().toString() +
				"&imt_event_form_general_account=" + selectedType +
				"&tailleecran=" + width,"BASE64"));

	}


	static class MyJavaScriptInterface
	{
		public void processHTML(String html)
		{
			//Log.d("HTML",html);
		}
	}

	@Override
	public void onBackPressed() {
		if(mSwitcher.getDisplayedChild() ==1) {
			mSwitcher.showPrevious();
			toggleFullscreen(false);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void toggleFullscreen(boolean fullscreen)
	{
		WindowManager.LayoutParams attrs = getWindow().getAttributes();
		if (fullscreen)
		{
			attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getSupportActionBar().hide();
		}
		else
		{
			attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getSupportActionBar().show();
		}
		getWindow().setAttributes(attrs);
	}
}

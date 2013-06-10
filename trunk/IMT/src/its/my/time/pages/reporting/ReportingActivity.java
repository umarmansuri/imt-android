package its.my.time.pages.reporting;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.ws.Callback;
import its.my.time.data.ws.WSLogin;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ReportingActivity extends Activity{

	private static final String URL = "http://app.my-time.fr/IMT/reporting/mobile/";

	private WebView mWebView;
	private LinearLayout layoutCompte;
	private RelativeLayout layoutDate;
	private RadioGroup layoutType;
	private List<CompteBean> comptes;
	private Integer[] types;
	private String[] labels;
	private String selectedType;
	private List<Integer> selecteCid = new ArrayList<Integer>();

	private Button mBtnValidate;

	private ProgressDialog dialog;

	private int width;

	private ViewSwitcher mSwitcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mSwitcher = (ViewSwitcher)View.inflate(this, R.layout.activity_reporting, null);
		setContentView(mSwitcher);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setEnableSmoothTransition(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSavePassword(false);

		mBtnValidate = (Button)findViewById(R.id.buttonValidate);
		mBtnValidate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(selecteCid.size() == 0 || selectedType != null) {
					selecteCid.add(1);
					selectedType = "event";
				}
				if(selecteCid.size() > 0 && selectedType != null) {
					loadReporting();
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

		layoutDate = (RelativeLayout)findViewById(R.id.reportingLayoutDate);
		layoutType = (RadioGroup)findViewById(R.id.reportingLayoutType);

		labels = new String[] {
				getResources().getString(R.string.label_event_base),
				getResources().getString(R.string.label_event_meeting),
				//context.getResources().getString(R.string.label_event_task),
				getResources().getString(R.string.label_event_call) };
		types = new Integer[] { 
				Types.Event.BASE,
				Types.Event.MEETING, 
				//Types.Event.TASK,
				Types.Event.CALL };
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
		width = getWindowManager().getDefaultDisplay().getWidth();
	}


	private void loadReporting() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("Patience");
		dialog.setMessage("Création du rapport en cours...");
		dialog.setCancelable(false);
		dialog.show();

		WSLogin.checkConnexion(this, new Callback() {

			@Override
			public void done(Exception e) {
				if(e == null) {
					mWebView.loadUrl(URL);
				}
			}
		});
	}


	private class LoadReporting extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
			nameValuePairs.add(new BasicNameValuePair("datedebut ", "01/06/2013"));  
			nameValuePairs.add(new BasicNameValuePair("datefin", "01/07/2013"));  
			nameValuePairs.add(new BasicNameValuePair("account ", "[1]"));  
			nameValuePairs.add(new BasicNameValuePair("imt_event_form_general_account ", selectedType));  
			nameValuePairs.add(new BasicNameValuePair("tailleecran", String.valueOf(width)));

			try {
				HttpClient httpclient = new DefaultHttpClient();  
				HttpPost httppost = new HttpPost(URL);  
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  

				HttpResponse response = httpclient.execute(httppost);
				return EntityUtils.toString(response.getEntity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if(result != null) {
				mWebView.loadData(result, "text/html", "utf-8");
				mSwitcher.showNext();
			}
			if(dialog != null) {
				dialog.hide();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if(mSwitcher.getDisplayedChild() ==1) {
			mSwitcher.showPrevious();
		} else {
			super.onBackPressed();
		}
	}
}

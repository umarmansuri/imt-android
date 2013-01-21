package its.my.time;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.util.ActivityUtil;
import its.my.time.util.PreferencesUtil;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private Button btnConnexion;
	private TextView btnInscription;
	private TextView btnMdpLost;
	private EditText pseudo;
	private EditText mdp;
	private OnClickListener clickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		this.clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == SplashActivity.this.btnConnexion) {
					connexion();
				} else if (v == SplashActivity.this.btnInscription) {
					ActivityUtil.startProfilActivity(SplashActivity.this);
				}
			}
		};

		this.btnConnexion = (Button) findViewById(R.id.splash_btnConnexion);
		this.btnConnexion.setOnClickListener(this.clickListener);

		this.btnInscription = (TextView) findViewById(R.id.splash_signIn);
		this.btnInscription.setOnClickListener(this.clickListener);

		this.btnMdpLost = (TextView) findViewById(R.id.splash_mdpLost);
		this.btnMdpLost.setOnClickListener(this.clickListener);

		// temp();
		/*
		 * if(PreferencesUtil.getCurrentUid(SplashActivity.this) < 0)
		 * ActivityUtil.startProfilActivity(SplashActivity.this); else {
		 * startClockAnimation(); new LoadMainActivity().execute(); }
		 */
	}

	private void connexion() {
		if (PreferencesUtil.getCurrentUid(SplashActivity.this) != -1) {
			startClockAnimation();
			new LoadMainActivity().execute();
		} else {
			this.pseudo = (EditText) findViewById(R.id.splash_login);
			this.mdp = (EditText) findViewById(R.id.splash_mdp);

			UtilisateurBean user = new UtilisateurBean();
			final UtilisateurRepository connexion = new UtilisateurRepository(
					SplashActivity.this);
			user = connexion.getConnexion(this.pseudo.getText().toString(),
					this.mdp.getText().toString());
			if (user != null) {
				startClockAnimation();
				new LoadMainActivity().execute();
			} else {
				Toast.makeText(this, "FAIL PELO !", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void startClockAnimation() {
		final Animation anim = new RotateAnimation(-20f, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setDuration(3000);
		anim.setFillAfter(true);
		findViewById(R.id.splash_foreground).setAnimation(anim);
		findViewById(R.id.splash_foreground).getAnimation().start();
	}

	private class LoadMainActivity extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ActivityUtil.startCalendarActivity(SplashActivity.this);
					finish();
				}
			}, 2000);
		}
	}
}

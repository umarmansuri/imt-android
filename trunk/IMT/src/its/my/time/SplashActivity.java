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

		clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == btnConnexion)
					connexion();
				else if (v == btnInscription)
					ActivityUtil.startProfilActivity(SplashActivity.this);
			}
		};

		btnConnexion = (Button) findViewById(R.id.splash_btnConnexion);
		btnConnexion.setOnClickListener(clickListener);
		
		btnInscription = (TextView) findViewById(R.id.splash_signIn);
		btnInscription.setOnClickListener(clickListener);

		btnMdpLost = (TextView) findViewById(R.id.splash_mdpLost);
		btnMdpLost.setOnClickListener(clickListener);
		
		/*
		 * if(PreferencesUtil.getCurrentUid(SplashActivity.this) < 0)
		 * ActivityUtil.startProfilActivity(SplashActivity.this); else {
		 * startClockAnimation(); new LoadMainActivity().execute(); }
		 */
	}

	private void connexion() {
		if (PreferencesUtil.getCurrentUid(SplashActivity.this) < 0) {
			startClockAnimation();
			new LoadMainActivity().execute();
		}else{
			pseudo = (EditText) findViewById(R.id.splash_login);
			mdp = (EditText) findViewById(R.id.splash_mdp);
			 
			UtilisateurBean user = new UtilisateurBean();
			UtilisateurRepository connexion = new UtilisateurRepository(SplashActivity.this);
			user = connexion.getConnexion(pseudo.getText().toString(), mdp.getText().toString());
			if (user != null) {
				startClockAnimation();
				new LoadMainActivity().execute();
			}else{
				Toast.makeText(this, "FAIL PELO !", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void startClockAnimation() {
		Animation anim = new RotateAnimation(-20f, 0f,
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

			// TODO enlever
			/*
			 * deleteDatabase(DatabaseHandler.DATABASE_NAME);
			 * 
			 * UtilisateurRepository userRepo = new
			 * UtilisateurRepository(SplashActivity.this); UtilisateurBean user
			 * = new UtilisateurBean(); user.setAdresse("42 rue du charpenet");
			 * user.setCodePostal(69890); user.setMail("ad.hugon@gmail.com");
			 * user.setNom("Hugon"); user.setPays("France");
			 * user.setPrenom("Adrien"); user.setTel("0617454462");
			 * user.setVille("La Tour de salvagny"); long resUser =
			 * userRepo.insertUtilisateur(user);
			 * PreferencesUtil.setCurrentUid(SplashActivity.this, resUser);
			 * 
			 * CompteRepository repoCompte = new
			 * CompteRepository(SplashActivity.this); CompteBean compte = new
			 * CompteBean(); compte.setColor(Color.RED); compte.setShowed(true);
			 * compte.setTitle("Titre compte 1"); compte.setType(0);
			 * compte.setUid(resUser); long resCompte1 =
			 * repoCompte.insertCompte(compte);
			 * 
			 * compte = new CompteBean(); compte.setColor(Color.BLUE);
			 * compte.setShowed(true); compte.setTitle("Compte 2");
			 * compte.setType(0); compte.setUid(resUser); long resCompte2 =
			 * repoCompte.insertCompte(compte);
			 * 
			 * 
			 * 
			 * EventBaseRepository adapter = new
			 * EventBaseRepository(SplashActivity.this); EventBaseBean bean;
			 * Calendar calDeb2; Calendar calFin2; bean = new EventBaseBean();
			 * bean.setCid(resCompte1); bean.setTitle("Voicin un �v�nement");
			 * calDeb2 = Calendar.getInstance(); bean.sethDeb(calDeb2); calFin2
			 * = Calendar.getInstance(); calFin2.add(Calendar.HOUR, 2);
			 * bean.sethFin(calFin2);
			 * bean.setTypeId(EventBaseRepository.Types.TYPE_MEETING);
			 * bean.setDetailsId(0); long res = adapter.insertEvent(bean);
			 * 
			 * bean.setTitle("Deuxi�me"); bean.setId(1); res =
			 * adapter.insertEvent(bean);
			 * 
			 * bean.setTitle("Et un petit dernier..."); bean.setId(2); res =
			 * adapter.insertEvent(bean);
			 * 
			 * 
			 * 
			 * bean.setCid(resCompte2); bean.setId(3);
			 * bean.setTitle("Voicin un �v�nement"); calDeb2 =
			 * Calendar.getInstance(); calDeb2.add(Calendar.DAY_OF_MONTH, -10);
			 * bean.sethDeb(calDeb2); calFin2 = (Calendar) calDeb2.clone();
			 * calFin2.add(Calendar.HOUR, 2); bean.sethFin(calFin2);
			 * bean.setTypeId(EventBaseRepository.Types.TYPE_CALL);
			 * bean.setDetailsId(0); res = adapter.insertEvent(bean);
			 * 
			 * bean.setTitle("Deuxi�me"); bean.setId(4); res =
			 * adapter.insertEvent(bean); bean.setTitle("Troisieme...");
			 * bean.setId(5); res = adapter.insertEvent(bean);
			 * bean.setTitle("Un autre"); bean.setId(6); res =
			 * adapter.insertEvent(bean);
			 * bean.setTitle("Et le dernier!!!!!!!!!!"); bean.setId(7); res =
			 * adapter.insertEvent(bean);
			 */
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

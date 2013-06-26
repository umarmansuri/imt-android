package its.my.time;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.data.ws.GCMManager;
import its.my.time.data.ws.WSGetBase;
import its.my.time.data.ws.WSLogin;
import its.my.time.data.ws.WSPostBase;
import its.my.time.data.ws.user.UtilisateurBeanWS;
import its.my.time.data.ws.user.WSGetUser;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.ActivityUtil;
import its.my.time.util.CallManager;
import its.my.time.util.PreferencesUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class SplashActivity extends SherlockActivity {

	private Button btnConnexion;
	private TextView btnInscription;
	private TextView btnMdpLost;
	private EditText pseudo;
	private EditText mdp;
	private OnClickListener clickListener;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		PreferencesUtil.init(this);
		if(PreferencesUtil.getCurrentUid() > 0) {
			launchActivity();
		} else {
			setContentView(R.layout.activity_splash);
			this.clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (v == SplashActivity.this.btnConnexion) {
						logFromServer();
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

			this.pseudo = (EditText) findViewById(R.id.splash_login);
			this.mdp = (EditText) findViewById(R.id.splash_mdp);
		}
	}

	private void logFromServer() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("Patience");
		dialog.setMessage("Connexion en cours...");
		dialog.setCancelable(false);
		dialog.show();

		threadConnection = new Thread(new Runnable() {

			@Override
			public void run() {
				WSLogin.checkConnexion(
						pseudo.getText().toString(), 
						mdp.getText().toString(),
						SplashActivity.this, 
						new its.my.time.data.ws.Callback() {

							@Override
							public void done(Exception e) {
								if(e == null) {
									handlerConnection.sendEmptyMessage(0);
								} else {
									handlerConnection.sendEmptyMessage(1);
								}
							}
						});
			}
		});
		threadConnection.start();
		//startActivity(new Intent(SplashActivity.this, TestWSActivity.class));
	}

	private Thread threadConnection;

	private Handler handlerConnection = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message message) {
			if(dialog != null) {
				dialog.hide();
			}
			if(message.what == 0) {
				new WSGetUser(SplashActivity.this, 1, new WSGetBase.GetCallback<UtilisateurBeanWS>() {
					@Override public void done(Exception e) {					}
					@Override public void onGetObject(UtilisateurBeanWS object) {
						PreferencesUtil.setCurrentUid(object.getId());
						launchActivity();
					}
				}).execute();

			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
				builder.setTitle("Erreur");
				builder.setMessage("L'identifiant ou le mot de passe sont incorrects.");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
			}
			return false;
		}
	});


	private void launchActivity() {
		ActivityUtil.startCalendarActivity(SplashActivity.this);
		CallManager.initializeManager(getBaseContext());
		String gcmId = GCMManager.initGcm(SplashActivity.this);
		UtilisateurBean user = new UtilisateurRepository(SplashActivity.this).getByIdDistant(PreferencesUtil.getCurrentUid());
		new WSSendUser(SplashActivity.this, user, gcmId, new WSPostBase.PostCallback<UtilisateurBean>() {
			@Override public void done(Exception e) {
				finish();
			}
			@Override public void onGetObject(UtilisateurBean object) {}
		}).execute();
	}

	public void starAnimation() {
		btnConnexion.setEnabled(false);
		btnInscription.setEnabled(false);
		btnMdpLost.setEnabled(false);
		pseudo.setEnabled(false);
		mdp.setEnabled(false);
	}
}

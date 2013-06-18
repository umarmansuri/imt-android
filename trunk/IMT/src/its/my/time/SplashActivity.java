package its.my.time;

import java.util.Calendar;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.data.ws.GCMManager;
import its.my.time.data.ws.WSGetBase;
import its.my.time.data.ws.WSLogin;
import its.my.time.data.ws.WSManager;
import its.my.time.data.ws.WSPostBase;
import its.my.time.data.ws.user.UtilisateurBeanWS;
import its.my.time.data.ws.user.WSGetUser;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.receivers.IncomingCallReceiver;
import its.my.time.util.ActivityUtil;
import its.my.time.util.CallManager;
import its.my.time.util.PreferencesUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private Button btnConnexion;
	private TextView btnInscription;
	private TextView btnMdpLost;
	private EditText pseudo;
	private EditText mdp;
	private OnClickListener clickListener;
	private ProgressDialog dialog;
	private IncomingCallReceiver callReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction(CallManager.INTENT_FILTER);
		callReceiver = new IncomingCallReceiver();
		this.registerReceiver(callReceiver, filter);
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
			dialog.hide();

			if(message.what == 0) {

				dialog = new ProgressDialog(SplashActivity.this);
				dialog.setTitle("Patience");
				dialog.setMessage("Synchronisation en cours...");
				dialog.setCancelable(true);
				dialog.show();
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
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(dialog == null) {
					dialog = new ProgressDialog(SplashActivity.this);
				}
				dialog.setTitle("Patience");
				dialog.setMessage("Synchronisation en cours...");
				dialog.setCancelable(true);
				if(!dialog.isShowing()) {
					dialog.show();
				}
			}
		});
		CallManager.initializeManager(getBaseContext());
		GCMManager.initGcm(SplashActivity.this);
		UtilisateurBean user = new UtilisateurRepository(SplashActivity.this).getByIdDistant(PreferencesUtil.getCurrentUid());
		new WSSendUser(SplashActivity.this, user, new WSPostBase.PostCallback<UtilisateurBean>() {
			@Override public void done(Exception e) {

				if(Calendar.getInstance().getTimeInMillis() - PreferencesUtil.getLastUpdate().getTimeInMillis() >= PreferencesUtil.getSynchroInterval() * 60 * 1000) {
					WSManager.updateAllData(SplashActivity.this, new its.my.time.data.ws.Callback() {
						@Override
						public void done(Exception e) {
							ActivityUtil.startCalendarActivity(SplashActivity.this);
							finish();										
						}
					});
				} else {
					ActivityUtil.startCalendarActivity(SplashActivity.this);
					finish();
				}
			}
			@Override public void onGetObject(UtilisateurBean object) {}
		}).execute();
	}

	public void starAnimation() {
		Animation animClock = new RotateAnimation(0f, 35f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animClock.setFillBefore(true);
		animClock.setDuration(2000);
		animClock.setFillAfter(true);

		btnConnexion.setEnabled(false);
		btnInscription.setEnabled(false);
		btnMdpLost.setEnabled(false);
		pseudo.setEnabled(false);
		mdp.setEnabled(false);
		findViewById(R.id.splash_foreground).startAnimation(animClock);
	}
}

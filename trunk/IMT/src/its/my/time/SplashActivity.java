package its.my.time;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.bdd.event.EventBean;
import its.my.time.data.bdd.event.EventRepository;
import its.my.time.util.ActivityUtil;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		setTheme(android.R.style.Theme_Black_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		startClockAnimation();

		//TODO enlever
		//deleteDatabase(DatabaseHandler.DATABASE_NAME);
		
		new LoadMainActivity().execute();
	}

	public void startClockAnimation() {
		Animation anim = new RotateAnimation(0f, 360f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setRepeatMode(Animation.INFINITE);
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3500);
		findViewById(R.id.splash_foreground).setAnimation(anim);
		findViewById(R.id.splash_foreground).getAnimation().start();
	}

	private class LoadMainActivity extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			
			//TODO enlever
			//deleteDatabase(DatabaseHandler.DATABASE_NAME);
			
			EventRepository adapter = new EventRepository(SplashActivity.this);
			for(int i = 0; i < 15; i++) {
				EventBean event = new EventBean();
				event.setCid(0);
				event.setDetails("Détails de l'event " + i);
				event.setId(i);
				event.setTitle("Titre de l'event 1");
				adapter.insertEvent(event);

			}
			return null;
		}

			@Override
			protected void onPostExecute(Void result) {

				Animation anim = new AlphaAnimation(1, 0);
				anim.setDuration(500);
				anim.setFillAfter(true);
				findViewById(R.id.splash_fliper_chgt).startAnimation(anim);

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_top);
						anim.setDuration(2000);
						anim.setFillAfter(true);
						findViewById(R.id.splash_main).startAnimation(anim);
					}
				}, 1000);

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						findViewById(R.id.splash_main).setVisibility(View.INVISIBLE);
						ActivityUtil.startCalendarActivity(SplashActivity.this);
						finish();
					}
				}, 2000);
			}
		}
	}

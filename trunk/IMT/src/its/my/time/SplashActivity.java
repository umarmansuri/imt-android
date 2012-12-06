package its.my.time;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.util.ActivityUtil;

import java.util.GregorianCalendar;

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
			deleteDatabase(DatabaseHandler.DATABASE_NAME);
			EventBaseRepository adapter = new EventBaseRepository(SplashActivity.this);
			EventBaseBean bean;
			GregorianCalendar calDeb2;
			GregorianCalendar calFin2;
			
			bean = new EventBaseBean(); 
			bean.setId(2);
			bean.setTitle("Titre 2");
			calDeb2 = new GregorianCalendar(2012,11,5,8,0);
			bean.sethDeb(calDeb2);
			calFin2 = new GregorianCalendar(2012,11,5,9,30);
			bean.sethFin(calFin2);
			bean.setTypeId(2);
			bean.setDetailsId(0);
			long res = adapter.insertEvent(bean);
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

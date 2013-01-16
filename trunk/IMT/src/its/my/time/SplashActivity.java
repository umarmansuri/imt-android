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
import android.view.animation.Animation;
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
		Animation anim = new RotateAnimation(-20f, 0f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(3000);
		anim.setFillAfter(true);
		findViewById(R.id.splash_foreground).setAnimation(anim);
		findViewById(R.id.splash_foreground).getAnimation().start();
	}

	private class LoadMainActivity extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			
			//TODO enlever
			/*deleteDatabase(DatabaseHandler.DATABASE_NAME);
			EventBaseRepository adapter = new EventBaseRepository(SplashActivity.this);
			EventBaseBean bean;
			GregorianCalendar calDeb2;
			GregorianCalendar calFin2;
			
			
			bean = new EventBaseBean(); 
			bean.setId(2);
			bean.setTitle("Titre 2");
			calDeb2 = new GregorianCalendar(2013,1,20,12,0);
			bean.sethDeb(calDeb2);
			calFin2 = new GregorianCalendar(2013,1,20,13,0);
			bean.sethFin(calFin2);
			bean.setTypeId(EventBaseRepository.Types.TYPE_MEETING);
			bean.setDetailsId(0);
			long res = adapter.insertEvent(bean);*/
			
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

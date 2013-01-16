package its.my.time.pages.calendar;

import fonts.mooncake.MooncakeIcone;
import its.my.time.R;
import its.my.time.pages.calendar.base.BasePagerAdapter;
import its.my.time.pages.calendar.day.DayPagerAdapter;
import its.my.time.pages.calendar.list.ListEventAdapter;
import its.my.time.pages.calendar.month.MonthPagerAdapter;
import its.my.time.pages.editable.profil.ProfilActivity;

import java.util.Calendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CalendarActivity extends SherlockFragmentActivity implements
		OnClickListener, OnPageChangeListener {

	private ArrayAdapter<CharSequence> listMenu;

	public static final int INDEX_PAGER_DAY = 0;
	public static final int INDEX_PAGER_MONTH = 1;
	public static final int INDEX_PAGER_LISTE = 2;
	public static final long ANIM_DURATION = 500;

	public static final int INDEX_NAVIGATION_DAY = 0;
	public static final int INDEX_NAVIGATION_MONTH = 1;
	public static final int INDEX_NAVIGATION_LISTE = 2;

	public static final int INDEX_MENU_SYNC = 0;
	public static final int INDEX_MENY_TODAY = 1;
	public static final int INDEX_MENU_PROFIL = 2;

	private static final int ID_PAGER = 888889;
	private static final int DURATION_WAITING_END = 300;
	private int indexCurrentPager = -1;

	private FrameLayout mainFramePager;
	private ViewPager mViewPager;

	private TextView mTextTitle;

	private boolean isMenuShowed = false;

	private View mMainFrameLayout;

	public static Calendar curentCal;

	private static boolean isFirstMenuSelectedOk;
	private static boolean isWaitingEnd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		isWaitingEnd = false;
		isFirstMenuSelectedOk = false;
		initialiseActionBar();
		setContentView(R.layout.activity_calendar);

		mMainFrameLayout = findViewById(R.id.main_layout);
		mainFramePager = (FrameLayout) findViewById(R.id.main_pager);
		setMainMenuShowed(false, false);

		if (curentCal == null) {
			curentCal = Calendar.getInstance();
			new ChangePageTask().execute(INDEX_PAGER_MONTH);
		}
	}

	private void initialiseActionBar() {

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background_header));
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		mActionBar.setDisplayShowCustomEnabled(true);
		mTextTitle = new TextView(this);
		mTextTitle.setGravity(Gravity.CENTER);
		mTextTitle.setTextSize(20);
		mTextTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mTextTitle.setTextColor(getResources().getColor(
				R.color.background_other));
		mActionBar.setCustomView(mTextTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (isMenuShowed == false) {
				setMainMenuShowed(true, true);
			} else {
				setMainMenuShowed(false, true);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setMainMenuShowed(boolean showed, boolean withAnimation) {
		Animation anim;
		if (showed) {
			anim = new TranslateAnimation(-400, 0, 0, 0);
		} else {
			anim = new TranslateAnimation(0, -400, 0, 0);
		}
		anim.setFillAfter(true);
		if (withAnimation) {
			anim.setDuration(200);
		}
		mMainFrameLayout.startAnimation(anim);
		isMenuShowed = showed;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_calendar, menu);

		MooncakeIcone icone = new MooncakeIcone(this,
				MooncakeIcone.icon_calendar);
		icone.setId(R.id.menu_today);
		icone.setOnClickListener(this);
		icone.setTextColor(getResources().getColor(R.color.background_other));
		menu.findItem(R.id.menu_today).setActionView(icone);

		icone = new MooncakeIcone(this, MooncakeIcone.icon_user);
		icone.setId(R.id.menu_profil);
		icone.setOnClickListener(this);
		icone.setTextColor(getResources().getColor(R.color.background_other));
		menu.findItem(R.id.menu_profil).setActionView(icone);

		return super.onCreateOptionsMenu(menu);
	}

	public void showDays(Calendar cal) {
		curentCal = cal;
		getSupportActionBar().setSelectedNavigationItem(INDEX_NAVIGATION_DAY);
	}

	public void showListe() {
		getSupportActionBar().setSelectedNavigationItem(INDEX_PAGER_LISTE);
	}

	public void showMonths(Calendar cal) {
		curentCal = cal;
		getSupportActionBar().setSelectedNavigationItem(INDEX_NAVIGATION_MONTH);
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (isFirstMenuSelectedOk == false) {
			isFirstMenuSelectedOk = true;
			return true;
		}
		switch (itemPosition) {
		case INDEX_NAVIGATION_DAY:
			if (indexCurrentPager == INDEX_PAGER_DAY) {
				Toast.makeText(this, "Vous êtes déjà en vue jour!",
						Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_PAGER_DAY);
			}
			return true;
		case INDEX_NAVIGATION_MONTH:
			if (indexCurrentPager == INDEX_PAGER_MONTH) {
				Toast.makeText(this, "Vous êtes déjà en vue mois!",
						Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_PAGER_MONTH);
			}
			return true;
		case INDEX_NAVIGATION_LISTE:
			if (indexCurrentPager == INDEX_PAGER_LISTE) {
				Toast.makeText(this, "Vous êtes déjà en vue liste!",
						Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_PAGER_LISTE);
			}
			return true;
		}

		return false;
	}

	private void gotoDate(Calendar cal) {
		((BasePagerAdapter) ((ViewPager) mainFramePager.getChildAt(0))
				.getAdapter()).setCurrentCalendar(cal);
		((ViewPager) mainFramePager.getChildAt(0))
				.setCurrentItem(BasePagerAdapter.NB_PAGE / 2);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (indexCurrentPager) {
			case INDEX_PAGER_DAY:
				showMonths(curentCal);
				return true;
			case INDEX_PAGER_MONTH:
				if (isWaitingEnd) {
					finish();
				} else {
					isWaitingEnd = true;
					Toast.makeText(this,
							"Appuyer une nouvelle fois pour quitter",
							DURATION_WAITING_END).show();
					new Thread(new Runnable() {
						public void run() {
							try {
								Thread.sleep(DURATION_WAITING_END * 10);
							} catch (Exception e) {
							}
							isWaitingEnd = false;
						}
					}).start();
				}
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_profil:
			Intent intent = new Intent(this, ProfilActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_today:
			gotoDate(Calendar.getInstance());
			break;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(final int position) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTextTitle.setText(((BasePagerAdapter) mViewPager.getAdapter())
						.getTitle(position));
			}
		});
	}

	private class ChangePageTask extends AsyncTask<Integer, Void, View> {

		@Override
		protected void onPreExecute() {
			Animation anim = new AlphaAnimation(1, 0);
			anim.setFillAfter(true);
			anim.setDuration(ANIM_DURATION);
			mainFramePager.startAnimation(anim);
		}

		@Override
		protected View doInBackground(Integer... params) {
			int indexNextPage = params[0];
			mViewPager = new ViewPager(getApplicationContext());
			mViewPager.setId(ID_PAGER);
			switch (indexNextPage) {
			case INDEX_PAGER_DAY:
				mViewPager.setAdapter(new DayPagerAdapter(
						getSupportFragmentManager(), curentCal));
				indexCurrentPager = INDEX_PAGER_DAY;
				break;
			case INDEX_PAGER_MONTH:
				mViewPager.setAdapter(new MonthPagerAdapter(
						getSupportFragmentManager(), (Calendar) curentCal));
				mViewPager.getAdapter().notifyDataSetChanged();
				indexCurrentPager = INDEX_PAGER_MONTH;
				break;
			case INDEX_PAGER_LISTE:
				ListView mListView = new ListView(getApplicationContext());
				mListView
						.setAdapter(new ListEventAdapter(CalendarActivity.this));
				indexCurrentPager = INDEX_PAGER_LISTE;
				return mListView;
			}
			mViewPager.setOnPageChangeListener(CalendarActivity.this);
			mViewPager.setCurrentItem(BasePagerAdapter.NB_PAGE / 2);
			return mViewPager;
		}

		@Override
		protected void onPostExecute(View result) {
			mainFramePager.removeAllViews();
			mainFramePager.addView(result);
			Animation anim = new AlphaAnimation(0, 1);
			anim.setFillAfter(true);
			anim.setDuration(ANIM_DURATION);
			mainFramePager.startAnimation(anim);
		}
	}
}

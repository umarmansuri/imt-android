package its.my.time.pages.calendar;

import its.my.time.R;
import its.my.time.pages.calendar.base.BasePagerAdapter;
import its.my.time.pages.calendar.day.DayPagerAdapter;
import its.my.time.pages.calendar.list.ListEventAdapter;
import its.my.time.pages.calendar.month.MonthPagerAdapter;
import its.my.time.pages.editable.compte.ListeCompteAdapter;
import its.my.time.pages.editable.profil.ProfilActivity;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class CalendarActivity extends SherlockFragmentActivity implements OnNavigationListener {

	private ArrayAdapter<CharSequence> listMenu;

	public static final int INDEX_PAGER_DAY = 0;
	public static final int INDEX_PAGER_MONTH = 1;
	public static final int INDEX_PAGER_LISTE = 2;
	public static final long ANIM_DURATION = 500;

	public static final int INDEX_NAVIGATION_DAY = 0;
	public static final int INDEX_NAVIGATION_MONTH= 1;
	public static final int INDEX_NAVIGATION_LISTE= 2;


	public static final int INDEX_MENU_SYNC = 0;
	public static final int INDEX_MENY_TODAY= 1;
	public static final int INDEX_MENU_PROFIL= 2;

	private static final int ID_PAGER = 888889;
	private static final int DURATION_WAITING_END = 300;
	private int indexCurrentPager = -1;

	private FrameLayout mainFramePager;

	public static Calendar curentCal;


	private static boolean isFirstMenuSelectedOk;
	private static boolean isWaitingEnd;


	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		isWaitingEnd = false;
		isFirstMenuSelectedOk = false;
		initialiseActionBar();
		setContentView(R.layout.activity_calendar);

		mainFramePager = (FrameLayout)findViewById(R.id.main_frame_pager);
		((ListView)findViewById(R.id.main_liste_compte)).setAdapter(new ListeCompteAdapter(this));

		curentCal = Calendar.getInstance();
		new ChangePageTask().execute(INDEX_PAGER_MONTH);
	}

	private void initialiseActionBar() {

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setHomeButtonEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		listMenu = ArrayAdapter.createFromResource(this, R.array.array_menu, R.layout.sherlock_spinner_dropdown_item);
		listMenu.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		mActionBar.setListNavigationCallbacks(listMenu, this);
		mActionBar.setSelectedNavigationItem(INDEX_NAVIGATION_MONTH);
		BitmapDrawable dr = (BitmapDrawable)getResources().getDrawable(R.drawable.background_header_drawable);
		dr.setTileModeX(TileMode.REPEAT);
		mActionBar.setBackgroundDrawable(dr);
		setSupportProgressBarIndeterminate(true);
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_calendar, menu);
		
		int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		Bitmap bm = drawTextToBitmap(this, R.drawable.ic_menu_today, String.valueOf(today));
		Drawable dr = new BitmapDrawable(getResources(), bm);
		menu.findItem(R.id.menu_today).setIcon(dr);
		
		return super.onCreateOptionsMenu(menu);
	}

	public Bitmap drawTextToBitmap(Context gContext, 
			int gResId, 
			String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = 
				BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig =
				bitmap.getConfig();
		// set default bitmap config if none
		if(bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable, 
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		// new antialised Paint
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// text color - #3D3D3D
		paint.setColor(Color.WHITE);//rgb(61, 61, 61));
		// text size in pixels
		paint.setTextSize((int) (15 * scale));
		// text shadow
		paint.setShadowLayer(1f, 0f, 1f, Color.rgb(64, 64, 64));

		// draw text to the Canvas center
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width())/2;
		int y = (bitmap.getHeight() + bounds.height()/2)/2;

		canvas.drawText(gText, x * scale, y * scale, paint);

		return bitmap;
	}

	public void showProgressBar(boolean show) {
		setSupportProgressBarIndeterminateVisibility(show);
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

	public Calendar getCurrentCalendar() {
		return curentCal;
	}

	public void setCurrentCalendar(Calendar cal) {
		curentCal = cal;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_profil) {
		} else if(item.getItemId() == R.id.menu_profil) {
			Intent intent = new Intent(this, ProfilActivity.class);
			startActivity(intent);
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_profil:
			Intent intent = new Intent(this, ProfilActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_today:
			gotoDate(Calendar.getInstance());
			return true;
		default:
			return false;
		}
	}

	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if(isFirstMenuSelectedOk == false) {isFirstMenuSelectedOk  = true; return true;}
		switch (itemPosition) {
		case INDEX_NAVIGATION_DAY:
			if(indexCurrentPager == INDEX_PAGER_DAY) {
				Toast.makeText(this, "Vous êtes déjà en vue jour!", Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_PAGER_DAY);
			}
			return true;
		case INDEX_NAVIGATION_MONTH:
			if(indexCurrentPager == INDEX_PAGER_MONTH) {
				Toast.makeText(this, "Vous êtes déjà en vue mois!", Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_PAGER_DAY);
			}
			return true;
		case INDEX_NAVIGATION_LISTE:
			if(indexCurrentPager == INDEX_PAGER_LISTE) {
				Toast.makeText(this, "Vous êtes déjà en vue liste!", Toast.LENGTH_SHORT).show();
			} else {
				new ChangePageTask().execute(INDEX_PAGER_LISTE);
			}
			return true;
		}

		return false;
	}

	private void gotoDate(Calendar cal) {
		((BasePagerAdapter)((ViewPager)mainFramePager.getChildAt(0)).getAdapter()).setCurrentCalendar(cal);
		((ViewPager)mainFramePager.getChildAt(0)).setCurrentItem(BasePagerAdapter.NB_PAGE/2);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			switch (indexCurrentPager) {
			case INDEX_PAGER_DAY:
				showMonths(((BasePagerAdapter)((ViewPager)mainFramePager.getChildAt(0)).getAdapter()).getCurrentCalendar());
				return true;
			case INDEX_PAGER_MONTH:
				if(isWaitingEnd) {
					finish();
				} else {
					isWaitingEnd = true;
					Toast.makeText(this, "Appuyer une nouvelle fois pour quitter", DURATION_WAITING_END).show();
					new Thread(new Runnable() {
						public void run() {
							try{
								Thread.sleep(DURATION_WAITING_END * 10);
							}catch(Exception e) {}
							isWaitingEnd = false;
						}
					}).start();
				}

			}
		}
		return false;
	}

	private class ChangePageTask extends AsyncTask<Integer, Void, View> {

		private int nextIndexMenu;
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
			ViewPager v = new ViewPager(getApplicationContext());
			v.setId(ID_PAGER);
			switch (indexNextPage) {
			case INDEX_PAGER_DAY:
				v.setAdapter(new DayPagerAdapter(getSupportFragmentManager(), curentCal));
				indexCurrentPager = INDEX_PAGER_DAY;
				nextIndexMenu = 1;
				break;
			case INDEX_PAGER_MONTH:
				v.setAdapter(new MonthPagerAdapter(getSupportFragmentManager(), (Calendar) curentCal));
				v.getAdapter().notifyDataSetChanged();
				indexCurrentPager = INDEX_PAGER_MONTH;
				break;
			case INDEX_PAGER_LISTE:
				ListView mListView = new ListView(getApplicationContext());
				mListView.setAdapter(new ListEventAdapter(CalendarActivity.this));
				indexCurrentPager = INDEX_PAGER_LISTE;
				return mListView;
			}
			v.setCurrentItem(BasePagerAdapter.NB_PAGE / 2);
			return v;
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

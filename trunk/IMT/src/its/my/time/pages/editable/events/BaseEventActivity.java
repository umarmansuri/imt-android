package its.my.time.pages.editable.events;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.EventTypes;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;

public abstract class BaseEventActivity extends BaseActivity {

	protected ControledViewPager mPager;
	private static ArrayList<BasePluginFragment> fragments;
	protected EventBaseBean event;

	@Override
	public void onCreate(Bundle savedInstance) {
		Bundle bundle = getIntent().getExtras();
		super.onCreate(bundle);

		setContentView(R.layout.activity_event);

		if (bundle.getInt(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			event = new EventBaseRepository(this).getById(bundle.getInt(ActivityUtil.KEY_EXTRA_ID));
		}
		if (event == null) {
			event = new EventBaseBean();
			event.setTypeId(EventTypes.TYPE_TASK);
			event.sethDeb(DateUtil.getDateFromISO(bundle
					.getString(ActivityUtil.KEY_EXTRA_ISO_TIME)));
			event.sethFin((Calendar) event.gethDeb().clone());
			event.gethFin().add(Calendar.HOUR_OF_DAY, 2);
			event.setAllDay(bundle.getBoolean(ActivityUtil.KEY_EXTRA_ALL_DAY, false));
		}



		mPager = (ControledViewPager) findViewById(R.id.event_pager);
		mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));
		mPager.setOnPageChangeListener(pageListener);
		fragments = getPages();
		mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));
		
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		List<String> titles = new ArrayList<String>();
		for (BasePluginFragment fragment : fragments) {
			titles.add(fragment.getTitle());
		}
		
		mActionBar.setListNavigationCallbacks(new ArrayAdapter<String>(this,
				R.layout.navigation_spinner_item, titles.toArray(new String[]{})), navigationListener);
	}
	
	public abstract ArrayList<BasePluginFragment> getPages();

	@Override
	protected void initialiseActionBar() {
		super.initialiseActionBar();
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	protected OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			getSupportActionBar().setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};

	protected OnNavigationListener navigationListener = new OnNavigationListener() {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			if(mPager.isPagingEnabled()) {
				mPager.setCurrentItem(itemPosition);
			} else if(itemPosition != mPager.getCurrentItem()) {
				getSupportActionBar().setSelectedNavigationItem(mPager.getCurrentItem());
				Toast.makeText(BaseEventActivity.this, "Veuillez enregistrer avant de changer d'onglet", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};

	public BasePluginFragment getActiveFragment() {
		if(mPager.getAdapter() instanceof FragmentStatePagerAdapter) {
			FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) mPager.getAdapter();
			return (BasePluginFragment) a.instantiateItem(mPager, mPager.getCurrentItem());	
		} else {
			String name = makeFragmentName(mPager.getId(), mPager.getCurrentItem());
			return  (BasePluginFragment) getSupportFragmentManager().findFragmentByTag(name);	
		}
	}

	private static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}


	@Override
	protected void showEdit() {
		mPager.setPagingEnabled(false);
		getActiveFragment().launchEdit();
	}

	@Override
	protected void showCancel() {
		mPager.setPagingEnabled(true);
		getActiveFragment().launchCancel();
	}

	@Override
	protected void showSave() {
		mPager.setPagingEnabled(true);
		getActiveFragment().launchSave();
	}

	@Override
	protected void onMenuGroupSwitch(View v, int positionGroup,
			boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMenuItemSwitch(View v, int positionGroup,
			int positionObjet, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMenuChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected ArrayList<MenuGroupe> onMainMenuCreated(
			ArrayList<MenuGroupe> menuGroupes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onViewCreated() {}



	public class EventPagerAdapter extends FragmentPagerAdapter {
		public EventPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}
}

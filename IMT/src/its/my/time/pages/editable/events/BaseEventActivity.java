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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
	private boolean isNew;
	protected EventBaseBean event;

	@Override
	public void onCreate(Bundle savedInstance) {
		final Bundle bundle = getIntent().getExtras();
		super.onCreate(bundle);

		setContentView(R.layout.activity_event);
		
		isNew = false;
		if (bundle.getInt(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			this.event = new EventBaseRepository(this).getById(bundle.getInt(ActivityUtil.KEY_EXTRA_ID));
		}
		if (this.event == null) {
			this.event = new EventBaseBean();
			this.event.setTypeId(EventTypes.TYPE_TASK);
			this.event.sethDeb(DateUtil.getDateFromISO(bundle.getString(ActivityUtil.KEY_EXTRA_ISO_TIME)));
			this.event.sethFin((Calendar) this.event.gethDeb().clone());
			this.event.gethFin().add(Calendar.HOUR_OF_DAY, 2);
			this.event.setAllDay(bundle.getBoolean(ActivityUtil.KEY_EXTRA_ALL_DAY, false));
			isNew = true;
		}

		this.mPager = (ControledViewPager) findViewById(R.id.event_pager);
		this.mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));
		this.mPager.setOnPageChangeListener(this.pageListener);
		fragments = getPages();
		this.mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));

		final ActionBar mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final List<String> titles = new ArrayList<String>();
		for (final BasePluginFragment fragment : fragments) {
			titles.add(fragment.getTitle());
		}

		mActionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(this,
						R.layout.navigation_spinner_item, titles
						.toArray(new String[] {})),
						this.navigationListener);
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
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	protected OnNavigationListener navigationListener = new OnNavigationListener() {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			if (BaseEventActivity.this.mPager.isPagingEnabled()) {
				BaseEventActivity.this.mPager.setCurrentItem(itemPosition);
			} else if (itemPosition != BaseEventActivity.this.mPager
					.getCurrentItem()) {
				getSupportActionBar().setSelectedNavigationItem(
						BaseEventActivity.this.mPager.getCurrentItem());
				Toast.makeText(BaseEventActivity.this,
						"Veuillez enregistrer avant de changer d'onglet",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	};
	public BasePluginFragment getActiveFragment() {
		if (this.mPager.getAdapter() instanceof FragmentStatePagerAdapter) {
			final FragmentStatePagerAdapter a = (FragmentStatePagerAdapter) this.mPager
					.getAdapter();
			return (BasePluginFragment) a.instantiateItem(this.mPager,
					this.mPager.getCurrentItem());
		} else {
			final String name = makeFragmentName(this.mPager.getId(),
					this.mPager.getCurrentItem());
			return (BasePluginFragment) getSupportFragmentManager()
					.findFragmentByTag(name);
		}
	}

	public EventBaseBean getEvent() {
		return event;
	}
	public void setEvent(EventBaseBean event) {
		this.event = event;
	}

	private static String makeFragmentName(int viewId, int index) {
		return "android:switcher:" + viewId + ":" + index;
	}

	@Override
	protected void showEdit() {
		this.mPager.setPagingEnabled(false);
		getActiveFragment().launchEdit();
	}

	@Override
	protected void showCancel() {
		this.mPager.setPagingEnabled(true);
		getActiveFragment().launchCancel();
	}

	@Override
	protected void showSave() {
		this.mPager.setPagingEnabled(true);
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
		
	}

	@Override
	protected boolean onBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onViewCreated() {
		if(isNew) {
			launchEdit();
		}
	}

	public class EventPagerAdapter extends FragmentStatePagerAdapter {
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

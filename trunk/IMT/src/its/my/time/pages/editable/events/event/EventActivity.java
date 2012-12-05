package its.my.time.pages.editable.events.event;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class EventActivity extends BaseActivity{

	private ViewPager mPager;
	private Tab newTabEvent;
	private Tab newTabParticipants;
	private Tab newTabCommentaires;
	private Tab newTabPj;

	//for start from Extra
	private EventBaseBean event;

	@Override
	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.activity_event);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle.getInt(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			
			event = DatabaseUtil.Events.getEventRepository(this).getById(bundle.getInt(ActivityUtil.KEY_EXTRA_ID)); 
		} 
		if(event == null){
			event = new EventBaseBean();
			event.sethDeb(DateUtil.getDateFromISO(bundle.getString(ActivityUtil.KEY_EXTRA_ISO_TIME)));
		}
		
		mPager = (ViewPager) findViewById(R.id.event_pager);
		mPager.setOnPageChangeListener(pageListener);
		mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager(), event));
		
		super.onCreate(bundle);
	}
	

	@Override
	protected CharSequence getActionBarTitle() {
		return "Evènement";
	}

	@Override
	protected void initialiseActionBar() {

		super.initialiseActionBar();

		ActionBar mActionBar = getSupportActionBar();

		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			newTabEvent = getSupportActionBar().newTab();
			newTabEvent.setText(EventPagerAdapter.TITLE_PAGE_DETAILS);
			newTabEvent.setTabListener(tabListener);
			mActionBar.addTab(newTabEvent);

			newTabParticipants = getSupportActionBar().newTab();
			newTabParticipants.setText(EventPagerAdapter.TITLE_PAGE_PARTICIPANTS);
			newTabParticipants.setTabListener(tabListener);
			mActionBar.addTab(newTabParticipants);

			newTabCommentaires = getSupportActionBar().newTab();
			newTabCommentaires.setText(EventPagerAdapter.TITLE_PAGE_COMMENTAIRES);
			newTabCommentaires.setTabListener(tabListener);
			mActionBar.addTab(newTabCommentaires);

			newTabPj = getSupportActionBar().newTab();
			newTabPj.setText(EventPagerAdapter.TITLE_PAGE_PJ);
			newTabPj.setTabListener(tabListener);
			mActionBar.addTab(newTabPj);
		} else {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			String[] items = new String[]{EventPagerAdapter.TITLE_PAGE_DETAILS, EventPagerAdapter.TITLE_PAGE_PARTICIPANTS, EventPagerAdapter.TITLE_PAGE_COMMENTAIRES, EventPagerAdapter.TITLE_PAGE_PJ};
			mActionBar.setListNavigationCallbacks(new ArrayAdapter<String>(this, R.layout.sherlock_spinner_dropdown_item, items), navigationListener);
		}
	}

	private TabListener tabListener = new ActionBar.TabListener() {
		@Override public void onTabReselected(Tab tab, FragmentTransaction ft) {onTabSelected(tab, ft);}
		@Override public void onTabSelected(Tab tab, FragmentTransaction ft) {mPager.setCurrentItem(tab.getPosition());}
		@Override public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	};

	private OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			getSupportActionBar().setSelectedNavigationItem(position);
		}
		@Override public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override public void onPageScrollStateChanged(int arg0) {}
	};

	private OnNavigationListener navigationListener = new OnNavigationListener() {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			mPager.setCurrentItem(itemPosition);
			return true;
		}
	};


}

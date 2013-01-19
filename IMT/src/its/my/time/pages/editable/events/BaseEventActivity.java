package its.my.time.pages.editable.events;

import java.util.Calendar;

import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.view.ControledViewPager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public abstract class BaseEventActivity extends BaseActivity {

	protected ControledViewPager mPager;

	protected EventBaseBean event;

	@Override
	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.activity_event);

		Bundle bundle = getIntent().getExtras();
		if (bundle.getLong(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			event = DatabaseUtil.Events.getEventRepository(this).getById(
					bundle.getLong(ActivityUtil.KEY_EXTRA_ID));
		}
		if (event == null) {
			event = new EventBaseBean();
			event.setTypeId(EventBaseRepository.Types.TYPE_TASK);
			event.sethDeb(DateUtil.getDateFromISO(bundle
					.getString(ActivityUtil.KEY_EXTRA_ISO_TIME)));
			event.sethFin((Calendar) event.gethDeb().clone());
			event.gethFin().add(Calendar.HOUR_OF_DAY, 2);
			event.setAllDay(bundle.getBoolean(ActivityUtil.KEY_EXTRA_ALL_DAY, false));
		}

		mPager = (ControledViewPager) findViewById(R.id.event_pager);
		mPager.setOnPageChangeListener(pageListener);

		super.onCreate(bundle);
	}
	
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

	@Override
	protected void showEdit() {
		mPager.setPagingEnabled(false);
		onEdit();
	}

	@Override
	protected void showCancel() {
		mPager.setPagingEnabled(true);
		onCancel();
	}

	@Override
	protected void showSave() {
		mPager.setPagingEnabled(true);
		onSave();
	}

	protected abstract void onEdit();
	protected abstract void onSave();
	protected abstract void onCancel();

}

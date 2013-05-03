package its.my.time.pages.editable.events;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.Types;
import its.my.time.view.ControledViewPager;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.fonts.mooncake.MooncakeIcone;

public abstract class BaseEventActivity extends BaseActivity {

	protected ControledViewPager mPager;
	private static ArrayList<BasePluginFragment> fragments;
	private boolean isNew;
	protected EventBaseBean event;
	private MenuGroupe menuSuppression;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

		setContentView(R.layout.activity_event);

		final Bundle bundle = getIntent().getExtras();
		isNew = false;
		if (bundle.getInt(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			this.event = new EventBaseRepository(this).getById(bundle.getInt(ActivityUtil.KEY_EXTRA_ID));
		}
		if (this.event == null) {
			this.event = new EventBaseBean();
			this.event.setTypeId(Types.Event.BASE);
			
			boolean isAllDay = bundle.getBoolean(ActivityUtil.KEY_EXTRA_ALL_DAY, false);
			Calendar hDeb = DateUtil.getDateFromISO(bundle.getString(ActivityUtil.KEY_EXTRA_ISO_TIME));
			Calendar hFin;
			if(isAllDay) {
				hDeb.set(Calendar.HOUR_OF_DAY, 0);
				hDeb.set(Calendar.HOUR, 0);
				hDeb.set(Calendar.MINUTE, 0);
				hDeb.set(Calendar.SECOND, 0);
				hFin = (Calendar)hDeb.clone();
				hFin.set(Calendar.HOUR, 23);
				hFin.set(Calendar.MINUTE, 59);
				hFin.set(Calendar.SECOND, 59);
			} else {
				hFin = (Calendar)hDeb.clone();
				hFin.add(Calendar.HOUR, 2);
			}
			this.event.sethDeb(hDeb);
			this.event.sethFin(hFin);
			
			this.event.setAllDay(isAllDay);
			isNew = true;
		}
		fragments = getPages();
		if(fragments == null) {
			fragments = new ArrayList<BasePluginFragment>();
		}
		this.mPager = (ControledViewPager) findViewById(R.id.event_pager);
		this.mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));
		this.mPager.setOnPageChangeListener(this.pageListener);

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

	@Override
	protected ArrayList<MenuGroupe> onCreateMenu(ArrayList<MenuGroupe> menuGroupes) {
		menuSuppression = new MenuGroupe("Supp. événement", MooncakeIcone.icon_remove);
		menuGroupes.add(menuSuppression);
		return super.onCreateMenu(menuGroupes);
	}
	
	@Override
	protected void onMenuGroupClick(ExpandableListView parent,
			MenuGroupe group, long id) {
		if(group==menuSuppression) {
			AlertDialog.Builder alertSupperssion = new AlertDialog.Builder(this);
			alertSupperssion.setTitle("Supprimer l'événement ?");
			
			DialogInterface.OnClickListener listenerButon = new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) { 
						EventBaseRepository evRepo = new EventBaseRepository(getApplicationContext());
						evRepo.delete(getEvent());
						dialog.dismiss();
						finish();
				    }
				    else { 
				        dialog.cancel();
				        
				    }
					
				}
			};
			alertSupperssion.setPositiveButton("Oui", listenerButon);
			alertSupperssion.setNeutralButton("Non", listenerButon);
			alertSupperssion.show();
			
		} else {
			super.onMenuGroupClick(parent, group, id);
		}
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
	protected void onViewCreated() {
		if(isNew) {
			launchEdit();
		}
	}
	
	@Override
	protected boolean onBackButtonPressed() {
		for (BasePluginFragment fragment : fragments) {
			if(fragment.isInEditMode()) {
				Toast.makeText(this, "Attention, certaines modifications n'ont pas été enregistrées.", Toast.LENGTH_SHORT).show();
				return true;
			}
		}
		return super.onBackButtonPressed();
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

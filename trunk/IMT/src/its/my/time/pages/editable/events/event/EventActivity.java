package its.my.time.pages.editable.events.event;

import its.my.time.pages.editable.events.BaseEventActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;

public class EventActivity extends BaseEventActivity {

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		mPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager(),
				event));
	}

	@Override
	protected void onViewCreated() {
	}
	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel évènement";
	}

	@Override
	protected void initialiseActionBar() {

		super.initialiseActionBar();

		ActionBar mActionBar = getSupportActionBar();

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		String[] items = new String[] { EventPagerAdapter.TITLE_PAGE_DETAILS,
				EventPagerAdapter.TITLE_PAGE_PARTICIPANTS,
				EventPagerAdapter.TITLE_PAGE_COMMENTAIRES,
				EventPagerAdapter.TITLE_PAGE_PJ };
		mActionBar.setListNavigationCallbacks(new ArrayAdapter<String>(this,
				R.layout.navigation_spinner_item, items), navigationListener);
	}

	@Override
	protected void onEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCancel() {
		// TODO Auto-generated method stub
		
	}

}

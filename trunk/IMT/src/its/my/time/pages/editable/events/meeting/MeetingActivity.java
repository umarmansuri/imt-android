package its.my.time.pages.editable.events.meeting;

import its.my.time.pages.editable.events.BaseEventActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;

public class MeetingActivity extends BaseEventActivity {


	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		mPager.setAdapter(new MeetingPagerAdapter(getSupportFragmentManager(),
				event));
	}

	@Override
	protected void onViewCreated() {
	}
	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvelle réunion";
	}

	@Override
	protected void initialiseActionBar() {

		super.initialiseActionBar();

		ActionBar mActionBar = getSupportActionBar();

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		String[] items = new String[] { MeetingPagerAdapter.TITLE_PAGE_DETAILS,
				MeetingPagerAdapter.TITLE_PAGE_PARTICIPANTS,
				MeetingPagerAdapter.TITLE_PAGE_COMMENTAIRES,
				MeetingPagerAdapter.TITLE_PAGE_PJ };
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

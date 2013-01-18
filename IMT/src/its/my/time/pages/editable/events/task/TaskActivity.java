package its.my.time.pages.editable.events.task;

import its.my.time.pages.editable.events.BaseEventActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;

public class TaskActivity extends BaseEventActivity {

	private ViewPager mPager;
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		mPager.setAdapter(new TaskPagerAdapter(getSupportFragmentManager(),
				event));
	}

	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvelle t�che";
	}

	@Override
	protected void initialiseActionBar() {

		super.initialiseActionBar();

		ActionBar mActionBar = getSupportActionBar();

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		String[] items = new String[] { TaskPagerAdapter.TITLE_PAGE_DETAILS,
				TaskPagerAdapter.TITLE_PAGE_PARTICIPANTS,
				TaskPagerAdapter.TITLE_PAGE_COMMENTAIRES,
				TaskPagerAdapter.TITLE_PAGE_PJ };
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
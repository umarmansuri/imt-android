package its.my.time.pages.editable.events.call;

import its.my.time.pages.editable.events.BaseEventActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.R;
import com.actionbarsherlock.app.ActionBar;

public class CallActivity extends BaseEventActivity {


	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		mPager.setAdapter(new CallPagerAdapter(getSupportFragmentManager(),
				event));
	}

	@Override
	protected CharSequence getActionBarTitle() {
		return "Nouvel appel";
	}

	@Override
	protected void initialiseActionBar() {

		super.initialiseActionBar();
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		String[] items = new String[] { CallPagerAdapter.TITLE_PAGE_DETAILS,
				CallPagerAdapter.TITLE_PAGE_COMMENTAIRES };
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

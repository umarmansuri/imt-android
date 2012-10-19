package its.my.time.pages.editable.compte;

import its.my.time.pages.editable.BaseActivity;

import com.actionbarsherlock.R;

public class CompteActivity extends BaseActivity {

	public static final String KEY_EXTRA_ID = "key_id";

	@Override
	protected int getContentViewId() {
		return R.layout.activity_compte;
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Compte";
	}

}

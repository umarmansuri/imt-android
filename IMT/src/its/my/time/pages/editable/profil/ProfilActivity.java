package its.my.time.pages.editable.profil;

import its.my.time.pages.editable.BaseActivity;

import com.actionbarsherlock.R;

public class ProfilActivity extends BaseActivity {

	@Override
	protected int getContentViewId() {
		return R.layout.activity_profile;
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Profil";
	}

}

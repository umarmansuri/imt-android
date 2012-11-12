package its.my.time.pages.editable.profil;

import its.my.time.pages.editable.BaseActivity;
import android.os.Bundle;

import com.actionbarsherlock.R;

public class ProfilActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstance) {
		setContentView(R.layout.activity_profile);
		super.onCreate(savedInstance);
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Profil";
	}

}

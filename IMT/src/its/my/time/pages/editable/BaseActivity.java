package its.my.time.pages.editable;

import fonts.mooncake.MooncakeIcone;
import its.my.time.R;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseActivity extends SherlockFragmentActivity {

	private Menu menu;

	public static final int INDEX_MENU_EDIT = 0;
	public static final int INDEX_MENU_SAVE = 1;
	public static final int INDEX_MENU_CANCEL = 2;
	@Override
	protected void onCreate(Bundle bundle) {
		initialiseActionBar();
		super.onCreate(bundle);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	private void showEdit() {
		menu.getItem(INDEX_MENU_EDIT).setVisible(false);
		menu.getItem(INDEX_MENU_SAVE).setVisible(true);
		menu.getItem(INDEX_MENU_CANCEL).setVisible(true);
	}

	protected void initialiseActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setTitle(getActionBarTitle());
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setLogo(android.R.drawable.ic_menu_today);
		mActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_header));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_editable, menu);
		this.menu = menu;
		
		MooncakeIcone icone = new MooncakeIcone(this, MooncakeIcone.icon_edit);
		icone.setId(R.id.menu_today);
		icone.setTextColor(getResources().getColor(R.color.background_other));
		menu.getItem(INDEX_MENU_EDIT).setVisible(true);
		menu.getItem(INDEX_MENU_EDIT).setActionView(icone);

		icone = new MooncakeIcone(this, MooncakeIcone.icon_install);
		icone.setId(R.id.menu_today);
		icone.setTextColor(getResources().getColor(R.color.background_other));
		menu.getItem(INDEX_MENU_SAVE).setActionView(icone);
		
		icone = new MooncakeIcone(this, MooncakeIcone.icon_remove_circle);
		icone.setId(R.id.menu_today);
		icone.setTextColor(getResources().getColor(R.color.background_other));
		menu.getItem(INDEX_MENU_CANCEL).setActionView(icone);
		
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_edit:
			showEdit();
			return true;
		case android.R.id.home:
		case R.id.menu_cancel:
			finish();
			return true;
		case R.id.menu_save:
		default:
			return false;
		}
	}

	protected abstract CharSequence getActionBarTitle();
}

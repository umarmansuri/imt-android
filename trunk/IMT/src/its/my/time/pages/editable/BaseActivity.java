package its.my.time.pages.editable;

import its.my.time.R;
import its.my.time.pages.MyTimeActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.fonts.mooncake.MooncakeIcone;

public abstract class BaseActivity extends MyTimeActivity {

	private Menu menu;

	public static final int INDEX_MENU_EDIT = 0;
	public static final int INDEX_MENU_SAVE = 1;
	public static final int INDEX_MENU_CANCEL = 2;
	

	@Override
	public void onCreate(Bundle bundle) {
		initialiseActionBar();
		super.onCreate(bundle);
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}

	protected abstract CharSequence getActionBarTitle();

	private final OnClickListener mOnIconeClickLIstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.menu_edit) {
				launchEdit();
				return;
			} else if (v.getId() == R.id.menu_cancel) {
				launchCancel();
				return;
			} else if (v.getId() == R.id.menu_save) {
				launchSave();
				return;
			} else {
				finish();
				return;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_editable, menu);

		MooncakeIcone icone = new MooncakeIcone(this);
		icone.setTextSize(30);
		icone.setIconeRes(MooncakeIcone.icon_edit);
		icone.setTextColor(getResources().getColor(R.color.grey));
		icone.setId(R.id.menu_edit);
		icone.setPadding(20, 0, 20, 0);
		icone.setOnClickListener(this.mOnIconeClickLIstener);
		menu.getItem(INDEX_MENU_EDIT).setActionView(icone);

		icone = new MooncakeIcone(this);
		icone.setTextSize(30);
		icone.setIconeRes(MooncakeIcone.icon_install);
		icone.setTextColor(getResources().getColor(R.color.grey));
		icone.setId(R.id.menu_save);
		icone.setPadding(20, 0, 20, 0);
		icone.setOnClickListener(this.mOnIconeClickLIstener);
		menu.getItem(INDEX_MENU_SAVE).setActionView(icone);

		icone = new MooncakeIcone(this);
		icone.setTextSize(30);
		icone.setIconeRes(MooncakeIcone.icon_remove_circle);
		icone.setTextColor(getResources().getColor(R.color.grey));
		icone.setId(R.id.menu_cancel);
		icone.setPadding(20, 0, 20, 0);
		icone.setOnClickListener(this.mOnIconeClickLIstener);
		menu.getItem(INDEX_MENU_CANCEL).setActionView(icone);

		this.menu = menu;
		final boolean res = super.onCreateOptionsMenu(menu);
		onViewCreated();
		return res;
	}
	
	public void launchEdit() {
		this.menu.getItem(INDEX_MENU_EDIT).setVisible(false);
		this.menu.getItem(INDEX_MENU_SAVE).setVisible(true);
		this.menu.getItem(INDEX_MENU_CANCEL).setVisible(true);
		showEdit();
	}

	public void launchSave() {
		this.menu.getItem(INDEX_MENU_EDIT).setVisible(true);
		this.menu.getItem(INDEX_MENU_SAVE).setVisible(false);
		this.menu.getItem(INDEX_MENU_CANCEL).setVisible(false);
		showSave();
	}

	public void launchCancel() {
		this.menu.getItem(INDEX_MENU_EDIT).setVisible(true);
		this.menu.getItem(INDEX_MENU_SAVE).setVisible(false);
		this.menu.getItem(INDEX_MENU_CANCEL).setVisible(false);
		showCancel();
	}

	protected abstract void showEdit();

	protected abstract void showSave();

	protected abstract void showCancel();

	protected abstract void onViewCreated();

	public void setEditVisibility(boolean visible) {
		this.menu.getItem(INDEX_MENU_EDIT).setVisible(visible);
	}

	public void setCancelVisibility(boolean visible) {
		this.menu.getItem(INDEX_MENU_CANCEL).setVisible(visible);
	}

	public void setSaveVisibility(boolean visible) {
		this.menu.getItem(INDEX_MENU_SAVE).setVisible(visible);
	}
}

package its.my.time.pages.editable.events.plugins;

import its.my.time.pages.editable.BaseActivity;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BasePluginFragment extends SherlockFragment {

	@Override
	public void onResume() {
		setRetainInstance(true);
		super.onResume();
	}

	private BaseActivity parentActivity;

	@Override
	public void setMenuVisibility(boolean menuVisible) {

		if (menuVisible) {
			try {
				if (this.parentActivity == null) {
					this.parentActivity = (BaseActivity) getActivity();
				}

				this.parentActivity.setEditVisibility(isEditable());
				// parentActivity.setCancelVisibility(isCancelable());
				// parentActivity.setSaveVisibility(isSavable());

			} catch (final Exception e) {
			}
		}
		super.setMenuVisibility(menuVisible);
	}

	public abstract void launchEdit();

	public abstract void launchSave();

	public abstract void launchCancel();

	public abstract boolean isEditable();

	public abstract boolean isCancelable();

	public abstract boolean isSavable();

	public abstract String getTitle();

}

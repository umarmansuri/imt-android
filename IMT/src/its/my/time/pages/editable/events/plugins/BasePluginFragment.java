package its.my.time.pages.editable.events.plugins;

import its.my.time.pages.editable.events.BaseEventActivity;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BasePluginFragment extends SherlockFragment {


	private BaseEventActivity parentActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.parentActivity = (BaseEventActivity) getActivity();
	}
	
	@Override
	public void onResume() {
		setRetainInstance(true);
		super.onResume();
	}


	@Override
	public void setMenuVisibility(boolean menuVisible) {

		if (menuVisible) {
			try {
				if (this.parentActivity == null) {
					this.parentActivity = (BaseEventActivity) getActivity();
				}

				this.parentActivity.setEditVisibility(isEditable());
				// parentActivity.setCancelVisibility(isCancelable());
				// parentActivity.setSaveVisibility(isSavable());

			} catch (final Exception e) {
			}
		}
		super.setMenuVisibility(menuVisible);
	}
	
	public BaseEventActivity getParentActivity() {
		return parentActivity;
	}
	
	public abstract void launchEdit();

	public abstract void launchSave();

	public abstract void launchCancel();

	public abstract boolean isEditable();

	public abstract boolean isCancelable();

	public abstract boolean isSavable();

	public abstract String getTitle();

}

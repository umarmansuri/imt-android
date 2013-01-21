package its.my.time.pages.editable.events.plugins;

import its.my.time.pages.editable.BaseActivity;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BasePluginFragment extends SherlockFragment{

	private BaseActivity parentActivity;
	@Override
	public void setMenuVisibility(boolean menuVisible) {

		if(menuVisible) {
			try {
				if(parentActivity == null) {
					parentActivity = (BaseActivity)getActivity();
				}

				parentActivity.setEditVisibility(isEditable());
				//parentActivity.setCancelVisibility(isCancelable());
				//parentActivity.setSaveVisibility(isSavable());

			} catch (Exception e) {}
		}
		super.setMenuVisibility(menuVisible);
	}

	public abstract void launchEdit();
	public abstract void launchSave();
	public abstract void launchCancel();

	public abstract boolean isEditable();
	public abstract boolean isCancelable();
	public abstract boolean isSavable();



}
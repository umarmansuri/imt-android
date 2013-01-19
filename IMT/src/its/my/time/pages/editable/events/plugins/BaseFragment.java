package its.my.time.pages.editable.events.plugins;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment{

	public abstract void launchEdit();
	public abstract void launchSave();
	public abstract void launchCancel();
	
}

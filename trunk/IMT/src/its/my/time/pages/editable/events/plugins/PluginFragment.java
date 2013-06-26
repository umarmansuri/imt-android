package its.my.time.pages.editable.events.plugins;

import its.my.time.pages.editable.events.BaseEventActivity;

public interface PluginFragment {

	public BaseEventActivity getParentActivity();
	public boolean isInEditMode();
	public void launchEdit();
	public void launchSave();
	public void launchCancel();
	public abstract boolean isEditable();
	public abstract boolean isCancelable();
	public abstract boolean isSavable();
	public abstract String getTitle();
	
	public void refresh();
}

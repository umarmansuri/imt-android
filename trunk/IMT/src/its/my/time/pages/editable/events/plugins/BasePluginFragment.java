package its.my.time.pages.editable.events.plugins;

import its.my.time.pages.editable.events.BaseEventActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BasePluginFragment extends SherlockFragment implements PluginFragment{


	private BaseEventActivity parentActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.parentActivity = (BaseEventActivity) getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onResume() {
		setRetainInstance(true);
		super.onResume();
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {

		Log.d("Visibility","" + menuVisible);
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
	

	private boolean isInEditMode = false;
	public boolean isInEditMode() {
		return isInEditMode;
	}
	
	
	
	public void launchEdit(){
		isInEditMode = true;
	}
	public void launchSave(){
		isInEditMode = false;
	}
	public void launchCancel(){
		isInEditMode = false;
	}
	public abstract boolean isEditable();
	public abstract boolean isCancelable();
	public abstract boolean isSavable();
	public abstract String getTitle();

	
}

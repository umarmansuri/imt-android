package its.my.time.pages.calendar.base;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.pages.MyTimeActivity;
import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.pages.calendar.base.BaseFragment.OnViewCreated;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public abstract class BaseView extends FrameLayout {

	protected static int nbPageLoading = 0;
	private OnViewCreated onViewCreated;

	public BaseView(Context context, OnViewCreated onViewCreated) {
		super(context);
		init();
		this.onViewCreated = onViewCreated;
	}

	protected void onViewCreated(){
		if(onViewCreated != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
					onViewCreated.onViewCreated(BaseView.this);
					} catch (Exception e) {}
				}
			});

		}
	}
	
	private void init() {
		((CalendarActivity) getContext()).setSupportProgressBarVisibility(true);
	}


	public MyTimeActivity getActivity() {
		return (MyTimeActivity)getContext();
	}


	protected abstract String getTopBarText();

	public abstract View addEvent(EventBaseBean eventBaseBean, String color, int visibility);

}
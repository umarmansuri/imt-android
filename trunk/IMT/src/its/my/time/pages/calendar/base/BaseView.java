package its.my.time.pages.calendar.base;

import its.my.time.R;
import its.my.time.pages.calendar.CalendarActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class BaseView extends FrameLayout{

	protected static int nbPageLoading = 0;
	
	public BaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//setVisibility(INVISIBLE);

		((CalendarActivity)getContext()).setSupportProgressBarVisibility(true);
		new LoadView().execute();
	}

	public BaseView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaseView(Context context) {
		this(context, null);
	}

	protected abstract View createView();
	protected abstract String getTopBarText();
	
	private class LoadView extends AsyncTask<Void, Void, View> {

		@Override
		protected void onPreExecute() {
			nbPageLoading++;
			((CalendarActivity)getContext()).setSupportProgressBarIndeterminateVisibility(true);
		}
		
		@Override
		protected View doInBackground(Void... params) {
			View view = createView();
			((TextView)view.findViewById(R.id.topbar)).setText(getTopBarText());
			view.setVisibility(INVISIBLE);
			return view;
		}
		
		@Override
		protected void onPostExecute(View result) {
			removeAllViews();
			addView(result);
			Animation anim = new AlphaAnimation(0, 1);
			anim.setFillAfter(true);
			anim.setDuration(500);
			result.startAnimation(anim);
			
			nbPageLoading--;
			if(nbPageLoading == 0) {
				((CalendarActivity)getContext()).setSupportProgressBarIndeterminateVisibility(false);
			}
		}
	}
	
	
}
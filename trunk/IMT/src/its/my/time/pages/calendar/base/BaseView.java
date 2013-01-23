package its.my.time.pages.calendar.base;

import its.my.time.pages.calendar.CalendarActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

public abstract class BaseView extends FrameLayout {

	protected static int nbPageLoading = 0;

	public BaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		((CalendarActivity) getContext()).setSupportProgressBarVisibility(true);
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
			((CalendarActivity) getContext()).setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected View doInBackground(Void... params) {
			final View view = createView();
			return view;
		}

		@Override
		protected void onPostExecute(View result) {
			removeAllViews();
			addView(result);
			nbPageLoading--;
			if (nbPageLoading == 0) {
				((CalendarActivity) getContext())
						.setSupportProgressBarIndeterminateVisibility(false);
			}
		}
	}

}
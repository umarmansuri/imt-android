package its.my.time.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ControledViewPager extends ViewPager {

	private boolean pagingEnabled;

	public ControledViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.pagingEnabled = true;
	}

	public ControledViewPager(Context context) {
		super(context);
		this.pagingEnabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.pagingEnabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.pagingEnabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.pagingEnabled = enabled;
	}

	public boolean isPagingEnabled() {
		return this.pagingEnabled;
	}

}
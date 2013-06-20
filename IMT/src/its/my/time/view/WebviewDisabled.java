package its.my.time.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class WebviewDisabled extends WebView {

	private OnTouchListener listener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
	};

	public WebviewDisabled(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	public WebviewDisabled(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WebviewDisabled(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WebviewDisabled(Context context) {
		super(context);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if(enabled) {
			setOnTouchListener(null);
		} else {
			setOnTouchListener(listener);
		}
	}

}

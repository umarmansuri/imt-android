package its.my.time.util;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ViewUtil {

	public static int HEURE_WIDTH = 40;

	public static ProgressBar getProgressBar(Context context) {
		ProgressBar bar = new ProgressBar(context);
		return bar;
	}

	public static RelativeLayout.LayoutParams getProgressBarParams() {
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		return params;
	}

	
}

package its.my.time.util;

import its.my.time.view.ControledViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {

	private static void enableDisableViewGroup(ViewGroup viewGroup,
			boolean enabled) {
		final int childCount = viewGroup.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View view = viewGroup.getChildAt(i);
			if (view instanceof ControledViewPager) {
				((ControledViewPager) view).setPagingEnabled(enabled);
			}
			if (view instanceof ViewGroup) {
				enableDisableViewGroup((ViewGroup) view, enabled);
			}
			view.setEnabled(enabled);
		}
	}

	public static void enableAllView(View view, boolean enabled) {
		try {
			enableDisableViewGroup((ViewGroup) view, enabled);
		} catch (final Exception e) {
			view.setEnabled(enabled);
		}
	}
}

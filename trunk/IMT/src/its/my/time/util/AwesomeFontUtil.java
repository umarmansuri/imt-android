package its.my.time.util;

import android.content.Context;
import android.graphics.Typeface;


public class AwesomeFontUtil {

	private static Typeface font;

	public static Typeface getFont(Context context) {
		if(font == null) {
			font = Typeface.createFromAsset(context.getAssets(), "FontAwesome.ttf");
		}
		return font;
	}
}


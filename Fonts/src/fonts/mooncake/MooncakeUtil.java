package fonts.mooncake;

import android.content.Context;
import android.graphics.Typeface;


public class MooncakeUtil {

	private static Typeface font;

	public static Typeface getFont(Context context) {
		if(font == null) {
			font = Typeface.createFromAsset(context.getAssets(), "icomoon.ttf");
		}
		return font;
	}
}


package its.my.time.util;

import its.my.time.R;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class ColorUtil {

	public static int getInversColor(int color) {
		final float[] hsv = new float[3];
		final int colorDarker = color;
		Color.colorToHSV(colorDarker, hsv);
		if (hsv[2] > 0.5) {
			return getDarkerColor(color);
		} else {
			return getLighterColor(color);
		}
	}

	public static int getDarkerColor(int color) {
		final float[] hsv = new float[3];
		int colorDarker = color;
		Color.colorToHSV(colorDarker, hsv);
		hsv[2] *= 0.7f;
		colorDarker = Color.HSVToColor(hsv);
		return colorDarker;
	}

	public static int getLighterColor(int color) {
		final float[] hsv = new float[3];
		int colorDarker = color;
		Color.colorToHSV(colorDarker, hsv);
		hsv[2] *= 1.6f;
		colorDarker = Color.HSVToColor(hsv);
		return colorDarker;
	}
	
	public static int getDrawableRes(String label) {
		for (ColorMyTime color : colors) {
			if(color.label.equals(label)) {
				return color.res;
			}
		}
		return 0;
	}

	
	public static final ColorMyTime RED = new ColorMyTime(R.drawable.color_red, "fc-event-red");
	public static final ColorMyTime BLUE = new ColorMyTime(R.drawable.color_blue, "fc-event-blue");
	public static final ColorMyTime GREEN = new ColorMyTime(R.drawable.color_green, "fc-event-green");
	public static final ColorMyTime ORANGE = new ColorMyTime(R.drawable.color_orange, "fc-event-orange");
	public static final ColorMyTime BLACK = new ColorMyTime(R.drawable.color_black, "fc-event-black");

	private static final ColorMyTime[] colors = new ColorMyTime[]{RED, BLUE, GREEN, ORANGE, BLACK};
	
	public static class ColorMyTime {
		public int res;
		public String label;
		public ColorMyTime(int res, String label) {
			super();
			this.res = res;
			this.label = label;
		}
		
	}
}

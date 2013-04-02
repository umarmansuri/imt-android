package its.my.time.util;

import android.graphics.Color;

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
}

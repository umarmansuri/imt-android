package its.my.time.util;

import android.graphics.Color;

public class ColorUtil {

	public static int getInversColor(int color) {
		float[] hsv = new float[3];
		int colorDarker = color;
		Color.colorToHSV(colorDarker, hsv);
		if(hsv[2] > 0.5) {
			return getDarkerColor(color);
		} else {
			return getLighterColor(color);
		}
	}
	
	public static int getDarkerColor(int color) {
		float[] hsv = new float[3];
		int colorDarker = color;
		Color.colorToHSV(colorDarker, hsv);
		hsv[2] *= 0.4f;
		colorDarker = Color.HSVToColor(hsv);
		return colorDarker;
	}
	
	public static int getLighterColor(int color) {
		float[] hsv = new float[3];
		int colorDarker = color;
		Color.colorToHSV(colorDarker, hsv);
		hsv[2] *= 1.6f;
		colorDarker = Color.HSVToColor(hsv);
		return colorDarker;
	}
}

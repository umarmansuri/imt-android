package its.my.time.util;

import android.content.Context;

public class PreferencesUtil {

	private static final String KEY_CURREN_UID = "current_uid";

	private static long uid = -1;

	public static long getCurrentUid(Context context) {
		if (uid < 0) {
			uid = PreferenceConnector.readLong(context, KEY_CURREN_UID, uid);
		}
		return uid;
	}

	public static void setCurrentUid(Context context, long uid) {
		PreferenceConnector.writeLong(context, KEY_CURREN_UID, uid);
		PreferencesUtil.uid = uid;
	}

}
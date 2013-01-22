package its.my.time.util;

import android.content.Context;

public class PreferencesUtil {

	private static final String KEY_CURRENT_UID = "KEY_CURRENT_UID";
	private static final String KEY_CURRENT_TOKEN = "KEY_CURRENT_TOKEN";

	private static long uid = -1;
	private static String accesToken = null;


	public static long getCurrentUid(Context context) {
		if (uid < 0) {
			uid = PreferenceConnector.readLong(context, KEY_CURRENT_UID, uid);
		}
		return uid;
	}

	public static void setCurrentUid(Context context, long uid) {
		PreferenceConnector.writeLong(context, KEY_CURRENT_UID, uid);
		PreferencesUtil.uid = uid;
	}

	public static void setAccesToken(Context context, String accesToken) {
		PreferenceConnector.writeString(context, KEY_CURRENT_TOKEN, accesToken);
		PreferencesUtil.accesToken = accesToken;
	}
	public static String getCurrentAccessToken(Context context) {
		if (accesToken == null) {
			accesToken = PreferenceConnector.readString(context, KEY_CURRENT_TOKEN, null);
		}
		return accesToken;
	}
	
	public static void clearAll(Context context) {
		PreferenceConnector.getEditor(context).remove(KEY_CURRENT_TOKEN);
		PreferenceConnector.getEditor(context).remove(KEY_CURRENT_UID);
	}
}
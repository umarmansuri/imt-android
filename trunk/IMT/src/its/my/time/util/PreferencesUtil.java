package its.my.time.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {

	private static final String KEY_CURRENT_UID = "KEY_CURRENT_UID";
	private static final String KEY_CURRENT_TOKEN = "KEY_CURRENT_TOKEN";

	private static long uid = -1;
	private static String accesToken = null;

	private static Context context;

	public static void init(Context context) {
		PreferencesUtil.context = context;
	}

	public static final String PREF_NAME = "IMT";
	public static final int MODE = Context.MODE_PRIVATE;
	private static final String SHPREF_KEY_REFRESH_TOKEN = "SHPREF_KEY_REFRESH_TOKEN";
	private static final String SHPREF_KEY_ACCESS_TOKEN = "SHPREF_KEY_ACCESS_TOKEN";

	public static void writeBoolean(String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(String key,
			boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(String key, String value) {
		getEditor(context).putString(key, value).commit();

	}

	public static String readString(String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static long getCurrentUid() {
		if (uid < 0) {
			uid = readLong(KEY_CURRENT_UID, uid);
		}
		return uid;
	}

	public static void setCurrentUid(long uid) {
		writeLong(KEY_CURRENT_UID, uid);
		PreferencesUtil.uid = uid;
	}

	public static void setAccesToken(String accesToken) {
		writeString(KEY_CURRENT_TOKEN, accesToken);
		PreferencesUtil.accesToken = accesToken;
	}

	public static String getCurrentAccessToken() {
		if (accesToken == null) {
			accesToken = readString(KEY_CURRENT_TOKEN, null);
		}
		return accesToken;
	}

	public static void clearAll(Context context) {
		getEditor(context).remove(KEY_CURRENT_TOKEN);
		getEditor(context).remove(KEY_CURRENT_UID);
	}

	public static String getCurrentRefreshToken() {
		return getPreferences(context).getString(SHPREF_KEY_REFRESH_TOKEN, null);
	}

	public static void setCurrentToken(String refreshToken, String accessToken) {
		Editor e = getEditor(context);
		e.putString(SHPREF_KEY_ACCESS_TOKEN, accessToken);
		e.putString(SHPREF_KEY_REFRESH_TOKEN, refreshToken);
		e.commit();
	}
}
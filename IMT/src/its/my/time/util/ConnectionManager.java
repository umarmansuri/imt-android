package its.my.time.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean isOnlineWithMobileNetwork(Context context) {
		if(isOnline(context)) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			for (NetworkInfo netInfo : cm.getAllNetworkInfo()) {
				if(netInfo.isConnectedOrConnecting() && netInfo.getType() != ConnectivityManager.TYPE_MOBILE) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

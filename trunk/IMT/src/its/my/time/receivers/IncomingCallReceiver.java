package its.my.time.receivers;

import its.my.time.pages.call.IncomingCallActivity;
import its.my.time.util.CallManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IncomingCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("SIP","INcoming call!");
		CallManager.loadCall(intent);
		context.startActivity(new Intent(context, IncomingCallActivity.class));
	}
}
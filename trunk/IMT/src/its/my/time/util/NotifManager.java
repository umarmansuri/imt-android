package its.my.time.util;

import its.my.time.Consts;
import its.my.time.R;
import its.my.time.pages.SettingsActivity;
import its.my.time.receivers.ValidateParticipationActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotifManager {

	private static final int NOTIFICATION_ID = 100;

	public static final int STATE_REGISTERING = 0;
	public static final int STATE_REGISTERED = 1;
	public static final int STATE_UNREGISTERED = 2;

	public static void showNotifiaction(Context context, int state) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("My-Time");
		switch (state) {
		case STATE_REGISTERED:
			mBuilder
			.setContentText("En ligne");
			break;
		case STATE_UNREGISTERED:
			mBuilder
			.setContentText("Hors ligne");
			break;
		case STATE_REGISTERING:
			mBuilder
			.setContentText("Connexion en cours");
			break;
		}
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, SettingsActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(SettingsActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		Notification notif = mBuilder.build();
		notif.flags = Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(NOTIFICATION_ID, notif);
	}
	
	
	public static void generateNotification(Context context, String message, int id) {
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context,ValidateParticipationActivity.class);
		notificationIntent.putExtra(Consts.EXTRA_EID, 1);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}
}

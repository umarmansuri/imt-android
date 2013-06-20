package its.my.time.util;

import its.my.time.Consts;
import its.my.time.R;
import its.my.time.SplashActivity;
import its.my.time.pages.settings.SettingsActivity;
import its.my.time.receivers.ValidateParticipationActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotifManager {

	private static final int NOTIF_ID_SIP = 100;

	public static final int STATE_REGISTERING = 0;
	public static final int STATE_REGISTERED = 1;
	public static final int STATE_UNREGISTERED = 2;

	private static Notification voipNotif;

	private static int lastState;


	public static void showVoipNotifiaction(Context context, int state) {
		lastState = state;
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN
				|| !PreferencesUtil.isVoipNotifExtended()) {
			showVoipNotification(context, state);
		} else {
			showVoipNotificationIcs(context, state);
		}
	}

	public static void showVoipNotification(Context context) {
		showVoipNotifiaction(context, lastState);
	}

	private static void showVoipNotification(Context context, int state) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.app_icon)
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
		Intent resultIntent = new Intent(context, SplashActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(SplashActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mBuilder.setPriority(Notification.PRIORITY_HIGH);
		voipNotif = mBuilder.build();
		voipNotif.flags = Notification.FLAG_ONGOING_EVENT;
		if(PreferencesUtil.isVoipNotifEnable()) {
			mNotificationManager.notify(NOTIF_ID_SIP, voipNotif);
		}
	}



	private static void showVoipNotificationIcs(Context context, int state) {
		Notification.Builder mBuilder = new Notification.Builder(context)
		.setSmallIcon(R.drawable.app_icon)
		.setContentTitle("My-Time");
		switch (state) {
		case STATE_REGISTERED:
			mBuilder.setContentText("En ligne");
			break;
		case STATE_UNREGISTERED:
			mBuilder.setContentText("Hors ligne");
			break;
		case STATE_REGISTERING:
			mBuilder.setContentText("Connexion en cours");
			break;
		}
		
		Intent resultIntent = new Intent(context, SplashActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(SplashActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.addAction(android.R.drawable.ic_menu_today, "Ouvrir", resultPendingIntent);
		

		resultIntent = new Intent(context, SettingsActivity.class);
		stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(SettingsActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.addAction(android.R.drawable.ic_menu_preferences, "Réglages", resultPendingIntent);

		mBuilder.setPriority(Notification.PRIORITY_HIGH);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		voipNotif = mBuilder.build();
		voipNotif.flags = Notification.FLAG_ONGOING_EVENT;
		if(PreferencesUtil.isVoipNotifEnable()) {
			mNotificationManager.notify(NOTIF_ID_SIP, voipNotif);
		}
	}



	public static void hideVoipNotifiaction(Context context) {
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(NOTIF_ID_SIP);
	}

	public static void showEventNotification(Context context, String message, int id) {
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

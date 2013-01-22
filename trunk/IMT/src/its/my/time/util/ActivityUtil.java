package its.my.time.util;

import its.my.time.R;
import its.my.time.SplashActivity;
import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.pages.editable.comptes.ComptesActivity;
import its.my.time.pages.editable.comptes.compte.CompteActivity;
import its.my.time.pages.editable.events.call.CallActivity;
import its.my.time.pages.editable.events.event.EventActivity;
import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.pages.editable.events.task.TaskActivity;
import its.my.time.pages.editable.profil.ProfilActivity;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

public class ActivityUtil {

	public static final String KEY_EXTRA_ID = "KEY_ID";
	public static final String KEY_EXTRA_ISO_TIME = "KEY_EXTRA_ISO_TIME";
	public static final String KEY_EXTRA_ALL_DAY = "KEY_EXTRA_ALL_DAY";

	public static final String ACTION_FINISH = "ACTION_FINISH";

	public static void logout(Context context) {
		PreferencesUtil.setCurrentUid(context, -1);
		final Intent i = new Intent(ACTION_FINISH);
		i.putExtra("FINISH", "ACTION.FINISH.LOGOUT");
		context.sendBroadcast(i);
		startSplashActivity(context);
	}

	public static void startSplashActivity(Context context) {
		final Intent intent = new Intent(context, SplashActivity.class);
		context.startActivity(intent);
	}

	public static void startProfilActivity(Context context) {
		final Intent intent = new Intent(context, ProfilActivity.class);
		context.startActivity(intent);
	}

	public static void startCalendarActivity(Context context) {
		final Intent intent = new Intent(context, CalendarActivity.class);
		context.startActivity(intent);
	}

	public static void startComptesActivity(Context context) {
		final Intent intent = new Intent(context, ComptesActivity.class);
		context.startActivity(intent);
		Log.d("ActivityUtil","Compte activity started!");
	}

	public static void startCompteActivity(Context context, long id) {
		final Intent intent = new Intent(context, CompteActivity.class);
		intent.putExtra(KEY_EXTRA_ID, id);
		context.startActivity(intent);
	}

	private static Intent getEventIntentFromType(Context context, int typeEvent) {
		Intent intent;
		switch (typeEvent) {
		case Types.Event.TASK:
			intent = new Intent(context, TaskActivity.class);
			break;
		case Types.Event.MEETING:
			intent = new Intent(context, MeetingActivity.class);
			break;
		case Types.Event.CALL:
			intent = new Intent(context, CallActivity.class);
			break;
		case Types.Event.BASE:
		default:
			intent = new Intent(context, EventActivity.class);
			break;
		}
		return intent;
	}

	public static void startEventActivity(Context context, int id, int typeEvent) {
		final Intent intent = getEventIntentFromType(context, typeEvent);
		intent.putExtra(KEY_EXTRA_ID, id);
		context.startActivity(intent);
	}

	public static void startEventActivity(final Context context,
			final Calendar calHeure, final boolean isAllDay) {
		final String[] labels = new String[] {
				context.getResources().getString(R.string.label_event_base),
				context.getResources().getString(R.string.label_event_meeting),
				context.getResources().getString(R.string.label_event_task),
				context.getResources().getString(R.string.label_event_call) };
		final Integer[] types = new Integer[] { Types.Event.BASE,
				Types.Event.MEETING, Types.Event.TASK,
				Types.Event.CALL };

		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Evénement à créer");
		builder.setItems(labels, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				startEventActivity(context, calHeure, types[item], isAllDay);
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	private static void startEventActivity(Context context, Calendar calHeure,
			int typeEvent, boolean isAllDay) {
		final Intent intent = getEventIntentFromType(context, typeEvent);
		intent.putExtra(KEY_EXTRA_ID, -1);
		intent.putExtra(KEY_EXTRA_ISO_TIME, DateUtil.getTimeInIso(calHeure));
		intent.putExtra(KEY_EXTRA_ALL_DAY, isAllDay);
		context.startActivity(intent);
	}
}

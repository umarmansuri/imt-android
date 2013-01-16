package its.my.time.util;

import its.my.time.R;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.pages.editable.compte.CompteActivity;
import its.my.time.pages.editable.events.call.CallActivity;
import its.my.time.pages.editable.events.event.EventActivity;
import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.pages.editable.events.task.TaskActivity;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ActivityUtil {

	public static final String KEY_EXTRA_ID = "KEY_ID";
	public static final String KEY_EXTRA_ISO_TIME = "KEY_EXTRA_ISO_TIME";
	
	public static void startCalendarActivity(Context context) {
		Intent intent = new Intent(context, CalendarActivity.class);
		context.startActivity(intent);
	}
	
	public static void startCompteActivity(Context context, int id) {
		Intent intent = new Intent(context, CompteActivity.class);
		intent.putExtra(KEY_EXTRA_ID, id);
		context.startActivity(intent);
	}

	private static Intent getEventIntentFromType(Context context, int typeEvent) {
		Intent intent;
		switch (typeEvent) {
		case EventBaseRepository.Types.TYPE_TASK:
			intent = new Intent(context, TaskActivity.class);
			break;
		case EventBaseRepository.Types.TYPE_MEETING:
			intent = new Intent(context, MeetingActivity.class);
			break;
		case EventBaseRepository.Types.TYPE_CALL:
			intent = new Intent(context, CallActivity.class);
			break;
		case EventBaseRepository.Types.TYPE_BASE:
		default:
			intent = new Intent(context, EventActivity.class);
			break;
		}
		return intent;
	}

	public static void startEventActivity(Context context, int id, int typeEvent) {
		Intent intent = getEventIntentFromType(context, typeEvent);
		intent.putExtra(KEY_EXTRA_ID, id);
		context.startActivity(intent);
	}

	private static void startEventActivity(Context context, Calendar calHeure, int typeEvent) {
		Intent intent = getEventIntentFromType(context, typeEvent);
		intent.putExtra(KEY_EXTRA_ISO_TIME, DateUtil.getTimeInIso(calHeure));
		context.startActivity(intent);
	}

	public static void startEventActivity(final Context context, final Calendar calHeure) {
		String[] labels = new String[]{
				context.getResources().getString(R.string.label_event_base),
				context.getResources().getString(R.string.label_event_meeting),
				context.getResources().getString(R.string.label_event_task),
				context.getResources().getString(R.string.label_event_call)
		};
		final Integer[] types = new Integer[]{
				EventBaseRepository.Types.TYPE_BASE,
				EventBaseRepository.Types.TYPE_MEETING,
				EventBaseRepository.Types.TYPE_TASK,
				EventBaseRepository.Types.TYPE_CALL
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Ev�nement � cr�er");
		builder.setItems(labels, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	startEventActivity(context, calHeure, types[item]);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
}

package its.my.time.util;

import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.pages.editable.compte.CompteActivity;
import its.my.time.pages.editable.events.event.EventActivity;
import its.my.time.pages.editable.events.meeting.MeetingActivity;
import its.my.time.pages.editable.events.task.TaskActivity;

import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

	public static void startEventActivity(Context context, int id, int typeEvent) {
		Intent intent;
		switch (typeEvent) {
		case EventBaseRepository.Types.TYPE_TASK:
			intent = new Intent(context, TaskActivity.class);
			break;
		case EventBaseRepository.Types.TYPE_MEETING:
			intent = new Intent(context, MeetingActivity.class);
			break;
		case EventBaseRepository.Types.TYPE_BASE:
		default:
			intent = new Intent(context, EventActivity.class);
			break;
		}
		intent.putExtra(KEY_EXTRA_ID, id);
		context.startActivity(intent);
	}

	public static void startEventActivity(Context context, Calendar calHeure) {
		Intent intent = new Intent(context, EventActivity.class);
		intent.putExtra(KEY_EXTRA_ISO_TIME, DateUtil.getTimeInIso(calHeure));
		context.startActivity(intent);
	}
}


package its.my.time.util;

import java.util.GregorianCalendar;

import its.my.time.pages.calendar.CalendarActivity;
import its.my.time.pages.editable.compte.CompteActivity;
import its.my.time.pages.editable.events.event.EventActivity;
import android.content.Context;
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

	public static void startEventActivity(Context context, int id) {
		Intent intent = new Intent(context, EventActivity.class);
		intent.putExtra(KEY_EXTRA_ID, id);
		context.startActivity(intent);
	}

	public static void startEventActivity(Context context, GregorianCalendar calHeure) {
		Intent intent = new Intent(context, EventActivity.class);
		intent.putExtra(KEY_EXTRA_ISO_TIME, DateUtil.getTimeInIso(calHeure));
		context.startActivity(intent);
	}
}


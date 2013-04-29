/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package its.my.time.receivers;

import its.my.time.Consts;
import its.my.time.R;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
//import android.util.Log;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		super(Consts.GCM_PROJECT_ID);
	}


	@Override
	protected void onMessage(Context context, Intent intent) {
		int id = Integer.parseInt(intent.getStringExtra("id"));
		generateNotification(context, "Nouvel évènement", id);
	}

	public static void generateNotification(Context context, String message, int id) {
		//TODO receuperer nouveau user, jouter dans WSManger.addUser();
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


	@Override protected void onError(Context arg0, String arg1) {}
	@Override protected void onRegistered(Context arg0, String arg1) {}
	@Override protected void onUnregistered(Context arg0, String arg1) {}

}

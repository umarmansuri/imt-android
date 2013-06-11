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
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.ws.WSGetBase.GetCallback;
import its.my.time.data.ws.events.EventBeanWS;
import its.my.time.data.ws.events.WSGetEvent;

import java.util.Calendar;

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
	protected void onMessage(final Context context, Intent intent) {

		int id = Integer.parseInt(intent.getStringExtra("id"));
		String type = intent.getStringExtra("type");
		EventBaseBean event = new EventBaseRepository(context).getByIdDistant(id);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		if(event != null && event.getId() >= 0) {
			if(event.getDateSync().before(cal)) {
				new WSGetEvent(context, id, null).execute();
			}
		} else {
			new WSGetEvent(context, id, null).execute();
		}
	}

	@Override protected void onError(Context arg0, String arg1) {}
	@Override protected void onRegistered(Context arg0, String arg1) {}
	@Override protected void onUnregistered(Context arg0, String arg1) {}

}

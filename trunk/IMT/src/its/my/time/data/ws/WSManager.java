package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
//import android.util.Log;

public class WSManager {

	private static LocationManager lm;

	private static LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//Log.i(TAG,"onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			//Log.i(TAG,"onProviderEnabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			//Log.i(TAG,"onProviderDisabled");
		}

		@Override
		public void onLocationChanged(Location location) {
			new SendLocationUpdate(location.getLatitude(), location.getLongitude()).execute();
		}
	};


	public static void init(Context context) {
		initGcm(context);
	}

	public static void initGcm(Context context) {
		String gcmId = null;
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		if (GCMRegistrar.isRegistered(context)) {
			gcmId = GCMRegistrar.getRegistrationId(context);
		}
		String regId = GCMRegistrar.getRegistrationId(context);
		if (regId.equals("")) {
			GCMRegistrar.register(context, Consts.GCM_PROJECT_ID);
			regId = GCMRegistrar.getRegistrationId(context);
			gcmId = GCMRegistrar.getRegistrationId(context);
		} else {
			gcmId = regId;
		}		
		updateGcmId(gcmId, null);
	}

	public interface Callback {
		public void done(Exception e);
	}
	public interface LoadEventInfoCallback {
		public void done(EventBaseBean event, Exception e);
	}

	public static void uploadPicture(Bitmap bitmap, int uid) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte [] ba = bao.toByteArray();
		String ba1=Base64.encodeToString(ba,0);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("image",ba1));
		nameValuePairs.add(new BasicNameValuePair("id",String.valueOf(uid)));
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Consts.URL_UPLOAD);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			Log.d("upImg", EntityUtils.toString(response.getEntity()));
		}catch(Exception e){
			Log.e("upImg", "Error in http connection "+e.toString());
		}
	}

	public static void updateGcmId(String gcmId, Callback callback) {
		new SendGcmUpdate(gcmId,callback).execute();
	}


	public static void login(String pseudo, String pass, Callback callback) {
		new SendLogin(pseudo, pass, callback).execute();
	}

	public static void subscribe(int uid, String pseudo, String nom2, String prenom, String pass, Calendar date, String sexe, Bitmap bitmap, Callback callback) {
		new SendSubscribe(uid, bitmap, nom2, prenom, pseudo, pass, date, sexe, callback).execute();
	}

	public static void logout(Callback callback) {
		new SendLogout(callback).execute();
	}

	public static void loadInfo(int uid, LoadEventInfoCallback callback) {
		new LoadInfo(uid, callback).execute();
	}
}

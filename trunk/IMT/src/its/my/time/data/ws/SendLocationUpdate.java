package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;
//import android.util.Log;


public class SendLocationUpdate extends AsyncTask<Void, Void, Void> {

	private double latitude;
	private double longitude;



	public SendLocationUpdate(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try
		{
			int uid = PreferencesUtil.readInteger(Consts.PREFS_UID, -1);
			if(uid > 0) {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(latitude)));
				nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(longitude)));
				nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(uid)));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.URL_UPDATE_LOCATION);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				EntityUtils.toString(response.getEntity());
			}
		}
		catch(Exception e)
		{
			Log.d("postData", "Error:  "+e.toString());
		}		
		return null;
	}
}

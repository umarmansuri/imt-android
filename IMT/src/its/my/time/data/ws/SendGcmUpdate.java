package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.data.ws.WSManager.Callback;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
//import android.util.Log;


public class SendGcmUpdate extends AsyncTask<Void, Void, Integer> {

	private String gcmId;
	private Callback callback;	

	
	public SendGcmUpdate(String gcmId, Callback callback) {
		super();
		this.gcmId = gcmId;
		this.callback = callback;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			int uid = PreferencesUtil.readInteger(Consts.PREFS_UID, -1);

			//Log.d("update", "Update " + uid + "/" + gcmId);
			if(uid > 0) {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(uid)));
				nameValuePairs.add(new BasicNameValuePair("gcm_id", gcmId));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.URL_UPDATE_GCM_ID);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				@SuppressWarnings("unused")
				String result = EntityUtils.toString(httpclient.execute(httppost).getEntity());
				return uid;
			}
		} catch(Exception e) {
			//Log.d("postData", "Error:  "+e.toString());
		}		
		return -1;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if(callback != null) {
			if(result > 0) {
				callback.done(null);
			} else {
				callback.done(new Exception());
			}
		}
	}
}


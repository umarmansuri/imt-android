package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.data.ws.WSManager.Callback;
import its.my.time.exceptions.LogoutException;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
//import android.util.Log;

public class SendLogout extends AsyncTask<Void, Void, Boolean> {

	private Callback callback;
	
	public SendLogout(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			long id =  PreferencesUtil.getCurrentUid();
			if(id > 0) {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(id)));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Consts.URL_LOGOUT);
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httpclient.execute(httppost).getEntity(); 
				PreferencesUtil.writeInteger(Consts.PREFS_UID, -1);
			}
			return true;
		} catch(Exception e) {
			//Log.d("postData", "Error:  "+e.toString());
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(callback != null) {
			if(result == true) {
				callback.done(null);
			} else {
				callback.done(new LogoutException());
			}
		}
	}

}



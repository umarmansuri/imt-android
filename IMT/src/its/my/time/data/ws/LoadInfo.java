package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.data.bdd.events.eventBase.EventBaseBean;
import its.my.time.data.ws.WSManager.LoadEventInfoCallback;

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

public class LoadInfo extends AsyncTask<Void, Void, Boolean> {

	private LoadEventInfoCallback callback;
	private int uid;
	private EventBaseBean event;

	public LoadInfo(int uid, LoadEventInfoCallback callback) {
		this.uid = uid;
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(uid)));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Consts.URL_LOAD_INFO);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String json  = EntityUtils.toString(httpclient.execute(httppost).getEntity());
			event = JSonUtil.getEventFromJson(json);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if(callback != null) {
			if(result == true) {
				callback.done(event,null);
			} else {
				callback.done(event, new Exception());
			}
		}
	}

}



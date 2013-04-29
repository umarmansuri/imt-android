package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.data.ws.WSManager.Callback;
import its.my.time.util.PreferencesUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.security.auth.login.LoginException;

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

public class SendLogin extends AsyncTask<Void, Void, Integer> {
	
	private String pseudo;
	private String pass;
	private Callback callback;

	public SendLogin(String pseudo, String pass, Callback callback) {
		super();
		this.pseudo = pseudo;
		this.pass = pass;
		this.callback = callback;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("pseudo", pseudo));
			nameValuePairs.add(new BasicNameValuePair("pass", pass));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Consts.URL_LOGIN);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			int uid = Integer.parseInt(EntityUtils.toString(response.getEntity()));
			if(uid > 0) {
				PreferencesUtil.writeInteger(Consts.PREFS_UID, uid);
			}
			return uid;
		} catch(Exception e) {
			Log.d("postData", "Error:  "+e.toString());
		}		
		return Consts.ERROR_UNKNOW;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if(callback != null) {
			if(result > 0) {
				callback.done(null);
			} else if(result == Consts.ERROR_LOGIN) {
				callback.done(new LoginException());
			} else {
				callback.done(new Exception());
			}
		}
	}
	
	public static String getDateInverse(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
	
}



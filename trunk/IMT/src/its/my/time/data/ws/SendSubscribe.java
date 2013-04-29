package its.my.time.data.ws;

import its.my.time.Consts;
import its.my.time.data.ws.WSManager.Callback;
import its.my.time.exceptions.PseudoAlreadyUsedException;
import its.my.time.util.PreferencesUtil;

import java.text.SimpleDateFormat;
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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
//import android.util.Log;

public class SendSubscribe extends AsyncTask<Void, Void, Integer> {

	private Bitmap bitmap;

	private int uid;
	private String nom;
	private String prenom;
	private String pseudo;
	private String pass;
	private String sexe;
	private Calendar date;
	private Callback callback;



	public SendSubscribe(int uid, Bitmap bitmap, String nom, String prenom,
			String pseudo, String pass, Calendar date, String sexe, Callback callback) {
		super();
		this.uid = uid;
		this.bitmap = bitmap;
		this.nom = nom;
		this.prenom = prenom;
		this.pseudo = pseudo;
		this.pass = pass;
		this.date = date;
		this.sexe = sexe;
		this.callback = callback;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(uid)));
			nameValuePairs.add(new BasicNameValuePair("pseudo", pseudo));
			nameValuePairs.add(new BasicNameValuePair("pass", pass));
			nameValuePairs.add(new BasicNameValuePair("nom", nom));
			nameValuePairs.add(new BasicNameValuePair("prenom", prenom));
			nameValuePairs.add(new BasicNameValuePair("date_naissance", getDateInverse(date)));
			nameValuePairs.add(new BasicNameValuePair("sexe", sexe));
			nameValuePairs.add(new BasicNameValuePair("gcm_id", ""));

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Consts.URL_SAVE_USER);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			String result = EntityUtils.toString(response.getEntity());
			Log.d("ws",result);
			int uid = Integer.parseInt(result);
			if(uid > 0) {
				PreferencesUtil.setCurrentUid(uid);
				if(bitmap != null) {
					WSManager.uploadPicture(bitmap, uid);
				}
			}
			return uid;
		} catch(Exception e) {
			//Log.d("postData", "Error:  "+e.toString());
		}		
		return Consts.ERROR_UNKNOW;
	}

	@Override
	protected void onPostExecute(Integer result) {
		if(callback != null) {
			if(result > 0) {
				callback.done(null);
			} else if(result == Consts.ERROR_SUBSCRIBE_PSEUDO) {
				callback.done(new PseudoAlreadyUsedException());
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



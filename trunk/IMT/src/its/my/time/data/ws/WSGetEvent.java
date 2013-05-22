package its.my.time.data.ws;

import its.my.time.util.PreferencesUtil;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;

public class WSGetEvent extends WSBase{

	public WSGetEvent(Activity context, Callback callBack) {
		super(context, callBack);
	}

	@Override
	public Exception run() {
		try {
			HttpClient client = createClient();
			URI website = new URI(URL_API_GET); 
			HttpGet request = new HttpGet();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			request.setURI(website);
			HttpResponse response = client.execute(request);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}

}

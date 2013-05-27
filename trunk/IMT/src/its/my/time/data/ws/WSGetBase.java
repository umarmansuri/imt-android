package its.my.time.data.ws;

import its.my.time.data.ws.WSBase;
import its.my.time.util.PreferencesUtil;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.app.Activity;

public abstract class WSGetBase<T> extends WSBase{

	private int id;

	public WSGetBase(Activity context, int id, GetCallback<T> callBack) {
		super(context, callBack);
		this.id = id;
	}

	@Override
	public Exception run() {
		try {
			HttpClient client = createClient();
			String urlStr = getUrl();
			URI website;
			if(urlStr.startsWith("/")) {
				urlStr = urlStr.substring(1);
			}
			if(!urlStr.endsWith("/")) {
				urlStr = urlStr + "/";
			}
			website = new URI(URL_BASE + urlStr + id + ".json");	

			HttpGet request = new HttpGet();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			request.setURI(website);
			HttpResponse response = client.execute(request);
			createObjectFromJson(EntityUtils.toString(response.getEntity()));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}

	public abstract String getUrl();
	public abstract T createObjectFromJson(String json);

	public interface GetCallback<T> extends Callback {
		public void done(Exception e);
		public void onGetObject(T object);
	}
}

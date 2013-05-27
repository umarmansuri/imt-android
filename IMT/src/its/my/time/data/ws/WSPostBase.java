package its.my.time.data.ws;

import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import android.app.Activity;

public abstract class WSPostBase<T> extends WSBase{

	private T object;

	public WSPostBase(Activity context, T object, PostCallback<T> callBack) {
		super(context, callBack);
		this.object = object;
	}

	public T getObject() {
		return object;
	}
	
	@Override
	public Exception run() {
		try {
			HttpClient client = createClient();
			URI website = new URI(getUrl()); 
			HttpPost request = new HttpPost();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs = intitialiseParams(nameValuePairs);
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			request.setURI(website);
			client.execute(request);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}


	public abstract String getUrl();
	public abstract T createObjectFromJson(String json);
	public abstract List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs);
	
	public interface PostCallback<T> extends Callback {
		public void done(Exception e);
		public void onGetObject(T object);
	}
}

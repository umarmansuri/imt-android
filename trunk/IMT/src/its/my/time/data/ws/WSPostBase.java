package its.my.time.data.ws;

import its.my.time.data.bdd.base.BaseBean;
import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public abstract class WSPostBase<T extends BaseBean> extends WSBase{

	private T object;

	public WSPostBase(Context context, T object, PostCallback<? extends BaseBean> callBack) {
		super(context, callBack);
		this.object = object;
	}

	public T getObject() {
		return object;
	}
	
	@Override
	protected Exception run() {
		try {
			HttpClient client = createClient();
			String urlStr = getUrl();
			URI website;
			if(urlStr.startsWith("/")) {
				urlStr = urlStr.substring(1);
			}
			if(urlStr.endsWith("/")) {
				urlStr = urlStr.substring(0, urlStr.length() - 1);
			}
			website = new URI(URL_BASE + urlStr + ".json");	
			
			HttpPost request = new HttpPost();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs = intitialiseParams(nameValuePairs);
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			request.setURI(website);
			HttpResponse response = client.execute(request);
			String result = EntityUtils.toString(response.getEntity());
			Log.d("WSPost",result);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}
	
	public abstract BaseRepository<T> getRepository();
	public abstract String getUrl();
	public abstract List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs);
	
	public interface PostCallback<T> extends Callback {
		public void done(Exception e);
		public void onGetObject(T object);
	}
}

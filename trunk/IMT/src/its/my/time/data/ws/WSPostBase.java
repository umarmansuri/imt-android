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
import org.apache.http.util.EntityUtils;

import android.app.Activity;

public abstract class WSPostBase<T extends BaseBean> extends WSBase{

	private T object;

	public WSPostBase(Activity context, T object, PostCallback<? extends BaseBean> callBack) {
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
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			request.setURI(website);
			HttpResponse response = client.execute(request);
			EntityUtils.toString(response.getEntity());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}


	public abstract String getIdParam();
	public abstract BaseRepository<T> getRepository();
	public abstract String getUrl();
	public abstract T createObjectFromJson(String json);
	public abstract List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs);
	
	public interface PostCallback<T> extends Callback {
		public void done(Exception e);
		public void onGetObject(T object);
	}
}

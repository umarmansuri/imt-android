package its.my.time.data.ws.events.participating;

import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.ws.Callback;
import its.my.time.data.ws.WSBase;
import its.my.time.util.PreferencesUtil;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class WSGetEventParticipating extends WSBase{
	@SuppressWarnings("unchecked")
	private int id;
	private GetCallback<EventBaseBean> callback;

	public WSGetEventParticipating(Activity context, int id, GetCallback<EventBaseBean> callBack) {
		super(context, callBack);
		this.id = id;
		this.callback = callBack;
	}

	public WSGetEventParticipating(Context context, int id, GetCallback<EventBaseBean> callBack) {
		super(context, callBack);
		this.id = id;
		this.callback = callBack;
	}

	@Override
	protected Exception run() {
		if(retreiveObject() != null){
			return null;
		} else {
			return null;
		}
	}
	
	

	public EventBaseBean retreiveObject() {
		try {
			HttpClient client = createClient();
			URI website = new URI(URL_BASE + "api/users/" + PreferencesUtil.getCurrentUid() + "/participants.json");	

			HttpGet request = new HttpGet();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			request.setURI(website);
			HttpResponse response = client.execute(request);
			String result = EntityUtils.toString(response.getEntity());
			Log.d("WS",result);
			ObjectMapper mapper = new ObjectMapper();
			EventBaseBean object = mapper.readValue(result, EventBaseBean.class);
			if(callback != null) {
				callback.onGetObject(object);
			}
			return object;
		} catch (Exception e) {
			return null;
		}
	}


	public interface GetCallback<T> extends Callback {
		public void done(Exception e);
		public void onGetObject(T object);
	}
}

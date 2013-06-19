package its.my.time.data.ws.events.participating;

import its.my.time.data.ws.Callback;
import its.my.time.data.ws.WSBase;
import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class WSGetEventParticipating extends WSBase{
	private int id;
	private GetCallback<List<Participating>> callback;

	public WSGetEventParticipating(Activity context, int id, GetCallback<List<Participating>> callBack) {
		super(context, callBack);
		this.id = id;
		this.callback = callBack;
	}

	public WSGetEventParticipating(Context context, int id, GetCallback<List<Participating>> callBack) {
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
	
	

	public List<Participating> retreiveObject() {
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
			List<Participating> myObjects = mapper.readValue(result, new TypeReference<List<Participating>>(){});

			if(callback != null) {
				callback.onGetObject(myObjects);
			}
			return myObjects;
		} catch (Exception e) {
			return null;
		}
	}


	public interface GetCallback<T> extends Callback {
		public void done(Exception e);
		public void onGetObject(T object);
	}
}

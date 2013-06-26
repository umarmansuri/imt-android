package its.my.time.data.ws.events.plugins.participant;

import its.my.time.data.ws.WSBase;
import its.my.time.data.ws.WSGetBase;
import its.my.time.data.ws.WSGetBase.GetCallback;
import its.my.time.util.ConnectionManager;
import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class WSGetCommentaireByEvent {

	private int id;
	private Context context;
	private WSGetBase.GetCallback<List<CommentBeanWS>> callBack;

	public WSGetCommentaireByEvent(Context context, int id, GetCallback<List<CommentBeanWS>> callBack) {

		this.id = id;
		this.context = context;
		this.callBack = callBack;
	}
	
	public List<CommentBeanWS> retreiveObject() {

		if(!ConnectionManager.isOnline(context)) {
			Toast.makeText(context, "Vous n'êtes pas connecté à internet.", Toast.LENGTH_SHORT).show();
			return null;
		}
		try {
			HttpClient client =WSBase.createClient();
			URI website = new URI(WSBase.URL_BASE + "api/events/" + id + "/comments.json");	

			HttpGet request = new HttpGet();
			String accessToken = PreferencesUtil.getCurrentAccessToken();
			request.setHeader("Authorization", "Bearer "+accessToken);
			request.setURI(website);
			HttpResponse response = client.execute(request);
			String result = EntityUtils.toString(response.getEntity());
			Log.d("WS",result);
			ObjectMapper mapper = new ObjectMapper();
			List<CommentBeanWS> myObjects = mapper.readValue(result, new TypeReference<List<CommentBeanWS>>(){});

			if(callBack != null) {
				callBack.onGetObject(myObjects);
			}
			return myObjects;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

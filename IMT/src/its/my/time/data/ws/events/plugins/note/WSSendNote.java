package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

public class WSSendNote extends WSPostBase<NoteBean>{

	public WSSendNote(Activity context, PostCallback<NoteBean> callBack) {
		super(context, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NoteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NameValuePair> intitialiseParams(
			List<NameValuePair> nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}

}

package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetNote extends WSGetBase<String>{

	public WSGetNote(Activity context, int id, GetCallback<String> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/notes/";
	}

}

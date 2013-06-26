package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.ws.WSGetBase;
import android.content.Context;

public class WSGetNote extends WSGetBase<String>{

	public WSGetNote(Context context, int id, GetCallback<String> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/notes/";
	}

}

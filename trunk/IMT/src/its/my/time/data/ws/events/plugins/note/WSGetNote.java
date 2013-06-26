package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.ws.WSGetBase;
import android.content.Context;

public class WSGetNote extends WSGetBase<NoteBeanWS>{

	public WSGetNote(Context context, int id, GetCallback<NoteBeanWS> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/notes/";
	}

}

package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetNote extends WSGetBase<NoteBean>{

	public WSGetNote(Activity context, int id, GetCallback<NoteBean> callBack) {
		super(context, id, callBack);
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

}

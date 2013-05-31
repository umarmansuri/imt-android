package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllNoteByEid extends WSGetBase<List<NoteBean>>{

	private int eid;

	public WSGetAllNoteByEid(Activity context, int id, int eid, GetCallback<List<NoteBean>> callBack) {
		super(context, id, callBack);
		this.eid = eid;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NoteBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

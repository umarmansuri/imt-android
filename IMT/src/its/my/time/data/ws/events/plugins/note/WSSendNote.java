package its.my.time.data.ws.events.plugins.note;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.bdd.events.plugins.note.NoteRepository;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class WSSendNote extends WSPostBase<NoteBean>{

	public WSSendNote(Context context, NoteBean note, PostCallback<NoteBean> callBack) {
		super(context, note, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/note";
	}

	@Override
	public BaseRepository<NoteBean> getRepository() {
		return new NoteRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams( List<NameValuePair> nameValuePairs) {
		EventBaseBean event = new EventBaseRepository(getContext()).getById(getObject().getEid());
        nameValuePairs.add(new BasicNameValuePair("event_id ", String.valueOf(event.getIdDistant())));
        nameValuePairs.add(new BasicNameValuePair("note_content", getObject().getHtml()));
		return nameValuePairs;
	}

}

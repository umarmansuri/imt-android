package its.my.time.data.ws.events.plugins.commentaires;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.Types;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

public class WSSendCommentaire extends WSPostBase<CommentBean>{

	public WSSendCommentaire(Context context, CommentBean coment, PostCallback<CommentBean> callBack) {
		super(context, coment, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/comment";
	}
	
	@Override
	public BaseRepository<CommentBean> getRepository() {
		return new CommentRepository(getContext());
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
		if(getObject().getId() <= 0) {
	        nameValuePairs.add(new BasicNameValuePair("comment_id ", "" + 0));
		} else {
	        nameValuePairs.add(new BasicNameValuePair("comment_id ", "" + getObject().getId()));
		}
		EventBaseBean event = new EventBaseRepository(getContext()).getById(getObject().getEid());
        nameValuePairs.add(new BasicNameValuePair("event_type", Types.Event.getLabelById(event.getTypeId())));
        nameValuePairs.add(new BasicNameValuePair("event_id", "" + event.getIdDistant()));
        nameValuePairs.add(new BasicNameValuePair("comment_body", getObject().getComment()));
		return nameValuePairs;
	}

}

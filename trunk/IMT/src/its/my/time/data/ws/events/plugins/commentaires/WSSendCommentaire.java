package its.my.time.data.ws.events.plugins.commentaires;

import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;

public class WSSendCommentaire extends WSPostBase<CommentBean>{

	public WSSendCommentaire(Activity context, CommentBean coment, PostCallback<CommentBean> callBack) {
		super(context, coment, callBack);
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentBean createObjectFromJson(String json) {
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

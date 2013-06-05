package its.my.time.data.ws.events.plugins.commentaires;

import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.ws.WSGetBase;
import android.app.Activity;

public class WSGetCommentaire extends WSGetBase<CommentBean>{

	public WSGetCommentaire(Activity context, int id, GetCallback<CommentBean> callBack) {
		super(context, id, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/commentaires/";
	}

}

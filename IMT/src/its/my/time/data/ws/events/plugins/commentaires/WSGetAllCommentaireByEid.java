package its.my.time.data.ws.events.plugins.commentaires;

import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.ws.WSGetBase;

import java.util.List;

import android.app.Activity;

public class WSGetAllCommentaireByEid extends WSGetBase<List<CommentBean>>{

	private int eid;

	public WSGetAllCommentaireByEid(Activity context, int id, int eid, GetCallback<List<CommentBean>> callBack) {
		super(context, id, callBack);
		this.eid = eid;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommentBean> createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}

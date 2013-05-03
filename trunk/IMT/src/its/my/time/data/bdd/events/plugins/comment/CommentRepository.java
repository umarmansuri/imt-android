package its.my.time.data.bdd.events.plugins.comment;

import its.my.time.data.bdd.events.plugins.PluginBaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class CommentRepository extends PluginBaseRepository<CommentBean> {

	public CommentRepository(Context context) {
		super(context, CommentBean.class);
	}

	@Override
	public String getTableName() {
		return "comment";
	}


	protected void objectAdded(CommentBean object) {
		for (OnObjectChangedListener<CommentBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(CommentBean object) {
		for (OnObjectChangedListener<CommentBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(CommentBean object) {
		for (OnObjectChangedListener<CommentBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}


	private static List<OnObjectChangedListener<CommentBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<CommentBean>>();

	public void addOnObjectChangedListener(OnObjectChangedListener<CommentBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<CommentBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
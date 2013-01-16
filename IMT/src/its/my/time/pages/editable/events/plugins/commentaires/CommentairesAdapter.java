package its.my.time.pages.editable.events.plugins.commentaires;

import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class CommentairesAdapter implements ListAdapter{

	private Context context;
	private List<CommentBean> comments;

	private int idEvent;

	public CommentairesAdapter(Context context, int id) {
		this.context = context;
		this.idEvent = id;
		loadNextEvents();
	}

	private void loadNextEvents() {
		if(comments == null) {
			comments = new ArrayList<CommentBean>();
		}
		comments = DatabaseUtil.Plugins.getCommentRepository(context).getAllByEid(idEvent);
	}

	@Override
	public int getCount() {
		if(comments != null) {
			return comments.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {return null;}

	@Override
	public long getItemId(int position) {return comments.get(position).getId();}

	@Override
	public int getItemViewType(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new CommentairesView(context, comments.get(position));
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		if(comments == null | comments.size() == 0) {return true;} else {return false;}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {}

	@Override
	public boolean areAllItemsEnabled() {return false;}

	@Override
	public boolean isEnabled(int position) {return false;}



}

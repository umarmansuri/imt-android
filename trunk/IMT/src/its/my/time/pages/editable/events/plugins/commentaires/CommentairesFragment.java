package its.my.time.pages.editable.events.plugins.commentaires;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CommentairesFragment extends BasePluginFragment {

	private int eventId;
	private Button mButtonSend;
	private EditText mTextCommentaire;
	private ListView mListComment;
	
	public CommentairesFragment(int l) {
		this.eventId = l;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_commentaires, null);
		mListComment = (ListView)mView.findViewById(R.id.event_comment_liste);
		mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId, false));
		
		mTextCommentaire = (EditText)mView.findViewById(R.id.event_comment_editComment); 
		
		mButtonSend = (Button)mView.findViewById(R.id.event_comment_save);
		mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO envoyer via ws
				CommentBean commentaire = new CommentBean();
				commentaire.setComment(mTextCommentaire.getText().toString());
				commentaire.setDate(Calendar.getInstance());
				commentaire.setEid(eventId);
				commentaire.setUid(PreferencesUtil.getCurrentUid(getActivity()));
				long res = new CommentRepository(getActivity()).insertComment(commentaire);
				if(res < 0) {
					Toast.makeText(getActivity(), "Votre commentaire n'a pu être envoyé.", Toast.LENGTH_SHORT).show();
				}
				mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId, false));
				mTextCommentaire.setText("");
			}
		});
		
		
		return mView;
	}

	@Override
	public void launchEdit() {
		mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId, true));	
	}

	@Override
	public void launchSave() {
		mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId, false));
	}

	@Override
	public void launchCancel() {
		mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId, false));
	}
	

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}
	
	private class CommentairesAdapter implements ListAdapter{

		private List<CommentBean> comments;
		private boolean isEditMode;
		
		public CommentairesAdapter(Context context, int id, boolean isEditMode) {
			this.isEditMode = isEditMode;
			loadNextEvents();
		}

		private void loadNextEvents() {
			if(comments == null) {
				comments = new ArrayList<CommentBean>();
			}
			comments = new CommentRepository(getActivity()).getAllByEid(eventId);
		}

		@Override
		public int getCount() {
			if(comments != null) {
				return comments.size();
			}
			return 0;
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			CommentairesView view = new CommentairesView(getActivity(), comments.get(position), isEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new CommentRepository(getActivity()).deletecomment(comments.get(position).getId());
					mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId, true));	
				}
			});
			return view;
		}

		@Override public int getViewTypeCount() {return 1;}
		@Override public boolean hasStableIds() {return true;}
		@Override public boolean isEmpty() {if(comments == null | comments.size() == 0) {return true;} else {return false;}}
		@Override public Object getItem(int position) {return null;}
		@Override public long getItemId(int position) {return comments.get(position).getId();}
		@Override public int getItemViewType(int position) {return 0;}
		@Override public void registerDataSetObserver(DataSetObserver observer) {	}
		@Override public void unregisterDataSetObserver(DataSetObserver observer) {}
		@Override public boolean areAllItemsEnabled() {return false;}
		@Override public boolean isEnabled(int position) {return false;}
	}
}

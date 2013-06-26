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

	private Button mButtonSend;
	private EditText mTextCommentaire;
	private ListView mListComment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		final RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_commentaires, null);
		this.mListComment = (ListView) mView.findViewById(R.id.event_comment_liste);
		this.mTextCommentaire = (EditText) mView.findViewById(R.id.event_comment_editComment);

		this.mButtonSend = (Button) mView.findViewById(R.id.event_comment_save);
		this.mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO envoyer via ws
				final CommentBean commentaire = new CommentBean();
				commentaire.setComment(CommentairesFragment.this.mTextCommentaire.getText().toString());
				commentaire.setEid(getParentActivity().getEvent().getId());
				commentaire.setUid(PreferencesUtil.getCurrentUid());
				final long res = new CommentRepository(getActivity()).insert(commentaire);
				if (res < 0) {
					Toast.makeText(getActivity(),
							"Votre commentaire n'a pu être envoyé.",
							Toast.LENGTH_SHORT).show();
				}
				CommentairesFragment.this.mListComment
						.setAdapter(new CommentairesAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
				CommentairesFragment.this.mTextCommentaire.setText("");
			}
		});
		refresh();
		
		return mView;
	}

	@Override
	public void refresh() {
		this.mListComment.setAdapter(new CommentairesAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
	}
	
	@Override
	public String getTitle() {
		return "Commentaires";
	}

	@Override
	public void launchEdit() {
		this.mListComment.setAdapter(new CommentairesAdapter(getActivity(),
				getParentActivity().getEvent().getId(), true));
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		this.mListComment.setAdapter(new CommentairesAdapter(getActivity(),
				getParentActivity().getEvent().getId(), false));
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		this.mListComment.setAdapter(new CommentairesAdapter(getActivity(),
				getParentActivity().getEvent().getId(), false));
		super.launchCancel();
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

	private class CommentairesAdapter implements ListAdapter {

		private List<CommentBean> comments;
		private final boolean isEditMode;

		public CommentairesAdapter(Context context, int id, boolean isEditMode) {
			this.isEditMode = isEditMode;
			loadNextEvents();
		}

		private void loadNextEvents() {
			if (this.comments == null) {
				this.comments = new ArrayList<CommentBean>();
			}
			this.comments = new CommentRepository(getActivity()).getAllByEid(getParentActivity().getEvent().getId());
		}

		@Override
		public int getCount() {
			if (this.comments != null) {
				return this.comments.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final CommentairesView view = new CommentairesView(getActivity(),
					this.comments.get(position), this.isEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new CommentRepository(getActivity())
							.delete(CommentairesAdapter.this.comments
									.get(position));
					CommentairesFragment.this.mListComment
							.setAdapter(new CommentairesAdapter(getActivity(),
									getParentActivity().getEvent().getId(), true));
				}
			});
			return view;
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
			if (this.comments == null | this.comments.size() == 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return this.comments.get(position).getId();
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}
}

package its.my.time.pages.editable.events.plugins.commentaires;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.PreferencesUtil;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class CommentairesFragment extends SherlockFragment {

	private int eventId;
	private Button mButtonSend;
	private EditText mTextCommentaire;
	private ListView mListComment;
	
	public CommentairesFragment(int eventId) {
		super();
		this.eventId = eventId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_commentaires, null);
		mListComment = (ListView)mView.findViewById(R.id.event_comment_liste);
		mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId));
		
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
				long res = DatabaseUtil.Plugins.getCommentRepository(getActivity()).insertComment(commentaire);
				if(res < 0) {
					Toast.makeText(getActivity(), "Votre commentaire n'a pu être envoyé.", Toast.LENGTH_SHORT).show();
				}
				mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId));
				mTextCommentaire.setText("");
			}
		});
		
		
		return mView;
	}
}

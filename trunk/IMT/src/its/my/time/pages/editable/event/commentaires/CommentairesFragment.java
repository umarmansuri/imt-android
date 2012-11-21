package its.my.time.pages.editable.event.commentaires;

import its.my.time.R;
import android.content.Intent;
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
		/*mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO envoyer via ws
				CommentBean commentaire = new CommentBean();
				commentaire.setComment(mTextCommentaire.getText().toString());
				commentaire.setDate(Calendar.getInstance());
				commentaire.setEid(eventId);
				commentaire.setUid(PreferencesUtil.getCurrentUid(getActivity()));
				long res = DatabaseUtil.getCommentRepository(getActivity()).insertComment(commentaire);
				if(res < 0) {
					Toast.makeText(getActivity(), "Votre commentaire n'a pu être envoyé.", Toast.LENGTH_SHORT).show();
				}
				mListComment.setAdapter(new CommentairesAdapter(getActivity(), eventId));
				mTextCommentaire.setText("");
			}
		});*/
		
		mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
			        intent.setType("*/*"); 
			        intent.addCategory(Intent.CATEGORY_OPENABLE);

			        try {
			            startActivityForResult(
			                    Intent.createChooser(intent, "Select a File to Upload"),
			                    0);
			        } catch (android.content.ActivityNotFoundException ex) {
			            // Potentially direct the user to the Market with a Dialog
			            Toast.makeText(getActivity(), "Please install a File Manager.", 
			                    Toast.LENGTH_SHORT).show();
			        }
			}
		});
		
		
		
		return mView;
	}
}

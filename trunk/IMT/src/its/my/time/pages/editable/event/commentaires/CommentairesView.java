package its.my.time.pages.editable.event.commentaires;

import its.my.time.R;
import its.my.time.data.bdd.comment.CommentBean;
import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CommentairesView extends FrameLayout{

	private CommentBean comment;
	
	public CommentairesView(Context context) {
		super(context);
	}
	
	public CommentairesView(Context context, CommentBean comment) {
		super(context);
		inflate(context, R.layout.activity_event_commentaires_little, this);
		setBackgroundColor(Color.WHITE);
		this.comment = comment;
		
		initialiseDetails();
	}

	private void initialiseDetails() {
		((TextView)findViewById(R.id.event_comment_date)).setText("Mardi 17 octobre à 17h30");
		((TextView)findViewById(R.id.event_comment_title)).setText(comment.getTitle());
		((TextView)findViewById(R.id.event_comment_comments)).setText(comment.getComment());
	}
}

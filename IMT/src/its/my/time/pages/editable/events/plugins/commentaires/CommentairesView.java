package its.my.time.pages.editable.events.plugins.commentaires;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.util.DateUtil;
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
		((TextView)findViewById(R.id.event_comment_date)).setText(DateUtil.getLongDateTime(comment.getDate()));
		((TextView)findViewById(R.id.event_comment_comments)).setText(comment.getComment());
	}
}

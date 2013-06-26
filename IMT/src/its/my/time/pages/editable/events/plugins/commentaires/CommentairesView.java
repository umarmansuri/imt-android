package its.my.time.pages.editable.events.plugins.commentaires;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import its.my.time.util.DateUtil;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class CommentairesView extends EditableLittleView {

	private final CommentBean comment;

	public CommentairesView(Context context, CommentBean comment,
			boolean isEditMode) {
		super(context, isEditMode);
		inflate(context, R.layout.activity_event_commentaires_little, this);
		setBackgroundColor(Color.WHITE);
		this.comment = comment;
		initialiseDetails();
	}

	private void initialiseDetails() {
		super.initialiseValues();
		((TextView) findViewById(R.id.event_comment_date)).setVisibility(View.GONE);
		((TextView) findViewById(R.id.event_comment_comments))
				.setText(this.comment.getComment());
	}
}

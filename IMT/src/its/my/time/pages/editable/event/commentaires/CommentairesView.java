package its.my.time.pages.editable.event.commentaires;

import its.my.time.R;
import its.my.time.data.bdd.coment.ComentBean;
import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CommentairesView extends FrameLayout{

	private ComentBean coment;
	
	public CommentairesView(Context context) {
		super(context);
	}
	
	public CommentairesView(Context context, ComentBean coment) {
		super(context);
		inflate(context, R.layout.activity_event_commentaires_little, this);
		setBackgroundColor(Color.WHITE);
		this.coment = coment;
		
		initialiseDetails();
	}

	private void initialiseDetails() {
		((TextView)findViewById(R.id.event_coment_date)).setText("Mardi 17 octobre à 17h30");
		((TextView)findViewById(R.id.event_coment_title)).setText(coment.getTitle());
		((TextView)findViewById(R.id.event_coment_coments)).setText(coment.getComent());
	}
}

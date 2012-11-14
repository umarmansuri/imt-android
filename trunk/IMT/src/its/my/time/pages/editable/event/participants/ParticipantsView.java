package its.my.time.pages.editable.event.participants;

import its.my.time.R;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ParticipantsView extends FrameLayout{

	private UtilisateurBean utilisateur;
	
	public ParticipantsView(Context context) {
		super(context);
	}
	
	public ParticipantsView(Context context, UtilisateurBean utilisateur) {
		super(context);
		inflate(context, R.layout.activity_event_participant_little, this);
		setBackgroundColor(Color.WHITE);
		this.utilisateur = utilisateur;
		
		initialiseDetails();
	}

	private void initialiseDetails() {
		((TextView)findViewById(R.id.event_participant_title)).setText(utilisateur.getNom() + " " + utilisateur.getPrenom());
		((TextView)findViewById(R.id.event_participant_details)).setText(utilisateur.getAdresse());
	}
}

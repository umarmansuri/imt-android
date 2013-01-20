package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class ParticipantsView extends EditableLittleView{

	private UtilisateurBean utilisateur;
	
	
	public ParticipantsView(Context context, UtilisateurBean utilisateur, boolean isInEditMode) {
		super(context, isInEditMode);
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

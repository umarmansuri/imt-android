package its.my.time;

import java.util.Calendar;
import java.util.GregorianCalendar;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.ws.comptes.WSGetAccount;
import its.my.time.data.ws.comptes.WSSendAccount;
import its.my.time.data.ws.events.WSGetEvent;
import its.my.time.data.ws.events.WSSendEvent;
import its.my.time.data.ws.events.plugins.commentaires.WSGetCommentaire;
import its.my.time.data.ws.events.plugins.commentaires.WSSendCommentaire;
import its.my.time.data.ws.events.plugins.note.WSGetNote;
import its.my.time.data.ws.events.plugins.note.WSSendNote;
import its.my.time.data.ws.events.plugins.odj.WSGetOdj;
import its.my.time.data.ws.events.plugins.odj.WSSendOdj;
import its.my.time.data.ws.events.plugins.participants.WSGetParticipant;
import its.my.time.data.ws.events.plugins.participants.WSSendParticipant;
import its.my.time.data.ws.events.plugins.pj.WSGetPj;
import its.my.time.data.ws.events.plugins.pj.WSSendPj;
import its.my.time.data.ws.user.WSGetUser;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.Types;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class TestWSActivity extends Activity implements OnClickListener, OnLongClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_ws);

		findViewById(R.id.buttonCompte).setOnClickListener(this);
		findViewById(R.id.buttonEvent).setOnClickListener(this);
		findViewById(R.id.buttonUser).setOnClickListener(this);
		findViewById(R.id.buttonParticipant).setOnClickListener(this);
		findViewById(R.id.buttonComment).setOnClickListener(this);
		findViewById(R.id.buttonOdj).setOnClickListener(this);
		findViewById(R.id.buttonPj).setOnClickListener(this);
		findViewById(R.id.buttonNote).setOnClickListener(this);

	
		findViewById(R.id.buttonCompte).setOnLongClickListener(this);
		findViewById(R.id.buttonEvent).setOnLongClickListener(this);
		findViewById(R.id.buttonUser).setOnLongClickListener(this);
		findViewById(R.id.buttonParticipant).setOnLongClickListener(this);
		findViewById(R.id.buttonComment).setOnLongClickListener(this);
		findViewById(R.id.buttonOdj).setOnLongClickListener(this);
		findViewById(R.id.buttonPj).setOnLongClickListener(this);
		findViewById(R.id.buttonNote).setOnLongClickListener(this);
}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.buttonCompte) {
			new WSGetAccount(this, 1, null).execute();
		} else if (id == R.id.buttonEvent) {
			new WSGetEvent(this, 1, null).execute();
		} else if (id == R.id.buttonUser) {
			new WSGetUser(this, 1, null).execute();
		} else if (id == R.id.buttonParticipant) {
			new WSGetParticipant(this, 1, null).execute();
		} else if (id == R.id.buttonComment) {
			new WSGetCommentaire(this, 1, null).execute();
		} else if (id == R.id.buttonNote) {
			new WSGetNote(this, 1, null).execute();
		} else if (id == R.id.buttonOdj) {
			new WSGetOdj(this, 1, null).execute();
		} else if (id == R.id.buttonPj) {
			new WSGetPj(this, 1, null).execute();
		}
	}
	@Override
	public boolean onLongClick(View v) {
		int id = v.getId();
		if (id == R.id.buttonCompte) {
			CompteBean compte = new CompteBean();
			compte.setId(1000);
			compte.setUid(1);
			compte.setType(Types.Comptes.GOOGLE.id);
			compte.setTitle("Compte depuis Android");
			new WSSendAccount(this, compte, null).execute();
		} else if (id == R.id.buttonEvent) {
			EventBaseBean event  = new EventBaseBean();
			event.setAllDay(false);
			event.setCid(1000);
			event.setDetails("Le détails de l'event depuis Android");
			Calendar calDeb = Calendar.getInstance();
			event.sethDeb(calDeb);
			Calendar calFin = Calendar.getInstance();
			calFin.add(Calendar.HOUR_OF_DAY, 3);
			event.sethFin(calFin);
			event.setId(1001);
			event.setTitle("Event depuis Android");
			new WSSendEvent(this, event, null).execute();
		} else if (id == R.id.buttonUser) {
			//new WSSendUser(this, 1, null).execute();
		} else if (id == R.id.buttonParticipant) {
			//new WSSendParticipant(this, 1, null).execute();
		} else if (id == R.id.buttonComment) {
			CommentBean comment = new CommentBean();
			comment.setComment("Un commentaire depuis Android");
			comment.setDate(Calendar.getInstance());
			comment.setEid(1000);
			comment.setId(1000);
			comment.setUid(1);
			new WSSendCommentaire(this, comment, null).execute();
		} else if (id == R.id.buttonNote) {
			NoteBean note = new NoteBean();
			new WSSendNote(this, note, null).execute();
		} else if (id == R.id.buttonOdj) {
			OdjBean odj = new OdjBean();
			new WSSendOdj(this, odj, null).execute();
		} else if (id == R.id.buttonPj) {
			PjBean pj = new PjBean();
			new WSSendPj(this, pj, null).execute();
		}
		return true;
	}
}

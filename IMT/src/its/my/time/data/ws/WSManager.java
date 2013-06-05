package its.my.time.data.ws;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.event.EventBaseBean;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.comment.CommentBean;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.bdd.events.plugins.note.NoteRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.data.bdd.events.plugins.participation.ParticipationBean;
import its.my.time.data.bdd.events.plugins.participation.ParticipationRepository;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.data.ws.WSPostBase.PostCallback;
import its.my.time.data.ws.comptes.WSSendAccount;
import its.my.time.data.ws.events.WSSendEvent;
import its.my.time.data.ws.events.plugins.commentaires.WSSendCommentaire;
import its.my.time.data.ws.events.plugins.note.WSSendNote;
import its.my.time.data.ws.events.plugins.odj.WSSendOdj;
import its.my.time.data.ws.events.plugins.participants.WSSendParticipant;
import its.my.time.data.ws.events.plugins.pj.WSSendPj;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.PreferencesUtil;

import java.util.List;

import android.app.Activity;
import android.util.Log;

public class WSManager {

	private static Activity context;
	private static long uid;
	private static int count = 0;

	public static void updateAllData(Activity context, final Callback callback) {

		WSManager.context = context;

		uid = PreferencesUtil.getCurrentUid();
		new Thread(new Runnable() {

			@Override
			public void run() {
				sendLocalUpdate(
						new PostCallback() {
							@Override
							public void done(Exception e) {
								count--;
								Log.d("WS","callback launch");
								if(count==0){
									Log.d("WS","retrieve distante");
									getDistanteUpdate(callback);	
								}
							}
							@Override public void onGetObject(Object object) {}
						});	
				
			}

		}).run();
	}

	public static void sendLocalUpdate(PostCallback callback) {
		sendUser(callback);
		sendAllAccount(callback);
		sendAllEvent(callback);
		sendAllComment(callback);
		sendAllNote(callback);
		sendAllOdj(callback);
		sendAllParticipant(callback);
		sendAllParticipation(callback);
		sendAllPj(callback);	
	}


	private static void sendUser(PostCallback callback) {
		count ++;
		new WSSendUser(
				context,
				new UtilisateurRepository(context).getById(uid), 
				callback).execute();
	}

	private static void sendAllAccount(PostCallback callback) {

		List<CompteBean> comptes = new CompteRepository(context).getAllUpdatable();
		for (CompteBean compte : comptes) {
			count ++;
			new WSSendAccount(context, compte, callback).execute();
		}
	}

	private static void sendAllEvent(PostCallback callback) {
		List<EventBaseBean> events = new EventBaseRepository(context).getAllUpdatable();
		for (EventBaseBean event : events) {
			count ++;
			new WSSendEvent(context, event, callback).execute();
		}
	}

	private static void sendAllComment(PostCallback callback) {
		List<CommentBean> comments = new CommentRepository(context).getAllUpdatable();
		for (CommentBean comment : comments) {
			count ++;
			new WSSendCommentaire(context, comment, callback).execute();
		}
	}

	private static void sendAllNote(PostCallback callback) {
		List<NoteBean> notes = new NoteRepository(context).getAllUpdatable();
		for (NoteBean note : notes) {
			count ++;
			new WSSendNote(context, note, callback).execute();
		}
	}

	private static void sendAllOdj(PostCallback callback) {
		List<OdjBean> odjs = new OdjRepository(context).getAllUpdatable();
		for (OdjBean odj : odjs) {
			count ++;
			new WSSendOdj(context, odj, callback).execute();
		}
	}

	private static void sendAllParticipant(PostCallback callback) {
		List<ParticipantBean> participants = new ParticipantRepository(context).getAllUpdatable();
		for (ParticipantBean participant : participants) {
			count ++;
			new WSSendParticipant(context, participant, callback).execute();
		}
	}

	private static void sendAllParticipation(PostCallback callback) {
		List<ParticipationBean> participations = new ParticipationRepository(context).getAllUpdatable();
		for (ParticipationBean participation : participations) {
			count ++;
			//TODO new WSSendParticipation(context, participation, callback).execute();
		}
	}

	private static void sendAllPj(PostCallback callback) {
		List<PjBean> pjs = new PjRepository(context).getAllUpdatable();
		for (PjBean pj : pjs) {
			count ++;
			new WSSendPj(context, pj, callback).execute();
		}
	}

	public static void getDistanteUpdate(Callback callback) {

	}

}
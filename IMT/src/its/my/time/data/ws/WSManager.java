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
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.data.ws.WSGetBase.GetCallback;
import its.my.time.data.ws.WSPostBase.PostCallback;
import its.my.time.data.ws.comptes.CompteBeanWS;
import its.my.time.data.ws.comptes.Event;
import its.my.time.data.ws.comptes.WSGetAccount;
import its.my.time.data.ws.comptes.WSSendAccount;
import its.my.time.data.ws.events.Attachmants;
import its.my.time.data.ws.events.EventBeanWS;
import its.my.time.data.ws.events.Participant;
import its.my.time.data.ws.events.WSGetEvent;
import its.my.time.data.ws.events.WSSendEvent;
import its.my.time.data.ws.events.plugins.commentaires.WSSendCommentaire;
import its.my.time.data.ws.events.plugins.note.WSSendNote;
import its.my.time.data.ws.events.plugins.odj.WSSendOdj;
import its.my.time.data.ws.events.plugins.participants.ParticipantBeanWS;
import its.my.time.data.ws.events.plugins.participants.WSGetParticipant;
import its.my.time.data.ws.events.plugins.participants.WSSendParticipant;
import its.my.time.data.ws.events.plugins.pj.WSGetPj;
import its.my.time.data.ws.events.plugins.pj.WSSendPj;
import its.my.time.data.ws.user.Account;
import its.my.time.data.ws.user.UtilisateurBeanWS;
import its.my.time.data.ws.user.WSGetUser;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;

import java.util.List;

import android.app.Activity;
import android.util.Log;


@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class WSManager {

	private static Activity context;
	private static long uid;
	private static int count = 0;
	private static UtilisateurRepository utilisateurRepo;
	private static CompteRepository compteRepo;
	private static EventBaseRepository eventRepo;
	private static CommentRepository commentRepo;
	private static PjRepository pjRepo;
	private static OdjRepository odjRepo;
	private static NoteRepository noteRepo;
	private static ParticipantRepository participantRepo;
	private static ParticipationRepository participationRepo;

	public static void updateAllData(Activity context, final Callback callback) {

		WSManager.context = context;
		utilisateurRepo = new UtilisateurRepository(context);
		compteRepo = new CompteRepository(context);
		eventRepo = new EventBaseRepository(context);
		commentRepo = new CommentRepository(context);
		pjRepo = new PjRepository(context);
		odjRepo = new OdjRepository(context);
		noteRepo = new NoteRepository(context);
		participantRepo = new ParticipantRepository(context);
		participationRepo = new ParticipationRepository(context);
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
									getDistanteUpdate(new GetCallback() {
										@Override public void onGetObject(Object object) {}
										@Override
										public void done(Exception e) {
											if(callback != null) {
												callback.done(e);
											}	
										}
									}
											);	
								}
							}
							@Override public void onGetObject(Object object) {}
						});	

			}

		}).start();
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
				utilisateurRepo.getById(uid), 
				callback).execute();
	}

	private static void sendAllAccount(PostCallback callback) {

		List<CompteBean> comptes = compteRepo.getAllUpdatable();
		for (CompteBean compte : comptes) {
			count ++;
			new WSSendAccount(context, compte, callback).execute();
		}
	}

	private static void sendAllEvent(PostCallback callback) {
		List<EventBaseBean> events = eventRepo.getAllUpdatable();
		for (EventBaseBean event : events) {
			count ++;
			new WSSendEvent(context, event, callback).execute();
		}
	}

	private static void sendAllComment(PostCallback callback) {
		List<CommentBean> comments = commentRepo.getAllUpdatable();
		for (CommentBean comment : comments) {
			count ++;
			new WSSendCommentaire(context, comment, callback).execute();
		}
	}

	private static void sendAllNote(PostCallback callback) {
		List<NoteBean> notes = noteRepo.getAllUpdatable();
		for (NoteBean note : notes) {
			count ++;
			new WSSendNote(context, note, callback).execute();
		}
	}

	private static void sendAllOdj(PostCallback callback) {
		List<OdjBean> odjs = odjRepo.getAllUpdatable();
		for (OdjBean odj : odjs) {
			count ++;
			new WSSendOdj(context, odj, callback).execute();
		}
	}

	private static void sendAllParticipant(PostCallback callback) {
		List<ParticipantBean> participants = participantRepo.getAllUpdatable();
		for (ParticipantBean participant : participants) {
			count ++;
			new WSSendParticipant(context, participant, callback).execute();
		}
	}

	private static void sendAllParticipation(PostCallback callback) {
		List<ParticipationBean> participations = participationRepo.getAllUpdatable();
		for (ParticipationBean participation : participations) {
			count ++;
			//TODO new WSSendParticipation(context, participation, callback).execute();
		}
	}

	private static void sendAllPj(PostCallback callback) {
		List<PjBean> pjs = pjRepo.getAllUpdatable();
		for (PjBean pj : pjs) {
			count ++;
			new WSSendPj(context, pj, callback).execute();
		}
	}

	public static void getDistanteUpdate(GetCallback callback) {
		new WSGetUser(context, 1, userCallback).execute();
	}

	private static GetCallback<UtilisateurBeanWS> userCallback = new GetCallback<UtilisateurBeanWS>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(UtilisateurBeanWS object) {
			UtilisateurBean bean = utilisateurRepo.getByIdDistant(object.getId());
			bean.setIdDistant(object.getId());
			bean.setNom(object.getUsername());
			bean.setMail(object.getEmail());
			if(bean.getId() == -1) {
				utilisateurRepo.insert(bean);
			} else {
				utilisateurRepo.update(bean);
			}
			for (Account account : object.getAccounts()) {
				new WSGetAccount(context, account.getId(), accountCallback).execute();
			}
		}

	};

	private static GetCallback<CompteBeanWS> accountCallback = new GetCallback<CompteBeanWS>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(CompteBeanWS object) {
			CompteBean bean = compteRepo.getByIdDistant(object.getId());
			bean.setIdDistant(object.getId());
			bean.setTitle(object.getTitle());
			bean.setColor(object.getColor());
			bean.setShowed(true);
			bean.setUid(uid);
			if(bean.getId() == -1) {
				compteRepo.insert(bean);
			} else {
				compteRepo.update(bean);
			}
			
			for (Event event : object.getEvents()) {
				new WSGetEvent(context, event.getId(), eventCallback).execute();
			}
		}

	};

	private static GetCallback<EventBeanWS> eventCallback = new GetCallback<EventBeanWS>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(EventBeanWS object) {
			EventBaseBean bean = eventRepo.getByIdDistant(object.getId());
			bean.setIdDistant(object.getId());
			bean.setTitle(object.getTitle());
			bean.sethDeb(DateUtil.getDateFromISO(object.getDate()));
			bean.sethFin(DateUtil.getDateFromISO(object.getDate_fin()));
			bean.setAllDay(object.getAll_day());
			bean.setCid(compteRepo.getByIdDistant(object.getAccounts().get(0).getId()).getId());
			Log.d("WS","Compte ID EVENT = " + bean.getCid());
			if(bean.getId() == -1) {
				eventRepo.insert(bean);
			} else {
				eventRepo.update(bean);
			}

			for (Participant participant : object.getParticipants()) {
				new WSGetParticipant(context, 1, participantCallback).execute();
			}	

			for (Attachmants pj : object.getAttachments()) {
				new WSGetPj(context, 1, pjCallback).execute();
			}	
		}
	};

	private static GetCallback<CommentBean> commentCallback = new GetCallback<CommentBean>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(CommentBean object) {}

	};

	private static GetCallback<PjBean> pjCallback = new GetCallback<PjBean>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(PjBean object) {}

	};

	private static GetCallback<OdjBean> odjCallback = new GetCallback<OdjBean>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(OdjBean object) {}

	};

	private static GetCallback<NoteBean> noteCallback = new GetCallback<NoteBean>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(NoteBean object) {}

	};

	private static GetCallback<ParticipantBeanWS> participantCallback = new GetCallback<ParticipantBeanWS>() {
		@Override public void done(Exception e) {}
		@Override public void onGetObject(ParticipantBeanWS object) {}

	};

}
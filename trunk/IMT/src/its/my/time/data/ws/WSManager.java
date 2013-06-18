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
import its.my.time.data.ws.comptes.CompteBeanWS;
import its.my.time.data.ws.comptes.Event;
import its.my.time.data.ws.comptes.WSGetAccount;
import its.my.time.data.ws.comptes.WSSendAccount;
import its.my.time.data.ws.events.Attachmants;
import its.my.time.data.ws.events.EventBeanWS;
import its.my.time.data.ws.events.Participant;
import its.my.time.data.ws.events.WSGetEvent;
import its.my.time.data.ws.events.WSSendEvent;
import its.my.time.data.ws.events.participating.WSGetEventParticipating;
import its.my.time.data.ws.events.plugins.commentaires.WSSendCommentaire;
import its.my.time.data.ws.events.plugins.note.WSSendNote;
import its.my.time.data.ws.events.plugins.odj.WSSendOdj;
import its.my.time.data.ws.events.plugins.participants.WSSendParticipant;
import its.my.time.data.ws.events.plugins.pj.WSSendPj;
import its.my.time.data.ws.user.Account;
import its.my.time.data.ws.user.UtilisateurBeanWS;
import its.my.time.data.ws.user.WSGetUser;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.util.Log;


@SuppressWarnings({ "unused" })
public class WSManager {

	private static Activity context;
	private static long uid;

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


		WSLogin.checkConnexion(context, new Callback() {

			@Override
			public void done(Exception e) {
				if(e == null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							sendLocalUpdate();	
							getDistanteUpdate();	
							PreferencesUtil.setLastUpdate(Calendar.getInstance());
							if(callback != null) {
								callback.done(null);
							}	
						}
					}).start();
				} else {
					if(callback != null) {
						callback.done(new Exception());
					}	
				}
			}
		});


	}



	private static void sendLocalUpdate() {
		new WSSendUser(context,utilisateurRepo.getById(uid),null).run();

		List<CompteBean> comptes = compteRepo.getAllUpdatable();
		for (CompteBean compte : comptes) {
			new WSSendAccount(context, compte, null).run();
		}


		List<EventBaseBean> events = eventRepo.getAllUpdatable();
		for (EventBaseBean event : events) {
			new WSSendEvent(context, event, null).run();
		}


		List<CommentBean> comments = commentRepo.getAllUpdatable();
		for (CommentBean comment : comments) {
			new WSSendCommentaire(context, comment, null).run();
		}


		List<NoteBean> notes = noteRepo.getAllUpdatable();
		for (NoteBean note : notes) {
			new WSSendNote(context, note, null).run();
		}


		List<OdjBean> odjs = odjRepo.getAllUpdatable();
		for (OdjBean odj : odjs) {
			new WSSendOdj(context, odj, null).run();
		}


		List<ParticipantBean> participants = participantRepo.getAllUpdatable();
		for (ParticipantBean participant : participants) {
			new WSSendParticipant(context, participant, null).run();
		}


		List<ParticipationBean> participations = participationRepo.getAllUpdatable();
		for (ParticipationBean participation : participations) {
			//TODO new WSSendParticipation(context, participation, callback).execute();
		}


		List<PjBean> pjs = pjRepo.getAllUpdatable();
		for (PjBean pj : pjs) {
			new WSSendPj(context, pj, null).run();
		}

	}

	private static void getDistanteUpdate() {

		UtilisateurBeanWS user = new WSGetUser(context, 1, null).retreiveObject();	

		UtilisateurBean bean = utilisateurRepo.getByIdDistant(user.getId());
		bean .setIdDistant(user.getId());
		bean.setNom(user.getUsername());
		bean.setMail(user.getEmail());
		if(bean.getId() == -1) {
			utilisateurRepo.insert(bean);
		} else {
			utilisateurRepo.update(bean);
		}

		List<Event> eventsWs = new ArrayList<Event>();
		for (Account account : user.getAccounts()) {
			CompteBeanWS object = new WSGetAccount(context, account.getId(), null).retreiveObject();
			if(object != null) {
				CompteBean comtpeBean = compteRepo.getByIdDistant(object.getId());
				comtpeBean.setIdDistant(object.getId());
				comtpeBean.setTitle(object.getTitle());
				comtpeBean.setColor(object.getColor());
				comtpeBean.setShowed(true);
				comtpeBean.setUid(uid);
				if(comtpeBean.getId() == -1) {
					compteRepo.insert(comtpeBean);
				} else {
					compteRepo.update(comtpeBean);
				}
				eventsWs.addAll(object.getEvents());
			}
		}


		new WSGetEventParticipating(context, 1, null).retreiveObject();
		
		List<Participant> participantsWs = new ArrayList<Participant>();
		List<Attachmants> attachementsWs = new ArrayList<Attachmants>();
		for (Event event : eventsWs) {
			EventBeanWS object = new WSGetEvent(context, event.getId(), null).retreiveObject();

			EventBaseBean eventBean = eventRepo.getByIdDistant(object.getId());
			eventBean.setIdDistant(object.getId());
			eventBean.setTitle(object.getTitle());
			eventBean.sethDeb(Calendar.getInstance());// DateUtil.getDateFromISO(object.getDate()));
			eventBean.sethFin(Calendar.getInstance());//DateUtil.getDateFromISO(object.getDate_fin()));
			eventBean.setAllDay(object.getAll_day());
			eventBean.setMine(true);
			eventBean.setCid(compteRepo.getByIdDistant(object.getAccounts().get(0).getId()).getId());
			Log.d("WS","Compte ID EVENT = " + eventBean.getCid());
			if(eventBean.getId() == -1) {
				eventRepo.insert(eventBean);
			} else {
				eventRepo.update(eventBean);
			}		
			participantsWs.addAll(object.getParticipants());
			attachementsWs.addAll(object.getAttachments());
		}


		for (Attachmants attachmants : attachementsWs) {

		}
		for (Participant participant : participantsWs) {

		}

		//new WSGetUser(context, 1, userCallback).execute();
	}


}
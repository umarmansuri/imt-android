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
import its.my.time.data.ws.events.participating.Participating;
import its.my.time.data.ws.events.participating.WSGetEventParticipating;
import its.my.time.data.ws.events.plugins.commentaires.WSSendCommentaire;
import its.my.time.data.ws.events.plugins.note.WSGetNote;
import its.my.time.data.ws.events.plugins.note.WSSendNote;
import its.my.time.data.ws.events.plugins.odj.WSSendOdj;
import its.my.time.data.ws.events.plugins.participants.WSSendParticipant;
import its.my.time.data.ws.events.plugins.pj.PjBeanWS;
import its.my.time.data.ws.events.plugins.pj.WSGetPj;
import its.my.time.data.ws.events.plugins.pj.WSSendPj;
import its.my.time.data.ws.user.Account;
import its.my.time.data.ws.user.UtilisateurBeanWS;
import its.my.time.data.ws.user.WSGetUser;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;


@SuppressWarnings({ "unused" })
public class WSManager {

	private static Context context;
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

	public static void updateAllData(Context context, final Callback callback) {
		init(context);
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



	public static void init(Context context) {
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
	}



	private static void sendLocalUpdate() {
		String gcmId = GCMManager.initGcm(context);
		new WSSendUser(context,utilisateurRepo.getById(uid),gcmId,null).run();

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
		int uid = (int)PreferencesUtil.getCurrentUid();
		UtilisateurBeanWS user = new WSGetUser(context, uid, null).retreiveObject();	

		UtilisateurBean bean = utilisateurRepo.getByIdDistant(user.getId());
		bean .setIdDistant(user.getId());
		bean.setNom(user.getUsername());
		bean.setMail(user.getEmail());
		bean.setDateSync(Calendar.getInstance());
		if(bean.getId() == -1) {
			utilisateurRepo.insert(bean);
		} else {
			utilisateurRepo.update(bean);
		}

		SparseArray<CompteBean> comptes = new SparseArray<CompteBean>();
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
				comtpeBean.setType(Types.Comptes.getIdFromLabel(object.getType()));
				comtpeBean.setDateSync(Calendar.getInstance());
				if(comtpeBean.getId() == -1) {
					comtpeBean.setId((int)compteRepo.insert(comtpeBean));
				} else {
					compteRepo.update(comtpeBean);
				}
				comptes.put(comtpeBean.getIdDistant(), comtpeBean);
				eventsWs.addAll(object.getEvents());
			}
		}


		List<Participating> participating =  new WSGetEventParticipating(context, uid, null).retreiveObject();
		for (Participating parti : participating) {
			retreiveEvent(parti.getIdEvent(), parti.getIdAccount(), false, comptes);
		}

		for (Event event : eventsWs) {
			retreiveEvent(event.getId(), -1, true, comptes);
		}
	}


	public static void retreiveEvent(int distanteId, int distanteAccountId, boolean isMine, SparseArray<CompteBean> comptes) {
		
		List<Participant> participantsWs = new ArrayList<Participant>();
		List<Attachmants> attachementsWs = new ArrayList<Attachmants>();

		
		EventBeanWS eventObject = new WSGetEvent(context, distanteId, null).retreiveObject();

		EventBaseBean eventBean = eventRepo.getByIdDistant(eventObject.getId());
		if(eventBean == null) {
			eventBean = new EventBaseBean();
		}
		eventBean.setIdDistant(eventObject.getId());
		eventBean.setTitle(eventObject.getTitle());
		eventBean.sethDeb(DateUtil.getDateFromISO(eventObject.getDate()));
		eventBean.sethFin(DateUtil.getDateFromISO(eventObject.getDate_fin()));
		eventBean.setDetails(eventObject.getContent());
		eventBean.setAllDay(eventObject.getAll_day());
		eventBean.setMine(false);
		eventBean.setTypeId(Types.Event.getIdByLabel(eventObject.getType()));
		eventBean.setDateSync(Calendar.getInstance());
		if(isMine) {
			eventBean.setCid(comptes.get(eventObject.getAccounts().get(0).getId()).getId());
		} else {
			eventBean.setCid(comptes.get(distanteAccountId).getId());
		}
		if(eventBean.getId() == -1) {
			eventBean.setId((int)eventRepo.insert(eventBean));
		} else {
			eventRepo.update(eventBean);
		}
		if(eventBean.getTypeId() == Types.Event.MEETING) {
			String noteVal = new WSGetNote(context, eventBean.getIdDistant(), null).retreiveObject();
			List<NoteBean> notes = noteRepo.getAllByEid(eventBean.getId());
			NoteBean note = null;
			if(notes == null || notes.size() == 0) {
				note = new NoteBean();
			} else {
				note = notes.get(0);
			}
			note.setEid(eventBean.getId());
			note.setHtml(noteVal);
			note.setDateSync(Calendar.getInstance());
			if(note.getId() == -1) {
				note.setId((int)noteRepo.insert(note));
			} else {
				noteRepo.update(note);
			}
		}		
		
		participantsWs.addAll(eventObject.getParticipants());
		attachementsWs.addAll(eventObject.getAttachments());


		for (Attachmants attachmants : attachementsWs) {
			PjBeanWS object = new WSGetPj(context, attachmants.getId(), null).retreiveObject();

			PjBean pjbean = pjRepo.getByIdDistant(object.getId());
			pjbean.setIdDistant(object.getId());
			pjbean.setBase64(object.getFile_base64());
			pjbean.setExtension(object.getExtension());
			pjbean.setMime(object.getMime());
			pjbean.setName(object.getName());
			pjbean.setDateSync(Calendar.getInstance());
			try {
				int idEvent = eventRepo.getByIdDistant(object.getEvent().getId()).getId();
				pjbean.setEid(idEvent);

				int idUser = utilisateurRepo.getByIdDistant(object.getUser().getId()).getId();
				pjbean.setUid(idUser);

			} catch (Exception e) {
				e.printStackTrace();
			}
			if(pjbean.getId() == -1) {
				pjbean.setId((int)pjRepo.insert(pjbean));
			} else {
				pjRepo.update(pjbean);
			}		
		}

		for (Participant participant : participantsWs) {

		}

		//new WSGetUser(context, 1, userCallback).execute();
	}


}
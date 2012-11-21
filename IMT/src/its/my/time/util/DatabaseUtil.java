package its.my.time.util;

import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.event.EventRepository;
import its.my.time.data.bdd.event.comment.CommentRepository;
import its.my.time.data.bdd.event.participant.ParticipantRepository;
import its.my.time.data.bdd.event.pj.PjRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import android.content.Context;


public class DatabaseUtil {
	
	private static EventRepository eventRepo;
	public static EventRepository getEventRepository(Context context) {
		if(eventRepo == null) {
			eventRepo = new EventRepository(context);
		}
		return eventRepo;
	}
	
	private static CompteRepository compteRepo;
	public static CompteRepository getCompteRepository(Context context) {
		if(compteRepo == null) {
			compteRepo = new CompteRepository(context);
		}
		return compteRepo;
	}
	
	private static CommentRepository commentRepo;
	public static CommentRepository getCommentRepository(Context context) {
		if(commentRepo == null) {
			commentRepo = new CommentRepository(context);
		}
		return commentRepo;
	}
	
	private static PjRepository pjRepo;
	public static PjRepository getPjRepository(Context context) {
		if(pjRepo == null) {
			pjRepo = new PjRepository(context);
		}
		return pjRepo;
	}

	private static ParticipantRepository participantRepo;
	public static ParticipantRepository getParticipantRepository(Context context) {
		if(participantRepo == null) {
			participantRepo = new ParticipantRepository(context);
		}
		return participantRepo;
	}
	
	private static UtilisateurRepository utilisateurRepository;
	public static UtilisateurRepository getUtilisateurRepository(Context context) {
		if(utilisateurRepository == null) {
			utilisateurRepository = new UtilisateurRepository(context);
		}
		return utilisateurRepository;
	}
}


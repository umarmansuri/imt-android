package its.my.time.util;

import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import android.content.Context;


public final class DatabaseUtil {
	
	public static final class Events {
		private static EventBaseRepository eventRepo;
		public static EventBaseRepository getEventRepository(Context context) {
			if(eventRepo == null) {
				eventRepo = new EventBaseRepository(context);
			}
			return eventRepo;
		}		
	}
	
	public static final class Plugins {

		private static CommentRepository commentRepo;
		private static PjRepository pjRepo;
		private static ParticipantRepository participantRepo;
		private static OdjRepository odjRepo;

		public static CommentRepository getCommentRepository(Context context) {
			if(commentRepo == null) {
				commentRepo = new CommentRepository(context);
			}
			return commentRepo;
		}

		public static PjRepository getPjRepository(Context context) {
			if(pjRepo == null) {
				pjRepo = new PjRepository(context);
			}
			return pjRepo;
		}

		public static ParticipantRepository getParticipantRepository(Context context) {
			if(participantRepo == null) {
				participantRepo = new ParticipantRepository(context);
			}
			return participantRepo;
		}
		
		public static OdjRepository getOdjRepository(Context context) {
			if(odjRepo == null) {
				odjRepo = new OdjRepository(context);
			}
			return odjRepo;
		}
	}
	
	private static CompteRepository compteRepo;
	public static CompteRepository getCompteRepository(Context context) {
		if(compteRepo == null) {
			compteRepo = new CompteRepository(context);
		}
		return compteRepo;
	}
	
	private static UtilisateurRepository utilisateurRepository;
	public static UtilisateurRepository getUtilisateurRepository(Context context) {
		if(utilisateurRepository == null) {
			utilisateurRepository = new UtilisateurRepository(context);
		}
		return utilisateurRepository;
	}
}


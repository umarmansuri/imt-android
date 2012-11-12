package its.my.time.util;

import its.my.time.data.bdd.comment.CommentRepository;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.event.EventRepository;
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
}


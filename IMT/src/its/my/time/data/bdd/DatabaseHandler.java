package its.my.time.data.bdd;

import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.events.details.call.CallDetailsRepository;
import its.my.time.data.bdd.events.details.meeting.MeetingDetailsRepository;
import its.my.time.data.bdd.events.event.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.note.NoteRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.data.bdd.events.plugins.participation.ParticipationRepository;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler {

	public static final String DATABASE_NAME = "IMT";

	public static final int DATABASE_VERSION = 1;

	protected final Context context;
	private final DatabaseHelper DBHelper;
	protected SQLiteDatabase db;

	public DatabaseHandler(Context context) {
		this.context = context;
		this.DBHelper = new DatabaseHelper(this.context);
	}

	public DatabaseHandler open() throws SQLException {
		this.db = this.DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		this.DBHelper.close();
	}

	private class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(new UtilisateurRepository(context).getCreateRequest());		
			db.execSQL(new CompteRepository(context).getCreateRequest());
			db.execSQL(new EventBaseRepository(context).getCreateRequest());		
			db.execSQL(new CommentRepository(context).getCreateRequest());
			db.execSQL(new ParticipantRepository(context).getCreateRequest());		
			db.execSQL(new PjRepository(context).getCreateRequest());
			db.execSQL(new OdjRepository(context).getCreateRequest());		
			db.execSQL(new NoteRepository(context).getCreateRequest());		
			db.execSQL(new ParticipationRepository(context).getCreateRequest());		

			db.execSQL(new MeetingDetailsRepository(context).getCreateRequest());		
			db.execSQL(new CallDetailsRepository(context).getCreateRequest());		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
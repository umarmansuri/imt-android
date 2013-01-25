package its.my.time.data.bdd;

import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.contacts.ContactRepository;
import its.my.time.data.bdd.contacts.ContactInfo.ContactInfoRepository;
import its.my.time.data.bdd.events.eventBase.EventBaseRepository;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
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

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(UtilisateurRepository.CREATE_TABLE);
			

			db.execSQL(ContactRepository.CREATE_TABLE);
			db.execSQL(ContactInfoRepository.CREATE_TABLE);
			
			db.execSQL(CompteRepository.CREATE_TABLE);
			db.execSQL(EventBaseRepository.CREATE_TABLE);
			db.execSQL(CommentRepository.CREATE_TABLE);
			db.execSQL(ParticipantRepository.CREATE_TABLE);
			db.execSQL(PjRepository.CREATE_TABLE);
			db.execSQL(OdjRepository.CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
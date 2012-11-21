package its.my.time.data.bdd;

import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.event.EventRepository;
import its.my.time.data.bdd.event.comment.CommentRepository;
import its.my.time.data.bdd.event.participant.ParticipantRepository;
import its.my.time.data.bdd.event.pj.PjRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler {

	public static final String DATABASE_NAME = "IMT";

	public static final int DATABASE_VERSION = 1;

	private final Context context; 
	private DatabaseHelper DBHelper;
	protected SQLiteDatabase db;

	public DatabaseHandler(Context context)
	{
		this.context = context;
		this.DBHelper = new DatabaseHelper(this.context);
	}

	public DatabaseHandler open() throws SQLException 
	{
		this.db = this.DBHelper.getWritableDatabase();
		return this;
	}

	public void close(){this.DBHelper.close();}

	private static class DatabaseHelper extends SQLiteOpenHelper 
	{
		DatabaseHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase db){
			Log.d("DbAdapter","passe dans onCreate!");
			db.execSQL(UtilisateurRepository.CREATE_TABLE);
			db.execSQL(CompteRepository.CREATE_TABLE);
			db.execSQL(EventRepository.CREATE_TABLE);   
			db.execSQL(CommentRepository.CREATE_TABLE); 
			db.execSQL(ParticipantRepository.CREATE_TABLE);    
			db.execSQL(PjRepository.CREATE_TABLE);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){}
	}
}
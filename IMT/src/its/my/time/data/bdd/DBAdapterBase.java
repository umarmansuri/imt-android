package its.my.time.data.bdd;

import its.my.time.data.bdd.comment.CommentRepository;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.event.EventRepository;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterBase {

	public static final String DATABASE_NAME = "IMT";

	public static final int DATABASE_VERSION = 1;

	private final Context context; 
	private DatabaseHelper DBHelper;
	protected SQLiteDatabase db;

	public DBAdapterBase(Context context)
	{
		this.context = context;
		this.DBHelper = new DatabaseHelper(this.context);
	}

	public DBAdapterBase open() throws SQLException 
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
			db.execSQL(CompteRepository.CREATE_TABLE);
			db.execSQL(EventRepository.CREATE_TABLE);   
			db.execSQL(CommentRepository.CREATE_TABLE);    
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){}
	}
}
package its.my.time.data.bdd;

import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.event.EventRepository;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapterBase {

	public static final String DATABASE_NAME = "IMT";

	public static final int DATABASE_VERSION = 2;

	public static final String CREATE_TABLE_COMPTE =  "create table " + CompteRepository.DATABASE_TABLE + "("
			+ CompteRepository.KEY_ID + " integer primary key autoincrement,"
			+ CompteRepository.KEY_TITLE + " text not null,"
			+ CompteRepository.KEY_COLOR + " text not null,"
			+ CompteRepository.KEY_TYPE + " integer not null);";


	public static final String CREATE_TABLE_EVENT =  "create table " + EventRepository.DATABASE_TABLE + "("
			+ EventRepository.KEY_ID + " integer primary key autoincrement,"
			+ EventRepository.KEY_TITLE + " text not null,"
			+ EventRepository.KEY_DETAILS+ " text,"
			+ EventRepository.KEY_HDEB+ " text not null,"
			+ EventRepository.KEY_HFIN+ " text not null,"
			+ EventRepository.KEY_CID+ " integer not null);";

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
			db.execSQL(CREATE_TABLE_COMPTE);
			db.execSQL(CREATE_TABLE_EVENT);    
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){}
	}
}
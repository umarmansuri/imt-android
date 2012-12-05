package its.my.time.data.bdd.utilisateur;

import its.my.time.data.bdd.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UtilisateurRepository extends DatabaseHandler{

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_NOM = 1;
	public static final int KEY_INDEX_PRENOM = 2;
	public static final int KEY_INDEX_TEL= 3;
	public static final int KEY_INDEX_MAIL = 4;
	public static final int KEY_INDEX_ADRESSE = 5;
	public static final int KEY_INDEX_CODE_POSTAL= 6;
	public static final int KEY_INDEX_VILLE = 7;
	public static final int KEY_INDEX_PAYS = 8;
	
	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_NOM = "KEY_NOM";
	public static final String KEY_PRENOM = "KEY_PRENOM";
	public static final String KEY_TEL = "KEY_TEL";
	public static final String KEY_MAIL = "KEY_MAIL";
	public static final String KEY_ADRESSE = "KEY_ADRESSE";
	public static final String KEY_CODE_POSTAL = "KEY_CODE_POSTAL";
	public static final String KEY_VILLE = "KEY_VILLE";
	public static final String KEY_PAYS = "KEY_PAYS";


	public static final String DATABASE_TABLE = "utilisateur";

	public static final String CREATE_TABLE =  "create table " + DATABASE_TABLE + "("
			+ KEY_ID + " integer primary key autoincrement,"
			+ KEY_NOM + " text,"
			+ KEY_PRENOM+ " text,"
			+ KEY_TEL + " text,"
			+ KEY_MAIL + " text,"
			+ KEY_ADRESSE + " text,"
			+ KEY_CODE_POSTAL + " integer,"
			+ KEY_VILLE + " text,"
			+ KEY_PAYS + " text);";

	private String[] allAttr = new String[]{
			KEY_ID,
			KEY_NOM,
			KEY_PRENOM,
			KEY_TEL,
			KEY_MAIL,
			KEY_ADRESSE,
			KEY_CODE_POSTAL,
			KEY_VILLE,
			KEY_PAYS};

	public UtilisateurRepository(Context context) {
		super(context);
	}

	public List<UtilisateurBean> convertCursorToListObject(Cursor c) {
		List<UtilisateurBean> liste = new ArrayList<UtilisateurBean>();
		if (c.getCount() == 0){return liste;}
		c.moveToFirst();
		do {
			UtilisateurBean utilisateur = convertCursorToObject(c);
			liste.add(utilisateur);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public UtilisateurBean convertCursorToObject(Cursor c) {
		UtilisateurBean utilisateur = new UtilisateurBean();
		utilisateur.setId(c.getInt(KEY_INDEX_ID));
		utilisateur.setNom(c.getString(KEY_INDEX_NOM));
		utilisateur.setPrenom(c.getString(KEY_INDEX_NOM));
		utilisateur.setTel(c.getString(KEY_INDEX_NOM));
		utilisateur.setMail(c.getString(KEY_INDEX_NOM));
		utilisateur.setAdresse(c.getString(KEY_INDEX_NOM));
		utilisateur.setCodePostal(c.getInt(KEY_INDEX_NOM));
		utilisateur.setVille(c.getString(KEY_INDEX_NOM));
		utilisateur.setPays(c.getString(KEY_INDEX_NOM));
		utilisateur.setNom(c.getString(KEY_INDEX_NOM));
		utilisateur.setNom(c.getString(KEY_INDEX_NOM));
		return utilisateur;
	}

	public UtilisateurBean convertCursorToOneObject(Cursor c) {
		if(c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		UtilisateurBean utilisateur = convertCursorToObject(c);
		c.close();
		return utilisateur;
	}

	public long insertUtilisateur(UtilisateurBean utilisateur){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOM, utilisateur.getId());
		initialValues.put(KEY_PRENOM, utilisateur.getId());
		initialValues.put(KEY_TEL, utilisateur.getId());
		initialValues.put(KEY_MAIL, utilisateur.getId());
		initialValues.put(KEY_ADRESSE, utilisateur.getId());
		initialValues.put(KEY_CODE_POSTAL, utilisateur.getId());
		initialValues.put(KEY_VILLE, utilisateur.getId());
		initialValues.put(KEY_PAYS, utilisateur.getId());
		open();
		long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deleteUtilisateur(long rowId) {
		open();
		boolean res = this.db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public List<UtilisateurBean> getAllUtilisateur() {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, null, null, null, null, null);
		List<UtilisateurBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public UtilisateurBean getById(long id) {
		open();
		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + "=?", new String[] { "" + id }, null, null, null);
		UtilisateurBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public List<UtilisateurBean> getAllByIds(List<Integer> ids) {
		open();

		String valuesWhere = "";
		for (Integer id : ids) {
			valuesWhere = id + ",";
		}
		if(valuesWhere != null && valuesWhere != "") {
			valuesWhere = valuesWhere.substring(0,valuesWhere.length() - 2);
		}

		Cursor c = this.db.query(DATABASE_TABLE,allAttr, KEY_ID + " IN (?)", new String[] {valuesWhere}, null, null, null);
		List<UtilisateurBean> res = convertCursorToListObject(c);
		close();
		return res;
	}
}

package its.my.time.data.bdd.utilisateur;

import its.my.time.data.bdd.DatabaseHandler;
import its.my.time.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UtilisateurRepository extends DatabaseHandler {

	public static final int KEY_INDEX_ID = 0;
	public static final int KEY_INDEX_NOM = 1;
	public static final int KEY_INDEX_PRENOM = 2;
	public static final int KEY_INDEX_PSEUDO = 3;
	public static final int KEY_INDEX_MDP = 4;
	public static final int KEY_INDEX_DATE_ANNIVERSAIRE = 5;
	public static final int KEY_INDEX_TEL = 6;
	public static final int KEY_INDEX_MAIL = 7;
	public static final int KEY_INDEX_ADRESSE = 8;
	public static final int KEY_INDEX_CODE_POSTAL = 9;
	public static final int KEY_INDEX_VILLE = 10;
	public static final int KEY_INDEX_PAYS = 11;

	public static final String KEY_ID = "KEY_ID";
	public static final String KEY_NOM = "KEY_NOM";
	public static final String KEY_PRENOM = "KEY_PRENOM";
	public static final String KEY_PSEUDO = "KEY_PSEUDO";
	public static final String KEY_MDP = "KEY_MDP";
	public static final String KEY_DATE_ANNIVERSAIRE = "KEY_DATE_ANNIVERSAIRE";
	public static final String KEY_TEL = "KEY_TEL";
	public static final String KEY_MAIL = "KEY_MAIL";
	public static final String KEY_ADRESSE = "KEY_ADRESSE";
	public static final String KEY_CODE_POSTAL = "KEY_CODE_POSTAL";
	public static final String KEY_VILLE = "KEY_VILLE";
	public static final String KEY_PAYS = "KEY_PAYS";

	public static final String DATABASE_TABLE = "utilisateur";

	public static final String CREATE_TABLE = "create table " + DATABASE_TABLE
			+ "(" + KEY_ID + " integer primary key autoincrement," + KEY_NOM
			+ " text," + KEY_PRENOM + " text," + KEY_PSEUDO + " text,"
			+ KEY_MDP + " text," + KEY_DATE_ANNIVERSAIRE + " text," + KEY_TEL
			+ " text," + KEY_MAIL + " text," + KEY_ADRESSE + " text,"
			+ KEY_CODE_POSTAL + " text," + KEY_VILLE + " text," + KEY_PAYS
			+ " text);";

	private final String[] allAttr = new String[] { KEY_ID, KEY_NOM,
			KEY_PRENOM, KEY_PSEUDO, KEY_MDP, KEY_DATE_ANNIVERSAIRE, KEY_TEL,
			KEY_MAIL, KEY_ADRESSE, KEY_CODE_POSTAL, KEY_VILLE, KEY_PAYS };

	public UtilisateurRepository(Context context) {
		super(context);
	}

	public List<UtilisateurBean> convertCursorToListObject(Cursor c) {
		final List<UtilisateurBean> liste = new ArrayList<UtilisateurBean>();
		if (c.getCount() == 0) {
			return liste;
		}
		c.moveToFirst();
		do {
			final UtilisateurBean utilisateur = convertCursorToObject(c);
			liste.add(utilisateur);
		} while (c.moveToNext());
		c.close();
		return liste;
	}

	public UtilisateurBean convertCursorToObject(Cursor c) {
		final UtilisateurBean utilisateur = new UtilisateurBean();
		utilisateur.setId(c.getInt(KEY_INDEX_ID));
		utilisateur.setNom(c.getString(KEY_INDEX_NOM));
		utilisateur.setPrenom(c.getString(KEY_INDEX_PRENOM));
		utilisateur.setPseudo(c.getString(KEY_INDEX_PSEUDO));
		utilisateur.setMdp(c.getString(KEY_INDEX_MDP));
		utilisateur.setDateAniv(DateUtil.getDateFromISO(c
				.getString(KEY_INDEX_DATE_ANNIVERSAIRE)));
		utilisateur.setTel(c.getString(KEY_INDEX_TEL));
		utilisateur.setMail(c.getString(KEY_INDEX_MAIL));
		utilisateur.setAdresse(c.getString(KEY_INDEX_ADRESSE));
		utilisateur.setCodePostal(c.getString(KEY_INDEX_CODE_POSTAL));
		utilisateur.setVille(c.getString(KEY_INDEX_VILLE));
		utilisateur.setPays(c.getString(KEY_INDEX_PAYS));
		return utilisateur;
	}

	public UtilisateurBean convertCursorToOneObject(Cursor c) {
		if (c.getCount() <= 0) {
			return null;
		}
		c.moveToFirst();
		final UtilisateurBean utilisateur = convertCursorToObject(c);
		c.close();
		return utilisateur;
	}

	public long insertUtilisateur(UtilisateurBean utilisateur) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOM, utilisateur.getNom());
		initialValues.put(KEY_PRENOM, utilisateur.getPrenom());
		initialValues.put(KEY_PSEUDO, utilisateur.getPseudo());
		initialValues.put(KEY_MDP, utilisateur.getMdp());
		initialValues.put(KEY_DATE_ANNIVERSAIRE,
				DateUtil.getTimeInIso(utilisateur.getDateAniv()));
		initialValues.put(KEY_TEL, utilisateur.getTel());
		initialValues.put(KEY_MAIL, utilisateur.getMail());
		initialValues.put(KEY_ADRESSE, utilisateur.getAdresse());
		initialValues.put(KEY_CODE_POSTAL, utilisateur.getCodePostal());
		initialValues.put(KEY_VILLE, utilisateur.getVille());
		initialValues.put(KEY_PAYS, utilisateur.getPays());
		open();
		final long res = this.db.insert(DATABASE_TABLE, null, initialValues);
		close();
		return res;
	}

	public boolean deleteUtilisateur(long rowId) {
		open();
		final boolean res = this.db.delete(DATABASE_TABLE,
				KEY_ID + "=" + rowId, null) > 0;
		close();
		return res;
	}

	public List<UtilisateurBean> getAllUtilisateur() {
		open();
		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, null,
				null, null, null, null);
		final List<UtilisateurBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public UtilisateurBean getById(long id) {
		open();
		final Cursor c = db.query(DATABASE_TABLE, allAttr, KEY_ID
				+ "=?", new String[] { "" + id }, null, null, null);
		final UtilisateurBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public UtilisateurBean getConnexion(String Pseudo, String Mdp) {
		open();
		final Cursor c = db.query(DATABASE_TABLE, allAttr, KEY_PSEUDO
				+ " = '" + Pseudo + "' AND " + KEY_MDP + " = '" + Mdp + "'",
				null, null, null, null);
		final UtilisateurBean res = convertCursorToOneObject(c);
		close();
		return res;
	}

	public List<UtilisateurBean> getAllByIds(List<Integer> ids) {
		open();

		String valuesWhere = "";
		for (final Integer id : ids) {
			valuesWhere = id + ",";
		}
		if (valuesWhere != null && valuesWhere != "") {
			valuesWhere = valuesWhere.substring(0, valuesWhere.length() - 2);
		}

		final Cursor c = this.db.query(DATABASE_TABLE, this.allAttr, KEY_ID
				+ " IN (?)", new String[] { valuesWhere }, null, null, null);
		final List<UtilisateurBean> res = convertCursorToListObject(c);
		close();
		return res;
	}

	public int update(UtilisateurBean utilisateur) {
		final ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOM, utilisateur.getNom());
		initialValues.put(KEY_PRENOM, utilisateur.getPrenom());
		initialValues.put(KEY_PSEUDO, utilisateur.getPseudo());
		initialValues.put(KEY_MDP, utilisateur.getMdp());
		initialValues.put(KEY_DATE_ANNIVERSAIRE,
				DateUtil.getTimeInIso(utilisateur.getDateAniv()));
		initialValues.put(KEY_TEL, utilisateur.getTel());
		initialValues.put(KEY_MAIL, utilisateur.getMail());
		initialValues.put(KEY_ADRESSE, utilisateur.getAdresse());
		initialValues.put(KEY_CODE_POSTAL, utilisateur.getCodePostal());
		initialValues.put(KEY_VILLE, utilisateur.getVille());
		initialValues.put(KEY_PAYS, utilisateur.getPays());
		open();
		final int nbRow = db.update(DATABASE_TABLE, initialValues, KEY_ID
				+ "=?", new String[] { "" + utilisateur.getId() });
		close();
		return nbRow;
	}
}

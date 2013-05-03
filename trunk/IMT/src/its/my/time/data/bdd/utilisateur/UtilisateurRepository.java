package its.my.time.data.bdd.utilisateur;

import its.my.time.data.bdd.base.BaseRepository;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class UtilisateurRepository extends BaseRepository<UtilisateurBean> {

	public UtilisateurRepository(Context context) {
		super(context, UtilisateurBean.class);
	}

	
	@Override
	public String getTableName() {
		return "utilisateur";
	}

	public UtilisateurBean getUser(String pseudo, String mdp) {
		open();
		Cursor c = this.db.query(getTableName(), getAllAttr(), 
				 "pseudo =? AND mdp=?", new String[] {pseudo, mdp}, null, null, null);
		UtilisateurBean res = convertCursorToOneObject(c);
		close();
		return res;
	}
	

	protected void objectAdded(UtilisateurBean object) {
		for (OnObjectChangedListener<UtilisateurBean> listener: onObjectChangedListeners) {
			listener.onObjectAdded(object);
		}
	}

	protected void objectUpdated(UtilisateurBean object) {
		for (OnObjectChangedListener<UtilisateurBean> listener: onObjectChangedListeners) {
			listener.onObjectUpdated(object);
		}
	}

	protected void objectDeleted(UtilisateurBean object) {
		for (OnObjectChangedListener<UtilisateurBean> listener: onObjectChangedListeners) {
			listener.onObjectDeleted(object);
		}
	}

	private static List<OnObjectChangedListener<UtilisateurBean>> onObjectChangedListeners = new ArrayList<OnObjectChangedListener<UtilisateurBean>>();

	
	public void addOnObjectChangedListener(OnObjectChangedListener<UtilisateurBean> listener) {
		onObjectChangedListeners.add(listener);
	}
	public void removeOnObjectChangedListener(OnObjectChangedListener<UtilisateurBean> listener) {
		onObjectChangedListeners.remove(listener);
	}
}
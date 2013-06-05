package its.my.time.data.ws.user;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.data.ws.WSPostBase;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendUser extends WSPostBase<UtilisateurBean>{

	public WSSendUser(Activity context, UtilisateurBean user, PostCallback<UtilisateurBean> callBack) {
		super(context, user, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/user";
	}
	

	@Override
	public UtilisateurBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BaseRepository<UtilisateurBean> getRepository() {
		return new UtilisateurRepository(getContext());
	}

	@Override
	public String getIdParam() {
		return null;
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {

		UtilisateurBean user = getObject();
		//TODO Récupérer GSM ID !!!!!
        //nameValuePairs.add(new BasicNameValuePair("gcm_id", GCMManager.));
        nameValuePairs.add(new BasicNameValuePair("name", user.getNom()));
        nameValuePairs.add(new BasicNameValuePair("firstname", user.getPrenom()));
		return nameValuePairs;
	}

}

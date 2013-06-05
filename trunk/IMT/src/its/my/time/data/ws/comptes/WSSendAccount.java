package its.my.time.data.ws.comptes;

import its.my.time.data.bdd.base.BaseRepository;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.ws.WSPostBase;
import its.my.time.util.DateUtil;
import its.my.time.util.Types;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

public class WSSendAccount extends WSPostBase<CompteBean>{

	public WSSendAccount(Activity context, CompteBean compte, PostCallback<CompteBean> callBack) {
		super(context, compte, callBack);
	}

	@Override
	public String getUrl() {
		return "/api/account";
	}

	@Override
	public CompteBean createObjectFromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseRepository<CompteBean> getRepository() {
		return new CompteRepository(getContext());
	}
	
	@Override
	public String getIdParam() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<NameValuePair> intitialiseParams(List<NameValuePair> nameValuePairs) {
		CompteBean compte = getObject();
		
		if(compte.getDateSync().equals(DateUtil.createCalendar())) {
	        nameValuePairs.add(new BasicNameValuePair("account_idAccount", "0"));
		} else {
	        nameValuePairs.add(new BasicNameValuePair("account_idAccount", String.valueOf(compte.getId())));
		}
        nameValuePairs.add(new BasicNameValuePair("account_type", Types.Comptes.geWsLabelFromId(compte.getType())));
        nameValuePairs.add(new BasicNameValuePair("account_title", compte.getTitle()));
        //TODO Attendre le retour de Yann sur les couleurs de Gay-Queer-PD-BoucheAPipe-Negre
        nameValuePairs.add(new BasicNameValuePair("account_color", "fc-event-orange"));
        nameValuePairs.add(new BasicNameValuePair("account_active", "1"));
		return nameValuePairs;
	}

}

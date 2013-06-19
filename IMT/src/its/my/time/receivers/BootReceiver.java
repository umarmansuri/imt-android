package its.my.time.receivers;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.data.ws.GCMManager;
import its.my.time.data.ws.user.WSSendUser;
import its.my.time.util.CallManager;
import its.my.time.util.NotifManager;
import its.my.time.util.PreferencesUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		PreferencesUtil.init(context);
		if(PreferencesUtil.getCurrentUid() > 0) {
			CallManager.initializeManager(context);
			String gcmId = GCMManager.initGcm(context);
			UtilisateurBean user = new UtilisateurRepository(context).getByIdDistant(PreferencesUtil.getCurrentUid());
			new WSSendUser(context, user, gcmId, null).execute();
		} else {
			NotifManager.showVoipNotifiaction(context, NotifManager.STATE_UNREGISTERED);
		}
	}

}

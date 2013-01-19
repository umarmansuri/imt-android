package its.my.time.pages.editable.profil;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.PreferencesUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.R;

public class ProfilActivity extends BaseActivity {

	private UtilisateurBean user;

	@Override
	protected void onCreate(Bundle savedInstance) {
			overridePendingTransition(R.anim.entry_in, R.anim.entry_out);
		setContentView(R.layout.activity_profile);

		Bundle bundle = getIntent().getExtras();
		if(PreferencesUtil.getCurrentUid(this) >= 0) {
			user = DatabaseUtil.getUtilisateurRepository(this).getById(PreferencesUtil.getCurrentUid(this)); 
		} 
		if(user == null) {
			//TODO Renvoyer sur la page Login 
			//user = new UtilisateurBean();
			Toast.makeText(this, "Utilisateur non identifé", Toast.LENGTH_LONG).show();
		}
	
		super.onCreate(bundle);
	}

	@Override
	protected void onViewCreated() {
	}
	
	@Override
	protected CharSequence getActionBarTitle() {
		return "Profil";
	}

	@Override
	protected void showEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void showSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void showCancel() {
		// TODO Auto-generated method stub
		
	}

}

package its.my.time.pages.editable.profil;

import java.util.ArrayList;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.menu.MenuGroupe;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.actionbarsherlock.R;

public class ProfilActivity extends BaseActivity {

	private UtilisateurBean user;

	@Override
	public void onCreate(Bundle savedInstance) {
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	protected void onMenuGroupSwitch(View v, int positionGroup,
			boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMenuItemSwitch(View v, int positionGroup,
			int positionObjet, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMenuChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ArrayList<MenuGroupe> onMainMenuCreated(
			ArrayList<MenuGroupe> menuGroupes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean onBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}

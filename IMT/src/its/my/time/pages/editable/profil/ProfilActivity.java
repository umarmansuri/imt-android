package its.my.time.pages.editable.profil;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.actionbarsherlock.R;

public class ProfilActivity extends BaseActivity {

	private UtilisateurBean user;
	private EditText nom;
	private EditText prenom;
	private EditText pseudo;
	private EditText mdp;
	private EditText mdp2;
	private EditText adresse;
	private EditText dateAniv;
	private EditText tel;
	private EditText codePostal;
	private EditText ville;

	@Override
	public void onCreate(Bundle savedInstance) {
		overridePendingTransition(R.anim.entry_in, R.anim.entry_out);
		setContentView(R.layout.activity_profile);

		Bundle bundle = getIntent().getExtras();
		if(PreferencesUtil.getCurrentUid(this) >= 0) {
			user = DatabaseUtil.getUtilisateurRepository(this).getById(PreferencesUtil.getCurrentUid(this)); 
		} 
		if(user == null) {
			user = new UtilisateurBean();
		}
	
		super.onCreate(bundle);
	}
	
	private void initialiseValues() {
		nom = (EditText)findViewById(R.id.activity_profil_nom_value);
		nom.setEnabled(false);
		nom.setText(user.getNom());

		prenom = (EditText)findViewById(R.id.activity_profil_prenom_value);
		prenom.setEnabled(false);
		prenom.setText(user.getPrenom());

		pseudo = (EditText)findViewById(R.id.activity_profil_identifiant_value);
		pseudo.setEnabled(false);
		pseudo.setText(user.getPseudo());
		
		mdp = (EditText)findViewById(R.id.activity_profil_password_value);
		mdp.setEnabled(false);
		mdp.setText(user.getMdp());
		
		mdp2 = (EditText)findViewById(R.id.activity_profil_password_again_value);
		mdp2.setEnabled(false);
		mdp2.setText(user.getMdp());
		
		adresse = (EditText)findViewById(R.id.activity_profil_adresse_value);
		adresse.setEnabled(false);
		adresse.setText(user.getAdresse());
		
		dateAniv = (EditText)findViewById(R.id.activity_profil_password_value);
		dateAniv.setEnabled(false);
		dateAniv.setText(DateUtil.getTimeInIso(user.getDateAniv()));
	}

	@Override
	protected void onViewCreated() {
		initialiseValues();
	}
	protected CharSequence getActionBarTitle() {
		if(PreferencesUtil.getCurrentUid(this) >= 0) {
			return "Inscription";	
		} else {
			return "Profil";
		}
	}
	
	private void changeState(boolean state) {
		nom.setEnabled(state);
		prenom.setEnabled(state);
		pseudo.setEnabled(state);
		mdp.setEnabled(state);
		mdp2.setEnabled(state);
		adresse.setEnabled(state);
		dateAniv.setEnabled(state);
		tel.setEnabled(state);
		codePostal.setEnabled(state);
		ville.setEnabled(state);
	}
	
	@Override
	protected void showEdit() {
		changeState(true);
	}

	@Override
	protected void showSave() {
		changeState(false);
		if(PreferencesUtil.getCurrentUid(this) >= 0) {
			new UtilisateurRepository(this).insertUtilisateur(user);	
		} else {
			new UtilisateurRepository(this).update(user);
		}
		finish();
	}

	@Override
	protected void showCancel() {
		changeState(false);
		finish();
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

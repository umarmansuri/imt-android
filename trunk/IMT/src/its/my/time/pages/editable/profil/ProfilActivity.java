package its.my.time.pages.editable.profil;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;
import its.my.time.view.date.DateButton;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ProfilActivity extends BaseActivity {

	private UtilisateurBean user;
	private EditText nom;
	private EditText prenom;
	private EditText pseudo;
	private EditText mdp;
	private EditText mdp2;
	private EditText adresse;
	private DateButton dateAniv;
	private EditText tel;
	private EditText mail;
	private TextView age;
	private EditText codePostal;
	private EditText ville;
	private boolean isNew;
	private Bundle state;

	
	@Override
	public void onCreate(Bundle savedInstance) {
		overridePendingTransition(R.anim.entry_in, R.anim.entry_out);

		final Bundle bundle = getIntent().getExtras();
		super.onCreate(bundle);

		setContentView(R.layout.activity_profile);

		if(savedInstance != null && savedInstance.containsKey(KEY_NOM)) {
			this.state = savedInstance;
		}
	}

	public boolean isUpdatable() {
		return false;
	}
	private String KEY_NOM = "KEY_NOM";
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/**
		 * enregistrer tes valeurs dans le bundle outState
		 * 
		 * out
		 * 
		 */
		outState.putString(KEY_NOM, nom.getText().toString());
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onViewCreated() {
		initialiseFields();
		if (PreferencesUtil.getCurrentUid() >= 0) {
			isNew = false;
			user = new UtilisateurRepository(this).getById(PreferencesUtil.getCurrentUid());
			if(state == null) {
				initialiseValuesFromBean();
			} else {
				initialiseValuesFromState();
			}
		}else{
			isNew = true;
			user = new UtilisateurBean();
			if(state == null) {
				initialiseValuesFromBean();
			} else {
				initialiseValuesFromState();
			}
			changeState(true);
		}
	}

	private void initialiseFields() {
		age = (TextView) findViewById(R.id.activity_profil_birthday_count);
		nom = (EditText) findViewById(R.id.activity_profil_nom_value);
		prenom = (EditText) findViewById(R.id.activity_profil_prenom_value);
		pseudo = (EditText) findViewById(R.id.activity_profil_identifiant_value);
		mdp = (EditText) findViewById(R.id.activity_profil_password_value);
		mdp2 = (EditText) findViewById(R.id.activity_profil_password_again_value);
		dateAniv = (DateButton) findViewById(R.id.activity_profil_birthday_value);
		tel = (EditText)findViewById(R.id.activity_profil_phone_value);
		mail = (EditText)findViewById(R.id.activity_profil_mail_value);
		adresse = (EditText) findViewById(R.id.activity_profil_adresse_value);
		codePostal = (EditText)findViewById(R.id.activity_profil_code_postal_value);
		ville = (EditText)findViewById(R.id.activity_profil_city_value);
	}

	private void initialiseValuesFromBean() {		
		if(!isNew)
		{
			age.setText(DateUtil.getNbYears(user.getDateAniv())+ " ans");
		}
	
		nom.setEnabled(false);
		nom.setText(user.getNom());
	
	
		prenom.setEnabled(false);
		prenom.setText(user.getPrenom());
	
		pseudo.setEnabled(false);
		pseudo.setText(user.getPseudo());
	
		mdp.setEnabled(false);
		mdp.setText(user.getMdp());
	
		mdp2.setEnabled(false);
		mdp2.setText(user.getMdp());
	
		dateAniv.setEnabled(false);
		dateAniv.setDate(user.getDateAniv());
	
		tel.setEnabled(false);
		tel.setText(user.getTel());
	
		mail.setEnabled(false);
		mail.setText(user.getMail());
	
		adresse.setEnabled(false);
		adresse.setText(user.getAdresse());
	
		codePostal.setEnabled(false);
		codePostal.setText(user.getCodePostal());
	
		ville.setEnabled(false);
		ville.setText(user.getVille());
	
	}

	private void initialiseValuesFromState() {
		nom.setText(state.getString(KEY_NOM));
	}

	@Override
	protected CharSequence getActionBarTitle() {
		if (!isNew) {
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
		mail.setEnabled(state);
		dateAniv.setEnabled(state);
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
		user.setNom(nom.getText().toString());
		user.setPrenom(prenom.getText().toString());
		user.setPseudo(pseudo.getText().toString());
		user.setMdp(mdp.getText().toString());
		user.setAdresse(adresse.getText().toString());
		user.setDateAniv(dateAniv.getDate());
		user.setTel(tel.getText().toString());
		user.setMail(mail.getText().toString());
		user.setCodePostal(codePostal.getText().toString());
		user.setVille(ville.getText().toString());
		user.setPays("France");

		if (!isNew) {
			new UtilisateurRepository(this).update(user);
		} else {
			long id_user = new UtilisateurRepository(this).insert(user);
			if (id_user >=0){
				CompteBean compte_default = new CompteBean();
				compte_default.setShowed(true);
				compte_default.setTitle("My Time");
				compte_default.setType(Types.Comptes.MYTIME.id);
				compte_default.setUid(id_user);
				long id_compte = new CompteRepository(this).insert(compte_default);
				if(id_compte == -1) {Toast.makeText(this, "Erreur Création Compte My Time", Toast.LENGTH_LONG).show();}
			}else {Toast.makeText(this, "Erreur Création Utilisateur", Toast.LENGTH_LONG).show();}
		}
		finish();
	}

	@Override
	protected void showCancel() {
		changeState(false);
		finish();
	}


}

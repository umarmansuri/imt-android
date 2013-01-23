package its.my.time.pages.editable.profil;

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

import com.actionbarsherlock.R;

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
	
	@Override
	public void onCreate(Bundle savedInstance) {
		overridePendingTransition(R.anim.entry_in, R.anim.entry_out);
		
		final Bundle bundle = getIntent().getExtras();
		super.onCreate(bundle);
		
		setContentView(R.layout.activity_profile);
	}

	private void initialiseValues() {		
		if(!isNew)
		{
			age = (TextView) findViewById(R.id.activity_profil_birthday_count);
			age.setText(DateUtil.getNbYears(user.getDateAniv())+ " ans");
		}

		nom = (EditText) findViewById(R.id.activity_profil_nom_value);
		nom.setEnabled(false);
		nom.setText(user.getNom());

		prenom = (EditText) findViewById(R.id.activity_profil_prenom_value);
		prenom.setEnabled(false);
		prenom.setText(user.getPrenom());

		pseudo = (EditText) findViewById(R.id.activity_profil_identifiant_value);
		pseudo.setEnabled(false);
		pseudo.setText(user.getPseudo());

		mdp = (EditText) findViewById(R.id.activity_profil_password_value);
		mdp.setEnabled(false);
		mdp.setText(user.getMdp());

		mdp2 = (EditText) findViewById(R.id.activity_profil_password_again_value);
		mdp2.setEnabled(false);
		mdp2.setText(user.getMdp());

		dateAniv = (DateButton) findViewById(R.id.activity_profil_birthday_value);
		dateAniv.setEnabled(false);
		dateAniv.setDate(user.getDateAniv());
		
		tel = (EditText)findViewById(R.id.activity_profil_phone_value);
		tel.setEnabled(false);
		tel.setText(user.getTel());
		
		mail = (EditText)findViewById(R.id.activity_profil_mail_value);
		mail.setEnabled(false);
		mail.setText(user.getMail());
		
		adresse = (EditText) findViewById(R.id.activity_profil_adresse_value);
		adresse.setEnabled(false);
		adresse.setText(user.getAdresse());
	
		codePostal = (EditText)findViewById(R.id.activity_profil_code_postal_value);
		codePostal.setEnabled(false);
		codePostal.setText(user.getCodePostal());

		ville = (EditText)findViewById(R.id.activity_profil_city_value);
		ville.setEnabled(false);
		ville.setText(user.getVille());
		
	}

	@Override
	protected void onViewCreated() {
		if (PreferencesUtil.getCurrentUid(this) >= 0) {
			isNew = false;
			user = new UtilisateurRepository(this).getById(PreferencesUtil
					.getCurrentUid(this));
			initialiseValues();
		}else{
			isNew = true;
			user = new UtilisateurBean();
			initialiseValues();
			changeState(true);
		}
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
			long id_user = new UtilisateurRepository(this).insertUtilisateur(user);
			if (id_user >=0){
			CompteBean compte_default = new CompteBean();
			compte_default.setShowed(true);
			compte_default.setTitle("My Time");
			compte_default.setType(Types.Comptes.MYTIME.id);
			compte_default.setUid(id_user);
			long id_compte = new CompteRepository(this).insertCompte(compte_default);
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

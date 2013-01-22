package its.my.time.pages.editable.profil;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import android.os.Bundle;
import android.widget.EditText;

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

		final Bundle bundle = getIntent().getExtras();
		if (PreferencesUtil.getCurrentUid(this) >= 0) {
			this.user = new UtilisateurRepository(this).getById(PreferencesUtil
					.getCurrentUid(this));
		}
		if (this.user == null) {
			this.user = new UtilisateurBean();
		}

		super.onCreate(bundle);
	}

	private void initialiseValues() {
		this.nom = (EditText) findViewById(R.id.activity_profil_nom_value);
		this.nom.setEnabled(false);
		this.nom.setText(this.user.getNom());

		this.prenom = (EditText) findViewById(R.id.activity_profil_prenom_value);
		this.prenom.setEnabled(false);
		this.prenom.setText(this.user.getPrenom());

		this.pseudo = (EditText) findViewById(R.id.activity_profil_identifiant_value);
		this.pseudo.setEnabled(false);
		this.pseudo.setText(this.user.getPseudo());

		this.mdp = (EditText) findViewById(R.id.activity_profil_password_value);
		this.mdp.setEnabled(false);
		this.mdp.setText(this.user.getMdp());

		this.mdp2 = (EditText) findViewById(R.id.activity_profil_password_again_value);
		this.mdp2.setEnabled(false);
		this.mdp2.setText(this.user.getMdp());

		this.adresse = (EditText) findViewById(R.id.activity_profil_adresse_value);
		this.adresse.setEnabled(false);
		this.adresse.setText(this.user.getAdresse());

		this.dateAniv = (EditText) findViewById(R.id.activity_profil_password_value);
		this.dateAniv.setEnabled(false);
		this.dateAniv.setText(DateUtil.getTimeInIso(this.user.getDateAniv()));
	}

	@Override
	protected void onViewCreated() {
		initialiseValues();
	}

	@Override
	protected CharSequence getActionBarTitle() {
		if (PreferencesUtil.getCurrentUid(this) >= 0) {
			return "Inscription";
		} else {
			return "Profil";
		}
	}

	private void changeState(boolean state) {
		this.nom.setEnabled(state);
		this.prenom.setEnabled(state);
		this.pseudo.setEnabled(state);
		this.mdp.setEnabled(state);
		this.mdp2.setEnabled(state);
		this.adresse.setEnabled(state);
		this.dateAniv.setEnabled(state);
		this.tel.setEnabled(state);
		this.codePostal.setEnabled(state);
		this.ville.setEnabled(state);
	}

	@Override
	protected void showEdit() {
		changeState(true);
	}

	@Override
	protected void showSave() {
		changeState(false);
		if (PreferencesUtil.getCurrentUid(this) >= 0) {
			new UtilisateurRepository(this).insertUtilisateur(this.user);
		} else {
			new UtilisateurRepository(this).update(this.user);
		}
		finish();
	}

	@Override
	protected void showCancel() {
		changeState(false);
		finish();
	}
}

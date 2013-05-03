package its.my.time.pages.editable.contacts.contact;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.ActivityUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;
import its.my.time.util.Types.Comptes.Compte;

import java.util.Collection;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ContactActivity extends BaseActivity implements OnClickListener, TextWatcher, OnColorChangedListener {

	private CompteBean compte;

	private boolean isNew = false;
	private EditText mFieldTitleEdit;
	private Spinner mFieldTypeSpinner;
	private ImageButton mFieldColorButton;

	private int color;

	@Override
	public void onCreate(Bundle savedInstance) {

		final Bundle bundle = getIntent().getExtras();
		if (bundle.getLong(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			this.compte = new CompteRepository(this).getById(bundle.getLong(ActivityUtil.KEY_EXTRA_ID));
			color = compte.getColor();
		} else {
			this.compte = new CompteBean();
			color = Color.parseColor("#70b2cd");
			this.isNew = true;
		}

		super.onCreate(bundle);

		setContentView(R.layout.activity_compte);
	}

	@Override
	protected void onViewCreated() {
		initialiseValues();
		if(isNew) {
			launchEdit();
		}
	}

	private void initialiseValues() {
		this.mFieldTitleEdit = (EditText) findViewById(R.id.activity_compte_title);
		this.mFieldTitleEdit.setEnabled(false);
		this.mFieldTitleEdit.addTextChangedListener(this);
		this.mFieldTitleEdit.setText(this.compte.getTitle());
		updateTitle();

		this.mFieldTypeSpinner = (Spinner) findViewById(R.id.activity_compte_type);
		this.mFieldTypeSpinner.setEnabled(false);
		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if(compte.getType() == Types.Comptes.MYTIME.id) {
			adapter.add(Types.Comptes.MYTIME.label);
			mFieldTypeSpinner.setAdapter(adapter);
			//mFieldTypeSpinner.setSelection(0);
		} else {
			Collection<Compte> comptes = Types.Comptes.getAllCompteEditable();
			int selected = 0;
			int i = 0;
			for (Compte compte: comptes) {
				adapter.add(compte.label);
				if(compte.id == this.compte.getType()) {
					selected = i;
				}
				i++;
			}
			mFieldTypeSpinner.setAdapter(adapter);
			mFieldTypeSpinner.setSelection(selected);
		}
		this.mFieldColorButton = (ImageButton) findViewById(R.id.activity_compte_color);
		this.mFieldColorButton.setEnabled(false);
		this.mFieldColorButton.setOnClickListener(this);
		updateColorButton();

		if (this.isNew) {
			showEdit();
		} else {

		}
	}

	private void updateColorButton() {
		final ColorDrawable dr = new ColorDrawable(color);
		this.mFieldColorButton.setImageDrawable(dr);
	}

	private void updateTitle() {
		String title;
		if (this.mFieldTitleEdit.getText().toString() != "") {
			title = this.mFieldTitleEdit.getText().toString();
		} else {
			title = "Nouveau compte";
		}
		getSupportActionBar().setTitle(title);
	}

	private void changeState(boolean state) {
		this.mFieldTitleEdit.setEnabled(state);
		this.mFieldColorButton.setEnabled(state);
		if(state == false || compte.getType() != Types.Comptes.MYTIME.id) {
			this.mFieldTypeSpinner.setEnabled(state);
		}
	}

	@Override
	protected CharSequence getActionBarTitle() {
		if (this.isNew) {
			return "Nouveau compte";
		} else {
			return this.compte.getTitle();
		}
	}

	@Override
	protected void showEdit() {
		changeState(true);
	}

	@Override
	protected void showSave() {
		changeState(false);
		compte.setType(Types.Comptes.getIdFromLabel((String)mFieldTypeSpinner.getSelectedItem()));
		compte.setTitle(mFieldTitleEdit.getText().toString());
		compte.setUid(PreferencesUtil.getCurrentUid());
		compte.setColor(color);
		if (this.isNew) {
			new CompteRepository(this).insert(this.compte);
			isNew = false;
		} else {
			new CompteRepository(this).update(this.compte);
		}
	}

	@Override
	protected void showCancel() {
		changeState(false);
		if(isNew) {
			finish();
		} else {
			initialiseValues();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == this.mFieldColorButton) {
			if(color == 0) {
				color = compte.getColor();
			}
			final ColorPickerDialog dialog = new ColorPickerDialog(this,color);
			dialog.show();
			dialog.setOnColorChangedListener(this);
		}
	}

	@Override public void afterTextChanged(Editable v) {updateTitle();}
	@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
	@Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onColorChanged(int color) {
		this.color = color;
		updateColorButton();
	}
}

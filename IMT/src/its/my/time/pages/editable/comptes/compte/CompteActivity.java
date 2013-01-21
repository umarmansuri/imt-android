package its.my.time.pages.editable.comptes.compte;

import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.pages.editable.BaseActivity;
import its.my.time.util.ActivityUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.actionbarsherlock.R;

public class CompteActivity extends BaseActivity implements
		OnItemSelectedListener, OnClickListener, TextWatcher,
		OnColorChangedListener {

	private CompteBean compte;

	private boolean isNew = false;

	private EditText mFieldTitleEdit;

	private Spinner mFieldTypeSpinner;

	private ImageButton mFieldColorButton;

	@Override
	public void onCreate(Bundle savedInstance) {

		final Bundle bundle = getIntent().getExtras();
		if (bundle.getLong(ActivityUtil.KEY_EXTRA_ID) >= 0) {
			this.compte = new CompteRepository(this).getById(bundle
					.getLong(ActivityUtil.KEY_EXTRA_ID));
		} else {
			this.compte = new CompteBean();
			this.compte.setColor(Color.parseColor("#70b2cd"));
			this.compte.setUid(PreferencesUtil.getCurrentUid(this));
			this.isNew = true;
		}

		super.onCreate(bundle);

		setContentView(R.layout.activity_compte);
	}

	@Override
	protected void onViewCreated() {
		initialiseValues();
	}

	private void initialiseValues() {
		this.mFieldTitleEdit = (EditText) findViewById(R.id.activity_compte_title);
		this.mFieldTitleEdit.setEnabled(false);
		this.mFieldTitleEdit.addTextChangedListener(this);
		this.mFieldTitleEdit.setText(this.compte.getTitle());
		updateTitle();

		this.mFieldTypeSpinner = (Spinner) findViewById(R.id.activity_compte_type);
		this.mFieldTypeSpinner.setEnabled(false);
		this.mFieldTypeSpinner.setOnItemSelectedListener(this);
		final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("Google");
		adapter.add("ERP 1");
		adapter.add("CRM");
		this.mFieldTypeSpinner.setAdapter(adapter);
		this.mFieldTypeSpinner.setSelection(this.compte.getType());

		this.mFieldColorButton = (ImageButton) findViewById(R.id.activity_compte_color);
		this.mFieldColorButton.setEnabled(false);
		this.mFieldColorButton.setOnClickListener(this);
		updateColorButton();

		if (this.isNew) {
			launchEditMode();
		} else {

		}
	}

	private void updateColorButton() {
		final ColorDrawable dr = new ColorDrawable(this.compte.getColor());
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
		this.compte.setTitle(title);
	}

	private void changeState(boolean state) {
		this.mFieldTitleEdit.setEnabled(state);
		this.mFieldTypeSpinner.setEnabled(state);
		this.mFieldColorButton.setEnabled(state);
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
		if (this.isNew) {
			new CompteRepository(this).insertCompte(this.compte);
		} else {
			new CompteRepository(this).update(this.compte);
		}
		finish();
	}

	@Override
	protected void showCancel() {
		changeState(false);
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v == this.mFieldColorButton) {
			final ColorPickerDialog dialog = new ColorPickerDialog(this,
					this.compte.getColor());
			dialog.show();
			dialog.setOnColorChangedListener(this);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long id) {
		this.compte.setType(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void afterTextChanged(Editable v) {
		updateTitle();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onColorChanged(int color) {
		this.compte.setColor(color);
		updateColorButton();
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

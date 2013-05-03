package its.my.time.pages.editable.contacts;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.data.bdd.compte.CompteRepository;
import its.my.time.pages.MyTimeActivity;
import its.my.time.util.ActivityUtil;
import its.my.time.util.PreferencesUtil;
import its.my.time.util.Types;
import its.my.time.view.menu.MenuGroupe;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fonts.mooncake.MooncakeIcone;
import com.mobeta.android.dslv.DragSortListView;

public class ContactsActivity extends MyTimeActivity {

	private DragSortListView mMainListe;
	private List<CompteBean> contacts;
	private ContactsAdapter adapter;
	private MenuGroupe menuAjouter;
	private MenuGroupe menuSupprimer;
	private MenuGroupe menuReglages;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste);

		this.mMainListe = (DragSortListView) findViewById(R.id.activity_list_main_list);
	}

	@Override
	protected void onResume() {
		this.contacts = new CompteRepository(this).getAllByUid(PreferencesUtil.getCurrentUid());

		this.adapter = new ContactsAdapter(this.contacts);
		this.mMainListe.setAdapter(this.adapter);
		this.mMainListe.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,int position, long id) {
				ActivityUtil.startCompteActivity(ContactsActivity.this, id);
			}
		});
		super.onResume();
	}

	@Override
	protected void initialiseActionBar() {
		super.initialiseActionBar();
		getSupportActionBar().setTitle("Contacts");

	}


	@Override
	protected ArrayList<MenuGroupe> onCreateMenu(ArrayList<MenuGroupe> menuGroupes) {
		menuAjouter = new MenuGroupe("Ajouter", MooncakeIcone.icon_plus,false);
		menuGroupes.add(menuAjouter);
		menuSupprimer = new MenuGroupe("Supprimer",MooncakeIcone.icon_minus_sign, false);
		menuGroupes.add(menuSupprimer);
		menuReglages = new MenuGroupe("Réglages", MooncakeIcone.icon_settings,false);
		menuGroupes.add(menuReglages);
		return menuGroupes;
	}

	@Override
	protected void onMenuGroupClick(ExpandableListView parent,MenuGroupe group, long id) {
		if(group == menuAjouter){
			ActivityUtil.startContactActivity(this, -1);
		} else if(group == menuSupprimer){
			this.adapter.setDraggable(true);
			this.mMainListe.setAdapter(this.adapter);
		} else if(group == menuReglages){
			Toast.makeText(this, "Réglages...", Toast.LENGTH_SHORT).show();
		}
		super.onMenuGroupClick(parent, group, id);
	}

	@Override
	protected boolean onBackButtonPressed() {
		finish();
		return true;
	}

	public void remove(final int which) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Suppression");
		builder.setMessage("Vous allez définitivement supprimer le contact '" + this.contacts.get(which).getTitle() + "'.\n\nContinuer?");
		builder.setPositiveButton("Supprimer",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int index) {
				new CompteRepository(ContactsActivity.this).delete(ContactsActivity.this.contacts.get(which));
				contacts.remove(which);
				mMainListe.setAdapter(adapter);
			}
		});
		builder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onResume();
			}
		});
		builder.show();
	}

	private class ContactsAdapter implements ListAdapter {

		private boolean removable;

		public ContactsAdapter(List<CompteBean> comptes) {
			if (comptes == null) {
				comptes = new ArrayList<CompteBean>();
			}
		}

		public void setDraggable(boolean draggable) {
			this.removable = draggable;
		}

		@Override
		public int getCount() {
			return contacts.size();
		}

		@Override
		public CompteBean getItem(int position) {
			return contacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return contacts.get(position).getId();
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final View v = getLayoutInflater().inflate(R.layout.activity_comptes_compte_little, null);
			((TextView) v.findViewById(R.id.activity_comptes_compte_title)).setText(getItem(position).getTitle());

			final GradientDrawable dr = new GradientDrawable();
			dr.setColor(getItem(position).getColor());
			v.findViewById(R.id.activity_comptes_compte_color).setBackgroundDrawable(dr);
			final MooncakeIcone icone = (MooncakeIcone) v.findViewById(R.id.delete);
			icone.setIconeRes(MooncakeIcone.icon_minus_sign);
			if (this.removable && contacts.get(position).getType() != Types.Comptes.MYTIME.id) {
				icone.setVisibility(View.VISIBLE);
				icone.setEnabled(true);
				icone.setOnClickListener(this.onRemoveClickListener);
			} else {
				icone.setVisibility(View.INVISIBLE);
				icone.setEnabled(false);
			}
			return v;
		}

		private final OnClickListener onRemoveClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int index = ContactsActivity.this.mMainListe
						.getPositionForView((ViewGroup) v.getParent());
				remove(index);
			}
		};

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isEmpty() {
			return contacts.size() == 0;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}
	}
}
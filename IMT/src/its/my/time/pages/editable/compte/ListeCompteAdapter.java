package its.my.time.pages.editable.compte;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.PreferencesUtil;

import java.util.Collection;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

public class ListeCompteAdapter implements ListAdapter {

	private Collection<CompteBean> listeCompte;
	private Context context;

	public ListeCompteAdapter(Context context) {
		this.context = context;
		//TODO
		listeCompte = DatabaseUtil.getCompteRepository(context).getAllCompteByUid(PreferencesUtil.getCurrentUid(context));
	}

	public int getCount() {
		return listeCompte.size() + 1;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public int getItemViewType(int position) {
		return IGNORE_ITEM_VIEW_TYPE;
	}

	public View getView(int position, View convertView, final ViewGroup parent) {
		if(position < listeCompte.size()) {
			return new CompteLittleView(parent.getContext(), position);
		} else {
			RelativeLayout rl = new RelativeLayout(parent.getContext());
			rl.setBackgroundResource(R.drawable.border_backgrnd_enable);
			rl.setLayoutParams(new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 60));

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			ImageButton buttonMore = new ImageButton(parent.getContext());
			buttonMore.setImageResource(android.R.drawable.ic_menu_add);
			buttonMore.setBackgroundColor(Color.TRANSPARENT);
			rl.addView(buttonMore, params);
			buttonMore.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(context, CompteActivity.class);
					context.startActivity(intent);
				}
			});
			return rl;
		}
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isEmpty() {
		if(listeCompte.size() == 0) {
			return true;
		}
		return false;
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	public boolean areAllItemsEnabled() {
		return true;
	}

	public boolean isEnabled(int position) {
		return true;
	}
}

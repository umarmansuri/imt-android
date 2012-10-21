package its.my.time.pages.editable.event.commentaires;

import its.my.time.data.bdd.coment.ComentBean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class CommentairesAdapter implements ListAdapter{

	private static final int NB_COMENT_LOADED = 10;

	private Context context;
	private List<ComentBean> coments;
	private int indexComent;

	public CommentairesAdapter(Context context) {
		this.context = context;
		indexComent = 0;
		loadNextEvents();
	}

	private void loadNextEvents() {
		if(coments == null) {
			coments = new ArrayList<ComentBean>();
		}

		//TODO supprimer et recuperer les events de la bdd
		ComentBean comentBean;
		for(int i = 0; i < NB_COMENT_LOADED; i++){
			comentBean = new ComentBean();
			comentBean.setId(indexComent + i);
			comentBean.setTitle("Titre du commentaire " + (indexComent + i) );
			comentBean.setComent("Détails du commentaire " + (indexComent + i) + ": Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi molestie, leo eget viverra luctus, massa sem lacinia est, at bibendum ipsum tellus eu tortor. Donec quis interdum leo. Curabitur in magna magna. Aliquam a odio sem, eu tempus lorem. Praesent consectetur odio nec massa dignissim a luctus purus rhoncus. Donec vitae risus non arcu cursus sodales et nec enim. Nam.");
			coments.add(comentBean);
		}
		indexComent+=NB_COMENT_LOADED;
	}

	@Override
	public int getCount() {
		if(coments != null) {
			return coments.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {return null;}

	@Override
	public long getItemId(int position) {return coments.get(position).getId();}

	@Override
	public int getItemViewType(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new CommentairesView(context, coments.get(position));
	}

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
		if(coments == null | coments.size() == 0) {return true;} else {return false;}
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {}

	@Override
	public boolean areAllItemsEnabled() {return false;}

	@Override
	public boolean isEnabled(int position) {return false;}



}

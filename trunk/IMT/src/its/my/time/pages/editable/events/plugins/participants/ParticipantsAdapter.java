package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.data.bdd.event.participant.ParticipantBean;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class ParticipantsAdapter implements ListAdapter{


	private Context context;
	private List<ParticipantBean> participants;
	private List<UtilisateurBean> utilisateurs;

	private int idEvent;

	public ParticipantsAdapter(Context context, int id) {
		this.context = context;
		this.idEvent = id;
		loadNextParticipants();
	}

	private void loadNextParticipants() {

		participants = DatabaseUtil.getParticipantRepository(context).getAllByEid(idEvent);
		if(participants == null) {
			participants = new ArrayList<ParticipantBean>();
		}
		
		List<Integer> ids = new ArrayList<Integer>();
		for (ParticipantBean participant : participants) {
			ids.add(participant.getUid());
		}
		utilisateurs = DatabaseUtil.getUtilisateurRepository(context).getAllByIds(ids);
		if(utilisateurs == null) {
			utilisateurs = new ArrayList<UtilisateurBean>();
		}
	}

	@Override
	public int getCount() {
		if(utilisateurs != null) {
			return utilisateurs.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {return null;}

	@Override
	public long getItemId(int position) {return utilisateurs.get(position).getId();}

	@Override
	public int getItemViewType(int position) {return 0;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return new ParticipantsView(context, utilisateurs.get(position));
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
		if(utilisateurs == null | utilisateurs.size() == 0) {return true;} else {return false;}
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

package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ParticipantsFragment extends BasePluginFragment {

	private int eventId;
	private ListView mListParticipant;

	public ParticipantsFragment() {
		this(-1);
	}
	
	public ParticipantsFragment(int l) {
		this.eventId = l;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_participant, null);
		mListParticipant = (ListView)mView.findViewById(R.id.event_participants_liste);
		mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(), eventId, false));
		
		return mView;
	}

	@Override
	public void launchEdit() {
		mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(), eventId, true));
	}

	@Override
	public void launchSave() {
		mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(), eventId, true));
	}

	@Override
	public void launchCancel() {
		mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(), eventId, true));
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}
	
	private class ParticipantsAdapter implements ListAdapter{

		private List<ParticipantBean> participants;
		private List<UtilisateurBean> utilisateurs;
		private boolean isInEditMode;

		public ParticipantsAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			loadNextParticipants();
		}

		private void loadNextParticipants() {

			participants = DatabaseUtil.Plugins.getParticipantRepository(getActivity()).getAllByEid(eventId);
			if(participants == null) {
				participants = new ArrayList<ParticipantBean>();
			}
			
			List<Integer> ids = new ArrayList<Integer>();
			for (ParticipantBean participant : participants) {
				ids.add(participant.getUid());
			}
			utilisateurs = DatabaseUtil.getUtilisateurRepository(getActivity()).getAllByIds(ids);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ParticipantsView view = new ParticipantsView(getActivity(), utilisateurs.get(position), isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new CommentRepository(getActivity()).deletecomment(participants.get(position).getId());
					mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(), eventId, true));	
				}
			});
			return view;
		}

		@Override public Object getItem(int position) {return null;}
		@Override public long getItemId(int position) {return utilisateurs.get(position).getId();}
		@Override public int getItemViewType(int position) {return 0;}
		@Override public int getViewTypeCount() {return 1;}
		@Override public boolean hasStableIds() {return true; }
		@Override public boolean isEmpty() { if(utilisateurs == null | utilisateurs.size() == 0) {return true;} else {return false;} }
		@Override public void registerDataSetObserver(DataSetObserver observer) {	}
		@Override public void unregisterDataSetObserver(DataSetObserver observer) {}
		@Override public boolean areAllItemsEnabled() {return false;}
		@Override public boolean isEnabled(int position) {return false;}
	}
}

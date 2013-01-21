package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.comment.CommentRepository;
import its.my.time.data.bdd.events.plugins.participant.ParticipantBean;
import its.my.time.data.bdd.events.plugins.participant.ParticipantRepository;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;

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

	private final int eventId;
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

		final RelativeLayout mView = (RelativeLayout) inflater.inflate(
				R.layout.activity_event_participant, null);
		this.mListParticipant = (ListView) mView
				.findViewById(R.id.event_participants_liste);
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				this.eventId, false));

		return mView;
	}

	@Override
	public String getTitle() {
		return "Participants";
	}

	@Override
	public void launchEdit() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				this.eventId, true));
	}

	@Override
	public void launchSave() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				this.eventId, true));
	}

	@Override
	public void launchCancel() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),
				this.eventId, true));
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

	private class ParticipantsAdapter implements ListAdapter {

		private List<ParticipantBean> participants;
		private List<UtilisateurBean> utilisateurs;
		private final boolean isInEditMode;

		public ParticipantsAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			loadNextParticipants();
		}

		private void loadNextParticipants() {

			this.participants = new ParticipantRepository(getActivity())
					.getAllByEid(ParticipantsFragment.this.eventId);
			if (this.participants == null) {
				this.participants = new ArrayList<ParticipantBean>();
			}

			final List<Integer> ids = new ArrayList<Integer>();
			for (final ParticipantBean participant : this.participants) {
				ids.add(participant.getUid());
			}
			this.utilisateurs = new UtilisateurRepository(getActivity())
					.getAllByIds(ids);
			if (this.utilisateurs == null) {
				this.utilisateurs = new ArrayList<UtilisateurBean>();
			}
		}

		@Override
		public int getCount() {
			if (this.utilisateurs != null) {
				return this.utilisateurs.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ParticipantsView view = new ParticipantsView(getActivity(),
					this.utilisateurs.get(position), this.isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new CommentRepository(getActivity())
							.deletecomment(ParticipantsAdapter.this.participants
									.get(position).getId());
					ParticipantsFragment.this.mListParticipant
							.setAdapter(new ParticipantsAdapter(getActivity(),
									ParticipantsFragment.this.eventId, true));
				}
			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return this.utilisateurs.get(position).getId();
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
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
			if (this.utilisateurs == null | this.utilisateurs.size() == 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}
	}
}

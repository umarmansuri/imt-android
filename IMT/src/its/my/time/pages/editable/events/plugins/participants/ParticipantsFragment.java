package its.my.time.pages.editable.events.plugins.participants;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.participation.ParticipationBean;
import its.my.time.data.bdd.events.plugins.participation.ParticipationRepository;
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

	private ListView mListParticipant;

	private List<ParticipationBean> participants;
	private ParticipationRepository repo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		repo = new ParticipationRepository(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_participant, null);
		this.mListParticipant = (ListView) mView.findViewById(R.id.event_participants_liste);
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
		return mView;
	}

	@Override
	public void refresh() {
		this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), false));
	}

	@Override
	public String getTitle() {
		return "Participants";
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	private class ParticipantsAdapter implements ListAdapter {

		private List<ParticipationBean> participation;
		private final boolean isEditMode;

		public ParticipantsAdapter(Context context, int id, boolean isEditMode) {
			this.isEditMode = isEditMode;
			loadNextEvents();
		}

		private void loadNextEvents() {
			if (this.participation == null) {
				this.participation = new ArrayList<ParticipationBean>();
			}
			this.participation = new ParticipationRepository(getActivity()).getAllByEid(getParentActivity().getEvent().getId());
		}

		@Override
		public int getCount() {
			if (this.participation != null) {
				return this.participation.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			final ParticipantsView view = new ParticipantsView(getActivity(),this.participation.get(position), this.isEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new ParticipationRepository(getActivity()).delete(ParticipantsAdapter.this.participation.get(position));
					ParticipantsFragment.this.mListParticipant.setAdapter(new ParticipantsAdapter(getActivity(),getParentActivity().getEvent().getId(), true));
				}
			});
			return view;
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
			if (this.participation == null | this.participation.size() == 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return this.participation.get(position).getId();
		}

		@Override
		public int getItemViewType(int position) {
			return 0;
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

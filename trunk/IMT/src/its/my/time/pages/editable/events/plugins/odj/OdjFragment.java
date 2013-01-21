package its.my.time.pages.editable.events.plugins.odj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;

public class OdjFragment extends BasePluginFragment {

	private final int eventId;
	private Button mButtonSend;
	private DragSortListView mListOdj;
	private EditText mTextOdj;

	private final DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
		}
	};

	private final DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
		}
	};

	private final DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				// Traverse all views in a millisecond
				return (getOdjAdapter().getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};

	public OdjFragment(int l) {
		this.eventId = l;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final RelativeLayout mView = (RelativeLayout) inflater.inflate(
				R.layout.activity_event_odjs, null);
		this.mListOdj = (DragSortListView) mView
				.findViewById(R.id.event_odj_liste);
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,
				false));
		this.mListOdj.setDropListener(this.onDrop);
		this.mListOdj.setRemoveListener(this.onRemove);
		this.mListOdj.setDragScrollProfile(this.ssProfile);

		this.mTextOdj = (EditText) mView.findViewById(R.id.event_odj_editOdj);
		this.mButtonSend = (Button) mView.findViewById(R.id.event_odj_save);
		this.mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO envoyer via ws
				final OdjBean odj = new OdjBean();
				odj.setValue(OdjFragment.this.mTextOdj.getText().toString());
				odj.setEid(OdjFragment.this.eventId);
				odj.setOrder(OdjFragment.this.mListOdj.getChildCount());
				final long res = new OdjRepository(getActivity())
						.insertOdj(odj);
				if (res < 0) {
					Toast.makeText(getActivity(),
							"Votre objet du jour n'a pu être envoyé.",
							Toast.LENGTH_SHORT).show();
				}
				OdjFragment.this.mListOdj.setAdapter(new OdjAdapter(
						getActivity(), OdjFragment.this.eventId, false));
				OdjFragment.this.mTextOdj.setText("");
			}
		});
		return mView;
	}

	private OdjAdapter getOdjAdapter() {
		return (OdjAdapter) this.mListOdj.getInputAdapter();
	}

	@Override
	public String getTitle() {
		return "Ordre du jour";
	}

	@Override
	public void launchEdit() {
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,
				true));
	}

	@Override
	public void launchSave() {
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,
				false));
	}

	@Override
	public void launchCancel() {
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,
				false));
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}

	private class OdjAdapter implements ListAdapter {

		private List<OdjBean> odj;
		private final boolean isInEditMode;

		public OdjAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			loadNext();
		}

		private void loadNext() {
			if (this.odj == null) {
				this.odj = new ArrayList<OdjBean>();
			}
			this.odj = new OdjRepository(getActivity())
					.getAllByEid(OdjFragment.this.eventId);
		}

		@Override
		public int getCount() {
			if (this.odj != null) {
				return this.odj.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final OdjView view = new OdjView(getActivity(),
					this.odj.get(position), this.isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new OdjRepository(getActivity())
							.deleteOdj(OdjAdapter.this.odj.get(position)
									.getId());
					OdjFragment.this.mListOdj.setAdapter(new OdjAdapter(
							getActivity(), OdjFragment.this.eventId, true));
				}
			});
			return view;
		}

		@Override
		public OdjBean getItem(int position) {
			return this.odj.get(position);
		}

		@Override
		public long getItemId(int position) {
			return this.odj.get(position).getId();
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
			if (this.odj == null | this.odj.size() == 0) {
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

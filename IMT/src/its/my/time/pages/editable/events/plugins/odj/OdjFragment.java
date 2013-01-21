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

	protected List<OdjBean> odjs;
	private final int eventId;
	private Button mButtonSend;
	private DragSortListView mListOdj;
	private EditText mTextOdj;

	
	private final DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			OdjBean odj = odjs.get(from);
			odjs.remove(odj);
			odjs.add(to, odj);
			mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId,true));
		}
	};

	private final DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
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
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,false));
		this.mListOdj.setDropListener(this.onDrop);
		this.mListOdj.setDragScrollProfile(this.ssProfile);

		this.mTextOdj = (EditText) mView.findViewById(R.id.event_odj_editOdj);
		this.mButtonSend = (Button) mView.findViewById(R.id.event_odj_save);
		this.mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO envoyer via ws
				final OdjBean odj = new OdjBean();
				odj.setValue(mTextOdj.getText().toString());
				odj.setEid(eventId);
				odj.setOrder(mListOdj.getChildCount());
				final long res = new OdjRepository(getActivity())
						.insertOdj(odj);
				if (res < 0) {
					Toast.makeText(getActivity(),
							"Votre objet du jour n'a pu �tre envoy�.",
							Toast.LENGTH_SHORT).show();
				}
				mListOdj.setAdapter(new OdjAdapter(
						getActivity(), eventId, false));
				mTextOdj.setText("");
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
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,true));
	}

	@Override
	public void launchSave() {
		OdjRepository repo = new OdjRepository(getActivity());
		OdjBean odj;
		for(int i = 0; i < odjs.size(); i++) {
			odj = odjs.get(i);
			odj.setOrder(i);
			repo.updateOdj(odj);
		}
		mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId,true));
		odjs = null;
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,false));
	}

	@Override
	public void launchCancel() {
		odjs = null;
		this.mListOdj.setAdapter(new OdjAdapter(getActivity(), this.eventId,false));
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

		private final boolean isInEditMode;

		public OdjAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			if(odjs == null) {
				odjs = new OdjRepository(getActivity()).getAllByEid(eventId);
			}
		}

		@Override
		public int getCount() {
			if (odjs != null) {
				return odjs.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final OdjView view = new OdjView(getActivity(),
					odjs.get(position), this.isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					odjs.remove(position);
					mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, true));
				}
			});
			return view;
		}

		@Override
		public OdjBean getItem(int position) {
			return odjs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return odjs.get(position).getId();
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
			if (odjs == null | odjs.size() == 0) {
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

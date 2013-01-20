package its.my.time.pages.editable.events.plugins.odj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.data.bdd.events.plugins.odj.OdjRepository;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;

public class OdjFragment extends BasePluginFragment {

	private int eventId;
	private Button mButtonSend;
	private DragSortListView mListOdj;
	private EditText mTextOdj;

	private DragSortListView.DropListener onDrop =
			new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
		}
	};

	private DragSortListView.RemoveListener onRemove = 
			new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
		}
	};

	private DragSortListView.DragScrollProfile ssProfile =
			new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				// Traverse all views in a millisecond
				return ((float) getOdjAdapter().getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};

	public OdjFragment(int eventId) {
		this.eventId = eventId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_odjs, null);
		mListOdj = (DragSortListView)mView.findViewById(R.id.event_odj_liste);
		mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, false));
		mListOdj.setDropListener(onDrop);
		mListOdj.setRemoveListener(onRemove);
		mListOdj.setDragScrollProfile(ssProfile);

		mTextOdj = (EditText)mView.findViewById(R.id.event_odj_editOdj); 
		mButtonSend = (Button)mView.findViewById(R.id.event_odj_save);
		mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO envoyer via ws
				OdjBean odj = new OdjBean();
				odj.setValue(mTextOdj.getText().toString());
				odj.setEid(eventId);
				odj.setOrder(mListOdj.getChildCount());
				long res = DatabaseUtil.Plugins.getOdjRepository(getActivity()).insertOdj(odj);
				if(res < 0) {
					Toast.makeText(getActivity(), "Votre objet du jour n'a pu être envoyé.", Toast.LENGTH_SHORT).show();
				}
				mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, false));
				mTextOdj.setText("");
			}
		}); 	
		return mView;
	}

	private OdjAdapter getOdjAdapter() {
		return (OdjAdapter)mListOdj.getInputAdapter();
	}

	@Override
	public void launchEdit() {
		mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, true));
	}

	@Override
	public void launchSave() {
		mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, false));
	}

	@Override
	public void launchCancel() {
		mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, false));
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

	private class OdjAdapter  implements ListAdapter{

		private List<OdjBean> odj;
		private boolean isInEditMode;

		public OdjAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			loadNext();
		}

		private void loadNext() {
			if(odj == null) {
				odj = new ArrayList<OdjBean>();
			}
			odj = DatabaseUtil.Plugins.getOdjRepository(getActivity()).getAllByEid(eventId);
		}

		@Override
		public int getCount() {
			if(odj != null) {
				return odj.size();
			}
			return 0; 
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			OdjView view = new OdjView(getActivity(), odj.get(position), isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new OdjRepository(getActivity()).deleteOdj(odj.get(position).getId());
					mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId, true));
				}
			});
			return view;
		}

		@Override public OdjBean getItem(int position) {return odj.get(position);}
		@Override public long getItemId(int position) {return odj.get(position).getId();}
		@Override public int getItemViewType(int position) {return 0;}
		@Override public int getViewTypeCount() {return 1;}
		@Override public boolean hasStableIds() {return true;}
		@Override public boolean isEmpty() {if(odj == null | odj.size() == 0) {return true;} else {return false;} }
		@Override public void registerDataSetObserver(DataSetObserver observer) {	}
		@Override public void unregisterDataSetObserver(DataSetObserver observer) {}
		@Override public boolean areAllItemsEnabled() {return false;}
		@Override public boolean isEnabled(int position) {return false;}
	}


}

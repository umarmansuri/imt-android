package its.my.time.pages.editable.events.plugins.odj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.pages.editable.events.plugins.BaseFragment;
import its.my.time.util.DatabaseUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;

public class OdjFragment extends BaseFragment {

	private int eventId;
	private Button mButtonSend;
	private DragSortListView mListOdj;
	private EditText mTextOdj;

	private DragSortListView.DropListener onDrop =
			new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			OdjBean item=getOdjAdapter().getItem(from);
			getOdjAdapter().remove(item);
			getOdjAdapter().insert(item, to);
			getOdjAdapter().notifyDataSetChanged();
		}
	};

	private DragSortListView.RemoveListener onRemove = 
			new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			getOdjAdapter().remove(getOdjAdapter().getItem(which));
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
		super();
		this.eventId = eventId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_odjs, null);
		mListOdj = (DragSortListView)mView.findViewById(R.id.event_odj_liste);
		mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId));
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
				mListOdj.setAdapter(new OdjAdapter(getActivity(), eventId));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchCancel() {
		// TODO Auto-generated method stub
		
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

}

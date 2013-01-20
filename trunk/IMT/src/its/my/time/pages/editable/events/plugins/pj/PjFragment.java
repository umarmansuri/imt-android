package its.my.time.pages.editable.events.plugins.pj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PjFragment extends BasePluginFragment {

	private int eventId;
	private Button mButtonSend;
	private ListView mListPj;

	private static final int PICK_FILE_RESULT_CODE = 1;

	public PjFragment(int l) {
		this.eventId = l;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout mView = (RelativeLayout) inflater.inflate(
				R.layout.activity_event_piecejointe, null);
		mListPj = (ListView) mView.findViewById(R.id.event_pj_liste);
		mListPj.setAdapter(new PjAdapter(getActivity(), eventId, false));

		mButtonSend = (Button) mView.findViewById(R.id.event_pj_Btenvoi);

		mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");

				intent.addCategory(Intent.CATEGORY_OPENABLE);
				Intent i = Intent.createChooser(intent, "File");
				try {
					startActivityForResult(i, PICK_FILE_RESULT_CODE);
				} catch (android.content.ActivityNotFoundException ex) {
					// TODO Vérifier pourquoi ce n'est pas lui qui est affiché
					// Potentially direct the user to the Market with a Dialog
					// Toast.makeText(getActivity(),
					// "Please install a File Manager.",
					// Toast.LENGTH_SHORT).show();
				}
			}
		});

		return mView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PICK_FILE_RESULT_CODE: {
			if (resultCode == Activity.RESULT_OK && data != null
					&& data.getData() != null) {
				String theFilePath = data.getData().getEncodedPath();
				String[] decoupeNom = theFilePath.split("/");

				// TODO envoyer via ws
				PjBean pj = new PjBean();
				pj.setName(decoupeNom[decoupeNom.length - 1]);
				pj.setLink(theFilePath);
				pj.setDate(Calendar.getInstance());
				pj.setEid(eventId);
				//TODO utilisateur désactivé
				//pj.setUid(PreferencesUtil.getCurrentUid(getActivity()));
				pj.setUid(1);
				long res = DatabaseUtil.Plugins.getPjRepository(getActivity())
						.insertpj(pj);
				if (res < 0) {
					Toast.makeText(getActivity(),
							"Votre pièce jointe n'a pu être envoyée.",
							Toast.LENGTH_SHORT).show();
				}
				mListPj.setAdapter(new PjAdapter(getActivity(), eventId, false));
			}
		};
		break;
		}}

	@Override
	public void launchEdit() {
		mListPj.setAdapter(new PjAdapter(getActivity(), eventId, true));
	}

	@Override
	public void launchSave() {
		mListPj.setAdapter(new PjAdapter(getActivity(), eventId, false));
	}

	@Override
	public void launchCancel() {
		mListPj.setAdapter(new PjAdapter(getActivity(), eventId, false));
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
	
	private class PjAdapter implements ListAdapter{

		private List<PjBean> pjs;
		private boolean isInEditMode;

		public PjAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			loadNextEvents();
		}

		private void loadNextEvents() {
			if(pjs == null) {
				pjs = new ArrayList<PjBean>();
			}
			pjs = DatabaseUtil.Plugins.getPjRepository(getActivity()).getAllByEid(eventId);
		}

		@Override
		public int getCount() {
			if(pjs != null) {
				return pjs.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			PjView view = new PjView(getActivity(), pjs.get(position), isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new PjRepository(getActivity()).deletepj(pjs.get(position).getId());
					mListPj.setAdapter(new PjAdapter(getActivity(), eventId, true));	
				}
			});
			return view;
		}

		@Override public int getViewTypeCount() {return 1;}
		@Override public boolean hasStableIds() {return true;}
		@Override public boolean isEmpty() {if(pjs == null | pjs.size() == 0) {return true;} else {return false;}}
		@Override public Object getItem(int position) {return null;}
		@Override public long getItemId(int position) {return pjs.get(position).getId();}
		@Override public int getItemViewType(int position) {return 0;}
		@Override public void registerDataSetObserver(DataSetObserver observer) {	}
		@Override public void unregisterDataSetObserver(DataSetObserver observer) {}
		@Override public boolean areAllItemsEnabled() {return false;}
		@Override public boolean isEnabled(int position) {return false;}
	}
}


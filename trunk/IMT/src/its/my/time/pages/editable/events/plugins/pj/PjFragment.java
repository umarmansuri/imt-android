package its.my.time.pages.editable.events.plugins.pj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.events.plugins.pj.PjRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.PreferencesUtil;

import java.util.ArrayList;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PjFragment extends BasePluginFragment {

	private Button mButtonSend;
	private ListView mListPj;

	private static final int PICK_FILE_RESULT_CODE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.activity_event_piecejointe, null);
		this.mListPj = (ListView) mView.findViewById(R.id.event_pj_liste);
		refresh(); 
		
		this.mButtonSend = (Button) mView.findViewById(R.id.event_pj_Btenvoi);

		this.mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");

				intent.addCategory(Intent.CATEGORY_OPENABLE);
				final Intent i = Intent.createChooser(intent, "File");
				try {
					startActivityForResult(i, PICK_FILE_RESULT_CODE);
				} catch (final android.content.ActivityNotFoundException ex) {
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
	public void refresh() {
		this.mListPj.setAdapter(new PjAdapter(getActivity(), getParentActivity().getEvent().getId(),false));	
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PICK_FILE_RESULT_CODE: {
			if (resultCode == Activity.RESULT_OK && data != null
					&& data.getData() != null) {
				final String theFilePath = data.getData().getEncodedPath();
				final String[] decoupeNom = theFilePath.split("/");

				final PjBean pj = new PjBean();
				pj.setName(decoupeNom[decoupeNom.length - 1]);
				
				final MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
				final String ext = MimeTypeMap.getFileExtensionFromUrl(theFilePath);
				String type = mimeMap.getMimeTypeFromExtension(ext);
				
				pj.setPath(theFilePath);
				pj.setEid(getParentActivity().getEvent().getId());
				pj.setUid(PreferencesUtil.getCurrentUid());
				final long res = new PjRepository(getActivity()).insert(pj);
				if (res < 0) {
					Toast.makeText(getActivity(),
							"Votre pièce jointe n'a pu être envoyée.",
							Toast.LENGTH_SHORT).show();
				}
				this.mListPj.setAdapter(new PjAdapter(getActivity(),
						getParentActivity().getEvent().getId(), false));
			}
		}
			;
			break;
		}
	}

	@Override
	public String getTitle() {
		return "Pièces jointes";
	}

	@Override
	public void launchEdit() {
		this.mListPj.setAdapter(new PjAdapter(getActivity(), getParentActivity().getEvent().getId(), true));
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		this.mListPj.setAdapter(new PjAdapter(getActivity(), getParentActivity().getEvent().getId(),
				false));
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		this.mListPj.setAdapter(new PjAdapter(getActivity(), getParentActivity().getEvent().getId(),
				false));
		super.launchCancel();
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

	private class PjAdapter implements ListAdapter {

		private List<PjBean> pjs;
		private final boolean isInEditMode;

		public PjAdapter(Context context, int id, boolean isInEditMode) {
			this.isInEditMode = isInEditMode;
			loadNextEvents();
		}

		private void loadNextEvents() {
			if (this.pjs == null) {
				this.pjs = new ArrayList<PjBean>();
			}
			this.pjs = new PjRepository(getActivity()).getAllByEid(getParentActivity().getEvent().getId());
		}

		@Override
		public int getCount() {
			if (this.pjs != null) {
				return this.pjs.size();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final PjView view = new PjView(getActivity(),this.pjs.get(position), this.isInEditMode);
			view.setOnDeleteClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new PjRepository(getActivity()).delete(PjAdapter.this.pjs.get(position));
					PjFragment.this.mListPj.setAdapter(new PjAdapter(
							getActivity(), getParentActivity().getEvent().getId(), true));
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
			if (this.pjs == null | this.pjs.size() == 0) {
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
			return this.pjs.get(position).getId();
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

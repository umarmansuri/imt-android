package its.my.time.pages.editable.events.plugins.pj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.pages.editable.events.plugins.BaseFragment;
import its.my.time.util.DatabaseUtil;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class PjFragment extends BaseFragment {

	private int eventId;
	private Button mButtonSend;
	private ListView mListPj;

	private static final int PICK_FILE_RESULT_CODE = 1;

	public PjFragment(int eventId) {
		this.eventId = eventId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout mView = (RelativeLayout) inflater.inflate(
				R.layout.activity_event_piecejointe, null);
		mListPj = (ListView) mView.findViewById(R.id.event_pj_liste);
		mListPj.setAdapter(new PjAdapter(getActivity(), eventId));

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
				mListPj.setAdapter(new PjAdapter(getActivity(), eventId));
			}
		};
		break;
		}}

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


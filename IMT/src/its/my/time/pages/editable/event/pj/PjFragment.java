package its.my.time.pages.editable.event.pj;

import java.util.Calendar;

import its.my.time.R;
import its.my.time.data.bdd.event.pj.PjBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.PreferencesUtil;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Path;
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

public class PjFragment extends SherlockFragment {

	private int eventId;
	private Button mButtonSend;
	private TextView mLinkPj;
	private ListView mListPj;

	private static final int PICK_FILE_RESULT_CODE = 1;

	public PjFragment(int eventId) {
		super();
		this.eventId = eventId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout mView = (RelativeLayout) inflater.inflate(
				R.layout.activity_event_piecejointe, null);
		mListPj = (ListView) mView.findViewById(R.id.event_pj_liste);
		mListPj.setAdapter(new PjAdapter(getActivity(), eventId));

		mLinkPj = (TextView) mView.findViewById(R.id.event_pj_editPj);

		mButtonSend = (Button) mView.findViewById(R.id.event_pj_Btenvoi);

		mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("file/*");
				
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
				Log.d("DEBUG UPLOAD", "adresse fichier " + theFilePath);
				mLinkPj.setText(theFilePath);
				String[] decoupeNom = theFilePath.split("/");
				Log.d("NAME", "Nom " + decoupeNom[decoupeNom.length - 1]);
				String[] decoupeType = decoupeNom[decoupeNom.length - 1]
						.split("\\.");
				Log.d("NAME", "Type " + decoupeType[decoupeType.length - 1]);

				// TODO envoyer via ws
				PjBean pj = new PjBean();
				pj.setName(decoupeNom[decoupeNom.length - 1]);
				pj.setType(decoupeType[decoupeType.length - 1]);
				pj.setDate(Calendar.getInstance());
				pj.setEid(eventId);
				//TODO utilisateur désactivé
				//pj.setUid(PreferencesUtil.getCurrentUid(getActivity()));
				pj.setUid(1);
				long res = DatabaseUtil.getPjRepository(getActivity())
						.insertpj(pj);
				if (res < 0) {
					Toast.makeText(getActivity(),
							"Votre pièce jointe n'a pu être envoyée.",
							Toast.LENGTH_SHORT).show();
				}
				mListPj.setAdapter(new PjAdapter(getActivity(), eventId));
				mLinkPj.setText("");
			}
		};
		break;
	}}}


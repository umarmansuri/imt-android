package its.my.time.pages.editable.events.plugins.pj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import its.my.time.util.DateUtil;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;

public class PjView extends EditableLittleView {

	private final PjBean pj;

	public PjView(Context context, PjBean pj, boolean isInEditMode) {
		super(context, isInEditMode);
		inflate(context, R.layout.activity_event_piecejointe_little, this);
		setBackgroundColor(Color.WHITE);
		this.pj = pj;
		initialiseDetails();
	}

	private void initialiseDetails() {
		super.initialiseValues();

		new UtilisateurBean();
		new UtilisateurRepository(getContext()).getById(this.pj.getUid());

		((TextView) findViewById(R.id.event_pj_date)).setText(DateUtil
				.getLongDateTime(this.pj.getDate()));
		// TODO activer quand user actif
		// ((TextView)findViewById(R.id.event_pj_owner)).setText(user.getPrenom()+" "+user.getNom());
		((TextView) findViewById(R.id.event_pj_owner))
				.setText("Pedro Orlandes");
		((TextView) findViewById(R.id.event_pj_name))
				.setText(this.pj.getName());
		((ImageButton) findViewById(R.id.event_pj_bouton))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						final File file = new File("/123.txt");
						final MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
						final String ext = MimeTypeMap
								.getFileExtensionFromUrl(file.getAbsolutePath());
						String type = mimeMap.getMimeTypeFromExtension(ext);
						if (type == null) {
							type = "*/*";
						}

						final Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(file), type);
						getContext().startActivity(intent);
					}
				});
	}
}

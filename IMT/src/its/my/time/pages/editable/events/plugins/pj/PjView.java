package its.my.time.pages.editable.events.plugins.pj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.pj.PjBean;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.pages.editable.events.plugins.EditableLittleView;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
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

		((TextView) findViewById(R.id.event_pj_name)).setText(this.pj.getName());
		((ImageButton) findViewById(R.id.event_pj_bouton)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					byte[] pdfAsBytes = Base64.decode(pj.getBase64(), 0);

					File filePath = new File(Environment.getExternalStorageDirectory()+"/" + pj.getName());
					FileOutputStream os = new FileOutputStream(filePath, true);
					os.write(pdfAsBytes);
					os.flush();
					os.close();

					final Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(filePath), pj.getMime());
					getContext().startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

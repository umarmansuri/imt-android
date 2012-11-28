package its.my.time.pages.editable.event.pj;

import its.my.time.R;
import its.my.time.data.bdd.event.pj.PjBean;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class PjView extends FrameLayout{

	private PjBean pj;

	public PjView(Context context) {
		super(context);
	}

	public PjView(Context context, PjBean pj) {
		super(context);
		inflate(context, R.layout.activity_event_piecejointe_little, this);
		setBackgroundColor(Color.WHITE);
		this.pj = pj;

		initialiseDetails();
	}

	private void initialiseDetails() {
		UtilisateurBean user = new UtilisateurBean();
		user = DatabaseUtil.getUtilisateurRepository(getContext()).getById(pj.getUid());

		((TextView)findViewById(R.id.event_pj_date)).setText(DateUtil.getLongDateTime(pj.getDate()));
		//TODO activer quand user actif 
		//((TextView)findViewById(R.id.event_pj_owner)).setText(user.getPrenom()+" "+user.getNom());
		((TextView)findViewById(R.id.event_pj_owner)).setText("Pedro Orlandes");
		((TextView)findViewById(R.id.event_pj_name)).setText(pj.getName());
		((ImageButton)findViewById(R.id.event_pj_bouton)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				File file = new File("/123.txt");
				MimeTypeMap mimeMap = MimeTypeMap.getSingleton();
				String ext = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
				String type = mimeMap.getMimeTypeFromExtension(ext);
				if(type == null) {
					type = "*/*";
				}
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file), type);
				getContext().startActivity(intent);
			}
		});
	}
}

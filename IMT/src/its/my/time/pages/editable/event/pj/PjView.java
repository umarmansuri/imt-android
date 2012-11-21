package its.my.time.pages.editable.event.pj;

import its.my.time.R;
import its.my.time.data.bdd.event.pj.PjBean;
import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.util.DatabaseUtil;
import its.my.time.util.DateUtil;
import its.my.time.util.PreferencesUtil;
import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
		
		((TextView)findViewById(R.id.event_pj_owner)).setText("Pedro Orlandes");
		((TextView)findViewById(R.id.event_pj_date)).setText(DateUtil.getLongDateTime(pj.getDate()));
		//TODO activer quand user actif 
		//((TextView)findViewById(R.id.event_pj_owner)).setText(user.getPrenom()+" "+user.getNom());
		
		((TextView)findViewById(R.id.event_pj_name)).setText(pj.getName());
		
		//TODO Ajouter la miniature en fonction du type de la pj
		if(pj.getType() == "pdf")
		{
			//((ImageView)findViewById(R.id.event_pj_bouton)).setImageBitmap(bm);
		}
	}
}

package its.my.time.pages.editable.compte;

import its.my.time.R;
import its.my.time.data.bdd.compte.CompteBean;
import its.my.time.util.ActivityUtil;
import its.my.time.util.DatabaseUtil;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CompteLittleView extends LinearLayout {


	private CompteBean compte;

	public CompteLittleView(Context context, CompteBean cpt) {
		super(context);
		
		this.compte = cpt;
		if(!compte.isShowed()) {
			Animation anim = new TranslateAnimation(0, -75, 0, 0);
			anim.setFillAfter(true);
			startAnimation(anim);
		}
		
		ImageButton butAffCompte = new ImageButton(getContext());
		butAffCompte.setBackgroundResource(R.drawable.border_backgrnd_enable);
		butAffCompte.getBackground().setColorFilter(compte.getColor(), Mode.MULTIPLY);
		butAffCompte.setImageResource(android.R.drawable.ic_menu_edit);
		butAffCompte.setColorFilter(compte.getColor(), Mode.MULTIPLY);
		butAffCompte.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Animation anim;
				if(compte.isShowed()) { 	
					anim = new TranslateAnimation(0, -75, 0, 0);
				} else {
					anim = new TranslateAnimation(-75, 0, 0, 0);
				}
				anim.setDuration(200);
				anim.setFillAfter(true);
				startAnimation(anim);
				compte.setShowed(compte.isShowed());
				DatabaseUtil.getCompteRepository(getContext()).update(compte);
			} 
		});
		LayoutParams params = new LayoutParams(100, 60);
		addView(butAffCompte, params);
		TextView txtTitleCompte = new TextView(getContext());
		txtTitleCompte.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
		txtTitleCompte.setText(compte.getTitle());
		txtTitleCompte.setTextColor(Color.BLACK);
		txtTitleCompte.setGravity(Gravity.CENTER_VERTICAL);
		params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		params.leftMargin = 30;
		addView(txtTitleCompte, params);


		setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				ActivityUtil.startCompteActivity(getContext(), compte.getId());
				return true;
			}
		});
	}
}
package its.my.time.pages.calendar.day;

import its.my.time.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Ligne extends RelativeLayout{

	private String lib;

	public Ligne(Context context, AttributeSet attrs) {
		super(context, attrs);


		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Ligne);

		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i)
		{
			int attr = a.getIndex(i);
			if(attr == R.styleable.Ligne_libelle) {
				this.lib = a.getString(attr);
			}
		}
		a.recycle();
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getContext().getResources().getDimensionPixelOffset(R.dimen.view_day_height_ligne_heure)));
		setBackgroundResource(R.drawable.background_day_ligne_normal);

		TextView mlibilleHeure = new TextView(getContext());
		if(lib != null) {
			mlibilleHeure.setText(lib);
		}
		mlibilleHeure.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		mlibilleHeure.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				getResources().getDimensionPixelOffset(R.dimen.view_day_height_ligne_heure_half), 
				getResources().getDimensionPixelOffset(R.dimen.view_day_height_ligne_heure));
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		addView(mlibilleHeure, params);

		
		setOnClickListener(new OnClickListener() {
			private boolean isClicked = false;
			
			@Override
			public void onClick(View v) {
				if(isClicked) {
					//TODO creer nouvel evenement
				} else {
					setBackgroundResource(R.drawable.background_day_ligne_pressed);
				}
				
				isClicked = !isClicked;
			}
		});
	}
}

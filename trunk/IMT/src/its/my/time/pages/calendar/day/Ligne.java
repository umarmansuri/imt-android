package its.my.time.pages.calendar.day;

import its.my.time.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Ligne extends RelativeLayout {

	private int heure;

	public Ligne(Context context, AttributeSet attrs) {
		super(context, attrs);

		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.Ligne);

		final int N = a.getIndexCount();
		for (int i = 0; i < N; ++i) {
			final int attr = a.getIndex(i);
			if (attr == R.styleable.Ligne_libelle) {
				this.heure = a.getInt(attr, -1);
			}
		}
		a.recycle();
		setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, getContext()
						.getResources().getDimensionPixelOffset(
								R.dimen.view_day_height_ligne_heure)));
		setBackgroundResource(R.drawable.background_day_ligne_normal);

		final TextView mheureilleHeure = new TextView(getContext());
		if (this.heure != -1) {
			mheureilleHeure.setText("" + this.heure);
		}
		mheureilleHeure.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		mheureilleHeure.setBackgroundColor(Color.WHITE);
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				getResources().getDimensionPixelOffset(
						R.dimen.view_day_height_ligne_heure_half),
				getResources().getDimensionPixelOffset(
						R.dimen.view_day_height_ligne_heure));
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		addView(mheureilleHeure, params);
	}

	public int getHeure() {
		return this.heure;
	}
}

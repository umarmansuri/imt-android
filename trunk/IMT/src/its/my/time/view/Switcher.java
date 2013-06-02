package its.my.time.view;

import its.my.time.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Switcher extends FrameLayout implements OnClickListener {

	private OnStateChangedListener onStateChangedListener;
	private boolean isChecked = false;

	private View mMainView;

	private TextView mOnView;
	private TextView mOffView;

	private Animation animOn;
	private Animation animOff;
	private float itemHalfWidth;

	public Switcher(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Switcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Switcher(Context context) {
		super(context);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.switcher, this);

		this.mMainView = findViewById(R.id.switcher_main);
		this.mOnView = (TextView) findViewById(R.id.switcher_text_on);
		this.mOffView = (TextView) findViewById(R.id.switcher_text_off);

		final Resources r = getResources();
		this.itemHalfWidth = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());

		this.animOn = new TranslateAnimation(-this.itemHalfWidth, 0, 0, 0);
		this.animOff = new TranslateAnimation(0, -this.itemHalfWidth, 0, 0);

		this.animOn.setAnimationListener(this.animListener);
		this.animOff.setAnimationListener(this.animListener);
		refreshValue(true, false);
		setOnClickListener(this);
	}

	public OnStateChangedListener getOnStateChangedListener() {
		return this.onStateChangedListener;
	}

	public void setOnStateChangedListener(
			OnStateChangedListener onStateChangedListener) {
		this.onStateChangedListener = onStateChangedListener;
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public void changeState(boolean isChecked, boolean withAnim, boolean isInit) {
		this.isChecked = isChecked;
		refreshValue(withAnim, !isInit);
	}

	public void changeState(boolean isChecked, boolean withAnim) {
		this.isChecked = isChecked;
		refreshValue(withAnim, true);
	}

	public void toggleState(boolean withAnim) {
		changeState(!this.isChecked, withAnim);
	}

	private void refreshValue(boolean withAnim, boolean launchListener) {
		this.animListener.setLaunchListener(launchListener);
		if (this.isChecked) {
			animToOn(withAnim);
		} else {
			animToOff(withAnim);
		}
	}

	private void animToOn(boolean withAnim) {
		if (withAnim) {
			this.animOn.setDuration(200);
		} else {
			this.animOn.setDuration(1);
		}
		this.animOn.setFillAfter(true);
		this.mMainView.startAnimation(this.animOn);
	}

	private void animToOff(boolean withAnim) {
		if (withAnim) {
			this.animOff.setDuration(200);
		} else {
			this.animOff.setDuration(1);
		}
		this.animOff.setFillAfter(true);
		this.mMainView.startAnimation(this.animOff);
	}

	@Override
	public void onClick(View v) {
		this.isChecked = !this.isChecked;
		refreshValue(true, true);
	}

	@SuppressWarnings("deprecation")
	public void changeOnColor(Drawable drawable) {
		this.mOnView.setBackgroundDrawable(drawable);
		this.mOnView.setText(null);

		GradientDrawable dr = (GradientDrawable) this.mOffView.getBackground().getConstantState().newDrawable();
		dr = (GradientDrawable) dr.mutate();
		dr.setColor(getResources().getColor(R.color.grey));
		this.mOffView.setBackgroundDrawable(dr);
		this.mOffView.setText(null);
	}

	private final SwitcherAnimationListener animListener = new SwitcherAnimationListener();

	public class SwitcherAnimationListener implements AnimationListener {
		private boolean launchListener = true;

		public boolean isLaunchListener() {
			return this.launchListener;
		}

		public void setLaunchListener(boolean launchListener) {
			this.launchListener = launchListener;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (animation == Switcher.this.animOff) {
				Switcher.this.mOffView.setVisibility(View.VISIBLE);
			} else {
				Switcher.this.mOnView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (animation == Switcher.this.animOff) {
				Switcher.this.mOnView.setVisibility(View.INVISIBLE);
			} else {
				Switcher.this.mOffView.setVisibility(View.INVISIBLE);
			}
			Switcher.this.mMainView.invalidate();
			if (Switcher.this.onStateChangedListener != null
					&& this.launchListener) {
				Switcher.this.onStateChangedListener.onStateCHangedListener(
						Switcher.this, Switcher.this.isChecked);
			}
		}
	}

	public interface OnStateChangedListener {
		public void onStateCHangedListener(Switcher switcher, boolean isChecked);
	}

}
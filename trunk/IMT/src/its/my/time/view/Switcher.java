package its.my.time.view;

import its.my.time.R;
import android.content.Context;
import android.content.res.Resources;
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

		mMainView = findViewById(R.id.switcher_main);
		mOnView = (TextView) findViewById(R.id.switcher_text_on);
		mOffView = (TextView) findViewById(R.id.switcher_text_off);

		Resources r = getResources();
		itemHalfWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());

		animOn = new TranslateAnimation(-itemHalfWidth, 0, 0, 0);
		animOff = new TranslateAnimation(0, -itemHalfWidth, 0, 0); 

		animOn.setAnimationListener(animListener);
		animOff.setAnimationListener(animListener);
		refreshValue(true, false);
		setOnClickListener(this);
	}

	public OnStateChangedListener getOnStateChangedListener() {
		return onStateChangedListener;
	}

	public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
		this.onStateChangedListener = onStateChangedListener;
	}

	public boolean isChecked() {
		return isChecked;
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
		changeState(!isChecked, withAnim);
	}

	private void refreshValue(boolean withAnim, boolean launchListener) {
		animListener.setLaunchListener(launchListener);
		if (isChecked)
			animToOn(withAnim);
		else
			animToOff(withAnim);
	}


	private void animToOn(boolean withAnim) {
		if(withAnim) {
			animOn.setDuration(200);
		} else {
			animOn.setDuration(1);	
		}
		animOn.setFillAfter(true);
		mMainView.startAnimation(animOn);
	}

	private void animToOff(boolean withAnim) {
		if(withAnim) {
			animOff.setDuration(200);
		} else {
			animOff.setDuration(1);	
		}
		animOff.setFillAfter(true);
		mMainView.startAnimation(animOff);		
	}


	@Override
	public void onClick(View v) {
		isChecked = !isChecked;
		refreshValue(true, true);
	}

	public void changeOnColor(int color) {
		GradientDrawable dr = (GradientDrawable)mOnView.getBackground().getConstantState().newDrawable();
		dr = (GradientDrawable) dr.mutate();
		dr.setColor(color);
		mOnView.setBackgroundDrawable(dr);
		mOnView.setText(null);

		dr = (GradientDrawable)mOffView.getBackground().getConstantState().newDrawable();
		dr = (GradientDrawable) dr.mutate();
		dr.setColor(getResources().getColor(R.color.grey));
		mOffView.setBackgroundDrawable(dr);
		mOffView.setText(null);
	}



	private SwitcherAnimationListener animListener = new SwitcherAnimationListener();

	public class SwitcherAnimationListener implements AnimationListener {
		private boolean launchListener = true;

		public boolean isLaunchListener() {
			return launchListener;
		}

		public void setLaunchListener(boolean launchListener) {
			this.launchListener = launchListener;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if(animation == animOff) {
				mOffView.setVisibility(View.VISIBLE);
			} else {
				mOnView.setVisibility(View.VISIBLE);	
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {}

		@Override
		public void onAnimationEnd(Animation animation) {
			if(animation == animOff) {
				mOnView.setVisibility(View.INVISIBLE);
			} else {
				mOffView.setVisibility(View.INVISIBLE);
			}
			mMainView.invalidate();
			if (onStateChangedListener != null && launchListener) {
				onStateChangedListener.onStateCHangedListener(Switcher.this, isChecked);
			}
		}
	}

	public interface OnStateChangedListener {
		public void onStateCHangedListener(Switcher switcher, boolean isChecked);
	}


}
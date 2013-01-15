package its.my.time.view;

import its.my.time.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class Switcher extends FrameLayout implements OnClickListener {

	private OnStateChangedListener onStateChangedListener;
	private boolean isChecked = false;
	private View mOnView;
	private View mOffView;
	private View mMainView;

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
		mOnView = findViewById(R.id.switcher_text_on);
		mOffView = findViewById(R.id.switcher_text_off);
		setOnClickListener(this);

		Resources r = getResources();
		itemHalfWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
		
		animOn = new TranslateAnimation(-itemHalfWidth, 0, 0, 0);
		animOff = new TranslateAnimation(0, -itemHalfWidth, 0, 0); 
		animOn.setAnimationListener(animListener);
		animOff.setAnimationListener(animListener);
		
		refreshValue(false);
	}

	public OnStateChangedListener getOnStateChangedListener() {
		return onStateChangedListener;
	}

	public void setOnStateChangedListener(
			OnStateChangedListener onStateChangedListener) {
		this.onStateChangedListener = onStateChangedListener;
	}

	public boolean isChecked() {
		return isChecked;
	}
	
	public void changeState(boolean isChecked, boolean withAnim) {
		this.isChecked = isChecked;
		refreshValue(withAnim);
	}

	private void refreshValue(boolean withAnim) {
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


	private AnimationListener animListener = new AnimationListener() {
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
			if (onStateChangedListener != null) {
				onStateChangedListener.onStateCHangedListener(Switcher.this, isChecked);
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		isChecked = !isChecked;
		refreshValue(true);
	}

	public interface OnStateChangedListener {
		public void onStateCHangedListener(Switcher switcher, boolean isChecked);
	}

}
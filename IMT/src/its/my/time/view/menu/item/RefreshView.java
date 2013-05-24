package its.my.time.view.menu.item;

import its.my.time.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class RefreshView extends FrameLayout{

	private ImageView mImage;
	private ProgressBar mProgress;

	public RefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RefreshView(Context context) {
		super(context);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.menu_item_refresh, this);
		mImage = (ImageView)findViewById(R.id.imageView);
		mProgress = (ProgressBar)findViewById(R.id.progressBar);
	}

	public void setOnProgress(boolean onProgress) {
		if(onProgress) {
			mImage.setVisibility(View.GONE);
			mProgress.setVisibility(View.VISIBLE);
		} else {
			mImage.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);
		}
	}
	
}

package its.my.time.pages.editable.events.plugins;

import its.my.time.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.fonts.mooncake.MooncakeIcone;

public abstract class EditableLittleView extends FrameLayout{

	private boolean isInEditMode;
	private MooncakeIcone deleteIcone;
	private View grabberIcone;
	private OnClickListener onDeleteClickListener;
	
	
	
	public EditableLittleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EditableLittleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditableLittleView(Context context) {
		super(context);
	}

	public EditableLittleView(Context context, boolean isEditMode) {
		super(context);
		this.isInEditMode = isEditMode;
	}
	
	public void initialiseValues() {
		deleteIcone = (MooncakeIcone)findViewById(R.id.delete);
		if(deleteIcone != null) {
			deleteIcone.setIconeRes(MooncakeIcone.icon_minus_sign);
			if(isInEditMode) {
				deleteIcone.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(onDeleteClickListener != null) {
							onDeleteClickListener.onClick(deleteIcone);
						}
					}
				});
			} else {
				deleteIcone.setVisibility(View.INVISIBLE);
			}
		}

		grabberIcone = findViewById(R.id.grabber);
		if(grabberIcone != null) {
			if(isInEditMode) {

			} else {
				grabberIcone.setVisibility(View.INVISIBLE);
			}
		}
	}

	public OnClickListener getOnDeleteClickListener() {
		return onDeleteClickListener;
	}

	public void setOnDeleteClickListener(OnClickListener onDeleteClickListener) {
		this.onDeleteClickListener = onDeleteClickListener;
	}
	
	
}

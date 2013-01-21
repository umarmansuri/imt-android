package its.my.time.pages.editable.events.plugins;

import its.my.time.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.fonts.mooncake.MooncakeIcone;

public abstract class EditableLittleView extends FrameLayout {

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
		this.deleteIcone = (MooncakeIcone) findViewById(R.id.delete);
		if (this.deleteIcone != null) {
			this.deleteIcone.setIconeRes(MooncakeIcone.icon_minus_sign);
			if (this.isInEditMode) {
				this.deleteIcone.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (EditableLittleView.this.onDeleteClickListener != null) {
							EditableLittleView.this.onDeleteClickListener
									.onClick(EditableLittleView.this.deleteIcone);
						}
					}
				});
			} else {
				this.deleteIcone.setVisibility(View.INVISIBLE);
			}
		}

		this.grabberIcone = findViewById(R.id.grabber);
		if (this.grabberIcone != null) {
			if (this.isInEditMode) {

			} else {
				this.grabberIcone.setVisibility(View.INVISIBLE);
			}
		}
	}

	public OnClickListener getOnDeleteClickListener() {
		return this.onDeleteClickListener;
	}

	public void setOnDeleteClickListener(OnClickListener onDeleteClickListener) {
		this.onDeleteClickListener = onDeleteClickListener;
	}

}

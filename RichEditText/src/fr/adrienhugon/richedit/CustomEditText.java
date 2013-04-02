package fr.adrienhugon.richedit;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

public final class CustomEditText extends EditText {

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomEditText(Context context) {
		super(context);
	}
	
	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		super.onSelectionChanged(selStart, selEnd);
		if(onSelectionChangedListener != null) {
			onSelectionChangedListener.onSelectionChanged(getText(), selStart, selEnd);
		}
	}

	public void updateText(CharSequence text) {
		super.setText(text);
	}
	
	private OnSelectionChangedListener onSelectionChangedListener;
	public OnSelectionChangedListener getOnSelectionChangedListener() {
		return onSelectionChangedListener;
	}
	public void setOnSelectionChangedListener(
			OnSelectionChangedListener onSelectionChangedListener) {
		this.onSelectionChangedListener = onSelectionChangedListener;
	}
	public interface OnSelectionChangedListener{
		public void onSelectionChanged(Editable text, int start, int end);
	}
}

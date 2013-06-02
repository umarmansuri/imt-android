package its.my.time.pages.editable.comptes.compte;

import its.my.time.R;
import its.my.time.util.ColorUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class ColorPickerDialog extends AlertDialog implements android.view.View.OnClickListener {

	public ColorPickerDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_color);

		findViewById(R.id.colorBlack).setOnClickListener(this);
		findViewById(R.id.colorBlue).setOnClickListener(this);
		findViewById(R.id.colorGreen).setOnClickListener(this);
		findViewById(R.id.colorOrange).setOnClickListener(this);
		findViewById(R.id.colorRed).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(onColorChangedListener != null) {
			if(v.getId() == R.id.colorBlack) {onColorChangedListener.onColorChanged(ColorUtil.BLACK.label);}
			if(v.getId() == R.id.colorBlue) {onColorChangedListener.onColorChanged(ColorUtil.BLUE.label);}
			if(v.getId() == R.id.colorGreen) {onColorChangedListener.onColorChanged(ColorUtil.GREEN.label);}
			if(v.getId() == R.id.colorOrange) {onColorChangedListener.onColorChanged(ColorUtil.ORANGE.label);}
			if(v.getId() == R.id.colorRed) {onColorChangedListener.onColorChanged(ColorUtil.RED.label);}
		}
		dismiss();
	}

	private ColorChangeListener onColorChangedListener;
	public void setOnColorChangedListener( ColorChangeListener onColorChangedListener) {
		this.onColorChangedListener = onColorChangedListener;
	}
	public interface ColorChangeListener {
		public void onColorChanged(String color);
	}

}

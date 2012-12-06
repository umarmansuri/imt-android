package fonts;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

public abstract class Icone extends TextView {
	private int mIconeReference;

	public Icone(Context context) {
		this(context, 0);
	}
	
	public Icone(Context context, int iconeReference) {
		this(context, iconeReference, 18);
	}
	
	public Icone(Context context, int iconeReference, int size) {
		super(context);
		initialiseTypeFace();
		changeIcone(iconeReference);
		
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		setTextSize(size);
		
		int padding = (int) (10 * scaledDensity);
		setPadding(padding,padding,padding,padding);
	}

	public int getmIconeReference() {
		return mIconeReference;
	}

	public void setmIconeReference(int mIconeReference) {
		this.mIconeReference = mIconeReference;
	}
	
	public void changeIcone(int iconeReference) {
		mIconeReference = iconeReference;
		setText(Html.fromHtml(Character.toString((char)mIconeReference)));
	}
	
	public abstract void initialiseTypeFace();
}

package its.my.time.pages.editable.event.details;

import its.my.time.R;
import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

public class DetailsView extends FrameLayout{

	public DetailsView(Context context) {
		super(context);
		addView(inflate(context, R.layout.activity_event_event, null));
		setBackgroundColor(Color.WHITE);
	}
}

package its.my.time.pages.editable.events.plugins.odj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

public class OdjView extends FrameLayout{

	private OdjBean odj;
	
	public OdjView(Context context) {
		super(context);
	}
	
	public OdjView(Context context, OdjBean odj) {
		super(context);
		inflate(context, R.layout.activity_event_odjs_little, this);
		setBackgroundColor(Color.WHITE);
		this.odj = odj;
		
		initialiseDetails();
	}

	private void initialiseDetails() {;
		((TextView)findViewById(R.id.event_odj_odjs)).setText(odj.getValue());
	}
}

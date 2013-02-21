package its.my.time.pages.editable.events.plugins.odj;

import its.my.time.R;
import its.my.time.data.bdd.events.plugins.odj.OdjBean;
import its.my.time.pages.editable.events.plugins.EditableLittleView;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class OdjView extends EditableLittleView {

	private final OdjBean odj;

	public OdjView(Context context, OdjBean odj, boolean isInEditMode) {
		super(context, isInEditMode);
		inflate(context, R.layout.activity_event_odjs_little, this);
		setBackgroundColor(Color.WHITE);
		this.odj = odj;
		initialiseDetails();
	}

	private void initialiseDetails() {
		super.initialiseValues();
		((TextView) findViewById(R.id.event_odj_odjs)).setText(this.odj.getValue());
	}
}

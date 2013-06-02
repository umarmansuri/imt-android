package its.my.time.pages.editable.events.call.details;

import its.my.time.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class CorrespondantChoiceDialog extends AlertDialog implements android.view.View.OnClickListener {

	private String user;
	
	public CorrespondantChoiceDialog(Context context, String user, String identifiant, String phone) {
		super(context);
		this.user = user;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_correspondant);

		findViewById(R.id.correspondant_phone).setOnClickListener(this);
		findViewById(R.id.correspondant_repertoire).setOnClickListener(this);
		findViewById(R.id.correspondant_mytime).setOnClickListener(this);

		if(user != null && !user.equals("")) {
			findViewById(R.id.correspondant_clear).setOnClickListener(this);
		} else {
			findViewById(R.id.correspondant_clear).setVisibility(View.GONE);
			findViewById(R.id.correspondant_last_divider).setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if(v.getId() == R.id.correspondant_clear) {
			if(onCorrespondantChange != null) {
				onCorrespondantChange.onCorrespondantChange("","","");
			}
		} else if(v.getId() == R.id.correspondant_phone) {
			new CorrespondanteCatchDialog(getContext(), CorrespondanteCatchDialog.TYPE_PHONE, onCorrespondantChange).show();
		} else if(v.getId() == R.id.correspondant_repertoire) {
			new CorrespondanteCatchDialog(getContext(), CorrespondanteCatchDialog.TYPE_REPERTOIRE, onCorrespondantChange).show();
		} else if(v.getId() == R.id.correspondant_mytime) {
			new CorrespondanteCatchDialog(getContext(), CorrespondanteCatchDialog.TYPE_MYTIME, onCorrespondantChange).show();
		}
	}

	public OnCorrespondantChange onCorrespondantChange;
	public void setOnCorrespondantChange(OnCorrespondantChange onCorrespondantChange) {
		this.onCorrespondantChange = onCorrespondantChange;
	}
}

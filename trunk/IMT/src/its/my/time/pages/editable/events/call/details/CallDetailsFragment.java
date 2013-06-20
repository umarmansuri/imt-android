package its.my.time.pages.editable.events.call.details;

import its.my.time.R;
import its.my.time.data.bdd.events.details.call.CallDetailsBean;
import its.my.time.pages.editable.events.call.CallActivity;
import its.my.time.pages.editable.events.event.details.DetailsFragment;
import its.my.time.util.CallManager;
import its.my.time.util.Types;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CallDetailsFragment extends DetailsFragment implements
OnClickListener, OnCorrespondantChange {

	private static final String KEY_BUNDLE_PHONE = "KEY_BUNDLE_PHONE";
	private static final String KEY_BUNDLE_USER = "KEY_BUNDLE_USER";
	private static final String KEY_BUNDLE_IDENTIFIANT = "KEY_BUNDLE_IDENTIFIANT";
	private static final String KEY_BUNDLE_COUNT = "KEY_BUNDLE_COUNT";

	private EditText mEditCorrespondant;
	private Button mButtonCall;
	private TextView mTextCount;
	private long duration;
	private String identifiant;
	private String phone;
	private String user;
	private TelephonyManager telephonyManager;
	private LayoutParams layoutParamBtnCall;

	@Override
	public String getTitle() {
		return "Appel";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		telephonyManager = (TelephonyManager) getActivity().getSystemService(
				Context.TELEPHONY_SERVICE);
	}

	@Override
	protected View getCustomView() {
		View v = View.inflate(getActivity(),R.layout.activity_event_call_details, null);
		mEditCorrespondant = (EditText) v.findViewById(R.id.activity_event_details_text_correspondant);
		mEditCorrespondant.setFocusable(false);
		mEditCorrespondant.setOnClickListener(this);
		mButtonCall = (Button) v.findViewById(R.id.activity_event_details_button_call);
		mButtonCall.setEnabled(false);
		layoutParamBtnCall = mButtonCall.getLayoutParams();
		mButtonCall.setOnClickListener(this);
		mTextCount = (TextView) v.findViewById(R.id.activity_event_details_text_count);
		return v;
	}

	@Override
	public void launchSave() {
		getParentActivity().getEvent().setTypeId(Types.Event.CALL);

		super.launchSave();
		
		getParentActivity().getCallDetails().setEid(getParentActivity().getEvent().getId());
		getParentActivity().getCallDetails().setDuration(duration);
		getParentActivity().getCallDetails().setIdentifiant(identifiant);
		getParentActivity().getCallDetails().setPhone(phone);
		getParentActivity().getCallDetails().setUser(user);

		if(getParentActivity().getCallDetails().getId() <= 0) {
			long id = getParentActivity().getCallDetailsRepo().insert(getParentActivity().getCallDetails());
			getParentActivity().getCallDetails().setId((int)id);
		} else {
			getParentActivity().getCallDetailsRepo().update(getParentActivity().getCallDetails());
		}
		mButtonCall.setEnabled(true);
	}

	@Override
	public void launchCancel() {
		super.launchCancel();
		mButtonCall.setEnabled(false);
	}

	@Override
	public void launchEdit() {
		super.launchEdit();
		mButtonCall.setEnabled(false);
	}

	public void initialiseValuesFromEvent() {
		CallDetailsBean details = getParentActivity().getCallDetails();
		user = details.getUser();
		identifiant = details.getIdentifiant();
		phone = details.getPhone();
		duration = details.getDuration();
		update();
		super.initialiseValuesFromEvent();
	}

	private CharSequence getDurationLabel(long duration) {
		Calendar calendar = new GregorianCalendar(0, 0, 0, 0, 0, 0);
		calendar.add(Calendar.SECOND, (int) duration);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("m'min' s's'");
		return dateFormat.format(calendar.getTime());
	}

	public void initialiseValueFromInstance() {
		user = state.getString(KEY_BUNDLE_USER);
		identifiant = state.getString(KEY_BUNDLE_IDENTIFIANT);
		phone = state.getString(KEY_BUNDLE_PHONE);
		duration = state.getInt(KEY_BUNDLE_COUNT);
		update();
		super.initialiseValueFromInstance();
	}

	private void update() {
		mEditCorrespondant.setText(user);
		mTextCount.setText(getDurationLabel(duration));
	}

	@Override
	public CallActivity getParentActivity() {
		return (CallActivity) getActivity();
	}

	@Override
	public void onClick(View v) {
		if (v == mEditCorrespondant) {
			CorrespondantChoiceDialog dialog = new CorrespondantChoiceDialog(
					getActivity(), user, identifiant, phone);
			dialog.setOnCorrespondantChange(this);
			dialog.show();
		} else if (v == mButtonCall) {
			if(currentCall != null) {
				closeCall();
				return;
			}
			if (phone != null && !phone.equals("") && identifiant != null && !identifiant.equals("")) {
				new CallChoiceDialog(getActivity()).show();
			} else if (phone != null && !phone.equals("")) {
				launchPhoneCall(phone);
			} else if (identifiant != null && !identifiant.equals("")) {
				launchVoipCall(identifiant);
			} else {
				launchVoipCall("101");
			}
		}
	}

	private void launchPhoneCall(String phone2) {
		telephonyManager.listen(new CallListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
		String uri = "tel:" + phone;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(uri));
		startActivity(intent);
	}

	private void launchVoipCall(final String identifiant) {
		new Thread(new Runnable() {
			@Override public void run() {runVoipCall(identifiant);}
		}).start();
	}

	private void runVoipCall(String identifiant2) {
		CallManager.addListener(sipAudioCallListener);
		CallManager.call(identifiant2);
	}

	private SipAudioCall currentCall = null;
	private SipAudioCall.Listener sipAudioCallListener  = new SipAudioCall.Listener() {
		public void onCalling(SipAudioCall call) {
			currentCall = call;
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mButtonCall.setBackgroundResource(R.drawable.btn_yellow);
					mButtonCall.setText("Appel en cours");	 
					mButtonCall.setLayoutParams(layoutParamBtnCall);
				}});
		}

		public void onCallEstablished(SipAudioCall call) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					startCount();
					mButtonCall.setBackgroundResource(R.drawable.btn_red);
					mButtonCall.setText("Raccrocher");
					mButtonCall.setLayoutParams(layoutParamBtnCall);
				}
			});
		}

		public void onCallEnded(SipAudioCall call) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					closeCall();
				}
			});		
		}
	};;

	public void closeCall() {
		CallManager.hangup();
		currentCall.close();
		currentCall = null;
		endCount();
		mButtonCall.setBackgroundResource(R.drawable.btn_green);
		mButtonCall.setText("Lancer l'appel");
		mButtonCall.setOnClickListener(CallDetailsFragment.this);
		mButtonCall.setLayoutParams(layoutParamBtnCall);

		CallManager.removeListener(sipAudioCallListener);
	}

	private long launchTime;

	private void startCount() {
		launchTime = Calendar.getInstance().getTimeInMillis();
	}

	private void endCount() {
		if(launchTime > 0) {
			duration = duration + (Calendar.getInstance().getTimeInMillis() - launchTime) / 1000;
			mTextCount.setText(getDurationLabel(duration));
			launchTime = 0;
		}
		launchSave();
	}

	@Override
	public void onCorrespondantChange(String user, String phone, String identifiant) {
		this.user = user;
		this.phone = phone;
		this.identifiant = identifiant;
		update();
	}

	private class CallListener extends PhoneStateListener {
		private boolean isStarted = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				
			}
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {

			}
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				if (!isStarted) {
					startCount();
				} else {
					endCount();
				}
				isStarted = !isStarted;
			}
		}
	}

	private class CallChoiceDialog extends AlertDialog implements android.view.View.OnClickListener {

		public CallChoiceDialog(Context context) {
			super(context);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.dialog_call_choice);

			findViewById(R.id.correspondant_phone).setOnClickListener(this);
			findViewById(R.id.correspondant_voip).setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			dismiss();
			if(v.getId() == R.id.correspondant_phone) {
				launchPhoneCall(phone);
			} else if(v.getId() == R.id.correspondant_voip) {
				launchVoipCall(identifiant);
			}
		}
	}
}
package its.my.time.sip;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipAudioCall.Listener;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.util.Log;


public class CallManager {

	protected static final String TAG = "SIP";

	private static final String USERNAME = "102";
	private static final String SERVER_ADRESSE = "192.168.1.28";
	private static final String PASSWORD = "123123";
	private static SipManager manager;
	private static SipProfile me;
	private static SipAudioCall call;
	private static Listener callListener = new SipAudioCall.Listener() {
		public void onCalling(SipAudioCall call) {
			for (SipAudioCall.Listener listener : listeners) {
				listener.onCalling(call);	
			}
		};
		
		@Override
		public void onCallEstablished(SipAudioCall call) {
			CallManager.call = call;
			call.startAudio();
			Log.d(TAG, "appel etablie");
			for (SipAudioCall.Listener listener : listeners) {
				listener.onCallEstablished(call);	
			}
		}

		@Override
		public void onCallEnded(SipAudioCall call) {
			CallManager.call = null;
			Log.d(TAG, "appel finit");
			for (SipAudioCall.Listener listener : listeners) {
				listener.onCallEnded(call);	
			}
		}
	};

	private static Context context;

	public static void initializeManager(Context con) {
		CallManager.context = con;
		if(manager == null) {
			manager = SipManager.newInstance(context);
		}

		initializeLocalProfile(context);
	}

	/**
	 * Logs you into your SIP provider, registering this device as the location to
	 * send SIP calls to for your SIP address.
	 */
	private static void initializeLocalProfile(Context context) {
		if (manager == null) {
			return;
		}

		if (me != null) {
			closeLocalProfile();
		}

		try {
			SipProfile.Builder builder = new SipProfile.Builder(USERNAME, SERVER_ADRESSE);
			builder.setPassword(PASSWORD);
			builder.setDisplayName("Android");
			me = builder.build();

			Intent i = new Intent();
			i.setAction("android.SipDemo.INCOMING_CALL");
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, Intent.FILL_IN_DATA);
			manager.open(me, pi, null);

			manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
				public void onRegistering(String localProfileUri) {

					Log.d(TAG, "connexion en cours");
				}

				public void onRegistrationDone(String localProfileUri, long expiryTime) {
					Log.d(TAG, "connecté");
				}

				public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
					Log.d(TAG, "connxion erreur: " + errorMessage);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeLocalProfile() {
		if (manager == null) {
			return;
		}
		try {
			if (me != null) {
				manager.close(me.getUriString());
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	public static void call(String destinataire) {
		String destAdress = "sip:" + destinataire + "@" + SERVER_ADRESSE;
		try {
			call = manager.makeAudioCall(me.getUriString(), destAdress, callListener, 30);
		}
		catch (Exception e) {
			if (me != null) {
				try {
					manager.close(me.getUriString());
				} catch (Exception ee) {
					ee.printStackTrace();
				}
			}
			if (call != null) {
				call.close();
			}
		}
	}

	public static void answer() {
		if(call != null) {
			try {
				call.answerCall(30);
				call.startAudio();
			} catch (Exception e) {
				if (call != null) {
					call.close();
					call = null;
				}
				e.printStackTrace();
			}
		}
	}

	public static void hangup() {
		if(call != null) {
			try {
				call.endCall();
			} catch (SipException se) {
				se.printStackTrace();
			}
			call.close();
			call = null;
		}
	}

	public static void loadCall(Intent intent) {
		try {
			call = manager.takeAudioCall(intent, callListener);
		} catch (SipException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<SipAudioCall.Listener> listeners = new ArrayList<SipAudioCall.Listener>();
	public static void addListener(SipAudioCall.Listener listener) {
		listeners.add(listener);
	}


	public static void removeListener(SipAudioCall.Listener listener) {
		listeners.remove(listener);
	}



}


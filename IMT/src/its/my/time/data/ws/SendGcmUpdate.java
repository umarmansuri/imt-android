package its.my.time.data.ws;

import android.app.Activity;

public class SendGcmUpdate extends WSBase{

	public SendGcmUpdate(Activity context, String gcmId, Callback callBack) {
		super(context, callBack);
	}

	@Override
	protected Exception run() {
		try {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
	}

}

package its.my.time.data.ws;


import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public abstract class WSBase extends AsyncTask<Void, Void, Void>{


	public static final String URL_BASE = "https://app.my-time.fr/";
	public static final String CLIENT_ID = "1_51ubfhpw9w084owsk0oogskgs840w0wg0g0scsgc4o08wk8w0k";
	public static final String CLIENT_SECRET = "65vs583ttm4owg8040cccsogs04g0kkgs0w080ogwg0kw4cogg";
	public static final String URL_REDIRECT = "http://5.135.156.82/login";

	public static final String URL_REPORTING = "http://app.my-time.fr/IMT/reporting/mobile/";
	public static final String URL_LOGIN = "http://app.my-time.fr/login";
	public static final String URL_LOGIN_CHECK = "http://app.my-time.fr/login_check";
	
	//public static final String URL_BASE = "http://192.168.43.133/my-time/web/app_dev.php/";
	//public static final String CLIENT_ID = "1_4df992bsjim8kc0kko4wkkwgwogssw0s0gc0k4wcos0sgsg0wc";
	//public static final String CLIENT_SECRET = "65vs583ttm4owg8040cccsogs04g0kkgs0w080ogwg0kw4cogg";
	//public static final String URL_REDIRECT = "http://localhost";

	public static final String URL_ACCESS = URL_BASE + "oauth/v2/auth";

	public static final String URL_FORM_LOGIN = URL_BASE + "oauth/v2/auth_login";
	public static final String URL_FORM_ACCEPT = URL_BASE + "oauth/v2/auth?client_id";

	public static final String URL_REFRESH = URL_BASE + "oauth/v2/token";
	public static final String URL_FORM_CREATE = "https://app.my-time.fr/login";

	private Callback callBack;
	public static Context context;

	public WSBase(Context context, Callback callBack) {
		if(WSBase.context == null) { 
			WSBase.context = context;
		}
		this.callBack = callBack;
	}

	public Context getContext() {
		return context;
	}

	public Callback getCallBack() {
		return callBack;
	}

	public static HttpClient createClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	@Override
	public Void doInBackground(Void... params) {
		WSLogin.checkConnexion(context, new Callback() {			
			@Override
			public void done(Exception e) {
				final Exception exception = run();
				if(callBack != null) {
					try {
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								callBack.done(exception);
							}
						});

					} catch (Exception e2) {}
				}
			}
		});
		return null;
	}

	protected abstract Exception run();

}


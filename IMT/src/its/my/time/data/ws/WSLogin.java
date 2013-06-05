package its.my.time.data.ws;

import its.my.time.util.PreferencesUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WSLogin {

	private static List<Callback> callBacks = new ArrayList<Callback>();

	public static Activity context;
	private static RefreshToken refreshTask = null;
	public static void checkConnexion(Activity context, Callback callback) {
		WSLogin.context = context ;
		callBacks.add(callback);

		if(refreshTask == null) {
			refreshTask = new RefreshToken();
			refreshTask.execute();
			Log.d("WS","task is null");
		} else {
			Log.d("WS","task not null");
		}
	}

	private static class RefreshToken extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {

				Calendar lastAccess = PreferencesUtil.getLastTokenAccess();
				lastAccess.add(Calendar.DATE, 60);
				if(lastAccess.before(Calendar.getInstance())) {
					askForToken();
				} else {
					askForRefresh();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressLint("SetJavaScriptEnabled")
		private static void askForToken() {
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					WebViewClient webViewClient =  new WebViewClient() {

						public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){ handler.proceed(); } 

						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							if ( url.startsWith(WSBase.URL_REDIRECT) ) {
								if ( url.indexOf("code=") != -1 ) {
									PreferencesUtil.setCurrentRequestToken(retreiveRequestToken(url));
									new Thread(new Runnable() {
										@Override
										public void run() {
											askForRefresh();
										}
									}).start();
								}
								return true;
							}
							return super.shouldOverrideUrlLoading(view, url); 
						}

						public void onPageFinished(WebView webview, String url) {
							if ( url.startsWith(WSBase.URL_FORM_LOGIN) ) {
								Log.d("JS","load JS!");
								webview.loadUrl("javascript: document.forms['login'].elements['username'].value = 'admin';");
								webview.loadUrl("javascript: document.forms['login'].elements['password'].value = 'azerazer';");
								webview.loadUrl("javascript: document.forms['login'].submit();");
							} else if ( url.startsWith(WSBase.URL_FORM_ACCEPT) ) {
								Log.d("JS","load JS!");
								webview.loadUrl("javascript: document.forms['login'].elements['imt_oauth_server_authorize_allowAccess'].checked = true;");
								webview.loadUrl("javascript: document.forms['login'].submit();");
							}
						};
					};

					WebView webview = new WebView(context);
					webview.getSettings().setSavePassword(false);
					webview.setWebViewClient(webViewClient);
					webview.getSettings().setJavaScriptEnabled(true);
					String authorizationUri = createAuthorizationRequestUri();
					webview.loadUrl(authorizationUri);
				}
			});
		};

		private static void askForRefresh() {
			Exception resultEx = null;
			Calendar lastRefresh = PreferencesUtil.getLastTokenRefresh();
			lastRefresh.add(Calendar.MINUTE, 50);
			if(lastRefresh.before(Calendar.getInstance())) {
				HttpClient client = WSBase.createClient();

				String refreshToken = PreferencesUtil.getCurrentRefreshToken();
				String requestToken = PreferencesUtil.getCurrentRequestToken();

				StringBuilder redirectUri = new StringBuilder();
				redirectUri.append(WSBase.URL_REFRESH + "?");
				redirectUri.append("client_id=" + WSBase.CLIENT_ID);
				redirectUri.append("&client_secret=" + WSBase.CLIENT_SECRET);
				redirectUri.append("&redirect_uri=" + WSBase.URL_REDIRECT);
				if(refreshToken != null && refreshToken != "") {
					redirectUri.append("&grant_type=refresh_token");
					redirectUri.append("&refresh_token=" + refreshToken);
				} else {
					redirectUri.append("&grant_type=authorization_code");
					redirectUri.append("&code=" + requestToken);
				}


				try {
					URI website = new URI(redirectUri.toString());

					HttpGet request = new HttpGet();
					request.setURI(website);
					HttpResponse response = client.execute(request);
					String result = EntityUtils.toString(response.getEntity());

					String accessToken = retreiveAccessToken(result);
					refreshToken = retreiveRefreshToken(result);

					PreferencesUtil.setCurrentToken(refreshToken, accessToken);
					PreferencesUtil.setLastTokenRefresh(Calendar.getInstance());
				} catch (Exception e) {
					e.printStackTrace();
					resultEx = e;
				}
			}
			

			List<Callback> toRemove = new ArrayList<Callback>();
			toRemove.addAll(callBacks);
			refreshTask = null;
			callBacks = new ArrayList<Callback>();
			for (Callback callBack : toRemove) {
				callBack.done(resultEx);	
			}
		}

		private static String createAuthorizationRequestUri() {
			StringBuilder sb = new StringBuilder();
			sb.append(WSBase.URL_ACCESS);
			sb.append("?client_id="+WSBase.CLIENT_ID);
			sb.append("&redirect_uri="+WSBase.URL_REDIRECT);
			sb.append("&response_type=code");
			return sb.toString();
		}

		private static String retreiveRequestToken(String url) {
			return url.substring(url.lastIndexOf("=")+1);
		}

		private static String retreiveAccessToken(String result) {
			String res = "";
			try {
				JSONObject object = new JSONObject(result);
				res = (String)object.get("access_token");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		private static String retreiveRefreshToken(String result) {
			String res = "";
			try {
				JSONObject object = new JSONObject(result);
				res = (String)object.get("refresh_token");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}
	}
}

package its.my.time.data.ws;

import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WSLogin {

	private static String refreshToken;
	private static String accessToken;
	private static WebView webview;

	private static boolean isConnected = false;
	public static boolean isConnected() {
		return isConnected;
	}

	private static Callback callBack;

	private static class RefreshToken extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Calendar lastRefresh = PreferencesUtil.getLastTokenRefresh();
				lastRefresh.add(Calendar.MINUTE, 50);
				if(true) {
					HttpClient client = WSBase.createClient();

					refreshToken = PreferencesUtil.getCurrentRefreshToken();

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
						redirectUri.append("&code=" + accessToken);
					}


					URI website = new URI(redirectUri.toString());
					HttpGet request = new HttpGet();
					request.setURI(website);
					HttpResponse response = client.execute(request);
					String result = EntityUtils.toString(response.getEntity());

					accessToken = retreiveAccessToken(result);
					refreshToken = retreiveRefreshToken(result);

					PreferencesUtil.setCurrentToken(refreshToken, accessToken);
					PreferencesUtil.setLastTokenRefresh(Calendar.getInstance());
					Log.d("WS","Refresh done!");
				}
				isConnected = true;
				if(callBack != null) {
					callBack.done(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			new Handler().postDelayed(new Runnable() {				
				@Override public void run() {isConnected = false;}
			}, 3000000);
			super.onPostExecute(result);
		}
	}


	private static WebViewClient webViewClient =  new WebViewClient() {

		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){ handler.proceed(); } 

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if ( url.startsWith(WSBase.URL_REDIRECT) ) {
				if ( url.indexOf("code=") != -1 ) {
					accessToken = mExtractToken(url);
					PreferencesUtil.setCurrentToken("", accessToken);
					new RefreshToken().execute();
				}
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url); 
		}

		public void onPageFinished(WebView view, String url) {
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

	@SuppressLint("SetJavaScriptEnabled")
	public static void launchOAuth(final Activity context, Callback callBack) {
		WSLogin.callBack = callBack;
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				webview = new WebView(context);
				webview.setVisibility(View.VISIBLE);
				webview.getSettings().setSupportZoom(true);
				webview.getSettings().setSavePassword(false);
				webview.setWebViewClient(webViewClient);
				webview.getSettings().setJavaScriptEnabled(true);
				accessToken = PreferencesUtil.getCurrentAccessToken();
				if (accessToken == null || accessToken == "" || accessToken == "") {
					String authorizationUri = mReturnAuthorizationRequestUri();
					webview.loadUrl(authorizationUri);
				} else {
					new RefreshToken().execute();
				}
			}
		});


	}

	private static String mExtractToken(String url) {
		return url.substring(url.lastIndexOf("=")+1);
	}

	private static String mReturnAuthorizationRequestUri() {
		/*
		 * http://5.135.156.82/oauth/v2/auth?
	client_id=3_51ubfhpw9w084owsk0oogskgs840w0wg0g0scsgc4o08wk8w0k
	redirect_uri=
	response_type=code
		 */
		StringBuilder sb = new StringBuilder();
		sb.append(WSBase.URL_ACCESS);
		sb.append("?client_id="+WSBase.CLIENT_ID);
		sb.append("&redirect_uri="+WSBase.URL_REDIRECT);
		sb.append("&response_type=code");
		return sb.toString();
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

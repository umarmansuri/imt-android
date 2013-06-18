package its.my.time.data.ws;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.bdd.utilisateur.UtilisateurRepository;
import its.my.time.util.ConnectionManager;
import its.my.time.util.PreferencesUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WSLogin {

	private static List<Callback> callBacks = new ArrayList<Callback>();

	public static Context context;
	private static LoadToken refreshTask = null;

	public static void checkConnexion(final Context context, final Callback callback) {
		try {
			if(WSBase.context == null) {
				WSBase.context = context;
			}
			((Activity)WSBase.context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {

					if(!ConnectionManager.isOnline(context)) {
						Toast.makeText(context, "Vous n'êtes pas connecté à internet.", Toast.LENGTH_SHORT).show();
						if(callback != null) {
							callback.done(new Exception());
						}
						return;
					}
					
					UtilisateurBean bean = new UtilisateurRepository(WSBase.context).getById(PreferencesUtil.getCurrentUid());
					if(bean == null || bean.getId() <= 0) {
						checkConnexion(bean.getPseudo(), bean.getMdp(), WSBase.context, callback);
					} else {
						if(callback != null) {
							callback.done(new Exception());
						}
					}	
				}
			});
		} catch (Exception e) {}
	}

	public static void checkConnexion(String user, String pass, Context context, Callback callback) {
		if(!ConnectionManager.isOnline(context)) {
			Toast.makeText(context, "Vous n'êtes pas connecté à internet.", Toast.LENGTH_SHORT).show();
			return;
		}
		WSLogin.context = context ;
		callBacks.add(callback);

		if(refreshTask == null) {
			refreshTask = new LoadToken(user, pass);
			refreshTask.execute();
			Log.d("WS","task is null");
		} else {
			Log.d("WS","task not null");
		}
	}

	private static class LoadToken extends AsyncTask<Void, Void, Void> {

		private static String username;
		private static String pass;

		public LoadToken(String username, String pass) {
			LoadToken.username = username;
			LoadToken.pass = pass;
		}

		@Override
		public Void doInBackground(Void... params) {
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
			try {
				((Activity)context).runOnUiThread(new Runnable() {
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
							private int count = 0;
							public void onPageFinished(WebView webview, String url) { 
								webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

								if ( url.startsWith(WSBase.URL_FORM_LOGIN) ) {
									if(count >= 1) {
										webview.stopLoading();
										List<Callback> toRemove = new ArrayList<Callback>();
										toRemove.addAll(callBacks);
										refreshTask = null;
										callBacks = new ArrayList<Callback>();
										for (Callback callBack : toRemove) {
											callBack.done(new Exception());	
										}
										return;
									}
									count++;
									Log.d("JS","load JS!");
									webview.loadUrl("javascript: document.forms['login'].elements['username'].value = '" + username + "';");
									webview.loadUrl("javascript: document.forms['login'].elements['password'].value = '" + pass +"';");
									//webview.loadUrl("javascript: document.forms['login'].elements['username'].value = 'ad.hugon';");
									//webview.loadUrl("javascript: document.forms['login'].elements['password'].value = 'azerazer';");
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
						webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
						String authorizationUri = createAuthorizationRequestUri();

						webview.loadUrl(authorizationUri);
					}
				});
			} catch (Exception e){}		
		};

		static class MyJavaScriptInterface
		{
			@SuppressWarnings("unused")
			public void processHTML(String html)
			{
				Log.d("HTML",html);
			}
		}

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

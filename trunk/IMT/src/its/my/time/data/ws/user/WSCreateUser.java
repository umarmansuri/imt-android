package its.my.time.data.ws.user;

import its.my.time.data.bdd.utilisateur.UtilisateurBean;
import its.my.time.data.ws.Callback;
import its.my.time.data.ws.WSBase;
import android.app.Activity;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WSCreateUser extends AsyncTask<Void, Void, Void> {

	private Activity context;
	private UtilisateurBean user;
	private Callback callback;

	public WSCreateUser(Activity context, UtilisateurBean user, Callback callback) {
		this.context = context;
		this.user = user;
		this.callback = callback;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				WebViewClient webViewClient = new WebViewClient() {

					public void onReceivedSslError(WebView view,SslErrorHandler handler, SslError error) {handler.proceed();}

					@Override
					public boolean shouldOverrideUrlLoading(WebView view,String url) {return false;}

					public void onPageFinished(WebView webview, String url) {
						if (url.startsWith(WSBase.URL_FORM_CREATE)) {
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].elements['name'].value = '" + user.getNom() + "';");
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].elements['firstname'].value = '" + user.getPrenom() + "';");
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].elements['fos_user_registration_form_username'].value = '" + user.getPseudo() + "';");
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].elements['fos_user_registration_form_email'].value = '" + user.getMail() + "';");
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].elements['fos_user_registration_form_plainPassword_first'].value = '" + user.getMdp() + "';");
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].elements['fos_user_registration_form_plainPassword_second'].value = '" + user.getMdp() + "';");
							webview.loadUrl("javascript: document.getElementsByTagName('form')[1].submit();");
						} else if (url.startsWith("https://app.my-time.fr/register/confirmed")) {
							if(callback != null) {
								context.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										callback.done(null);
									}
								});
							}
						} else if (url.startsWith("https://app.my-time.fr/register")) {
							callback.done(new Exception());
						}
					
					};
				};

				WebView webview = new WebView(context);
				webview.getSettings().setSavePassword(false);
				webview.setWebViewClient(webViewClient);
				webview.getSettings().setJavaScriptEnabled(true);
				webview.loadUrl(WSBase.URL_FORM_CREATE);
			}
		});
		return null;
	}	
}
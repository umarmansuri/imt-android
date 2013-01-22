package its.my.time.data.ws;

import its.my.time.util.PreferencesUtil;
import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OAuthResponseActivity extends Activity {

	protected final static String CLIENT_ID = "";
	protected final static String CLIENT_SECRET = "";
	protected final static String REDIRECT_URL = "mytime://oauthresponse";
	protected final static String AUTHENTICATION_URL = "";
	
	@Override
	protected void onNewIntent(Intent intent){
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(REDIRECT_URL))
		{
			String code = uri.getQueryParameter("code");
			
			OAuthClientRequest request = null;

			try {
				request = OAuthClientRequest.tokenLocation("<service request URL>")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
				   .setClientId(CLIENT_ID)
					.setClientSecret(CLIENT_SECRET)
					.setRedirectURI(REDIRECT_URL)
					.setCode(code)
					.buildBodyMessage();
				

				OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
				OAuthJSONAccessTokenResponse response = oAuthClient.accessToken(request);
				String token = response.getAccessToken();
				PreferencesUtil.setAccesToken(this, token);
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			} catch (OAuthProblemException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	public static class Connexion {

		public static void connect(Context context) {
			OAuthClientRequest request = null;
			try {
				request = OAuthClientRequest
						.authorizationLocation(AUTHENTICATION_URL)
						.setClientId(CLIENT_ID).setRedirectURI(REDIRECT_URL)
						.buildQueryMessage();
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			}

			Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(request.getLocationUri() + "&response_type=code"));
			context.startActivity(intent);
		}
	}

}

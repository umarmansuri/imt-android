package its.my.time.pages;

import its.my.time.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends Activity {

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("A Propos - My Time");
		
		final TextView message = (TextView) findViewById(R.id.activity_apropos_editText_Title);
		message.setLinksClickable(true);
		message.setMovementMethod(LinkMovementMethod.getInstance());
	    message.setText(Html.fromHtml("L'application android My Time est une application d'agenda collaboratif basée sur le site web My-Time.fr : <a href=\"http://www.my-time.fr\">www.my-time.fr</a>"));
	}
}

package its.my.time.view.wysiwyg;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class Wysiwyg extends FrameLayout {

	private WebView webView;

	public Wysiwyg(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Wysiwyg(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Wysiwyg(Context context) {
		super(context);
		init();
	}

	private void init() {
		webView = new WebView(getContext());
		webView.getSettings().setJavaScriptEnabled(true);
		addView(webView);
		webView.loadUrl("file:///android_asset/wysiwyg.html");
	}

	
}

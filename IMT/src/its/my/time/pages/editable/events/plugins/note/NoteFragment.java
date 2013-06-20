package its.my.time.pages.editable.events.plugins.note;

import its.my.time.data.bdd.events.plugins.note.NoteBean;
import its.my.time.data.bdd.events.plugins.note.NoteRepository;
import its.my.time.pages.editable.events.plugins.BasePluginFragment;
import its.my.time.util.PreferencesUtil;
import its.my.time.view.WebviewDisabled;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NoteFragment extends BasePluginFragment {

	private NoteBean noteBean;
	private NoteRepository noteRepo;
	private WebviewDisabled mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		int eid = getParentActivity().getEvent().getId();
		long uid = PreferencesUtil.getCurrentUid();
		
		noteRepo = new NoteRepository(getActivity());
		noteBean = noteRepo.getByUidEid(eid, uid);
		
		if(noteBean.getEid() < 0) {
			noteBean = new NoteBean();
			noteBean.setEid(eid);
			noteBean.setName("Note");
			noteBean.setUid(uid);
			noteBean.setHtml("");
		} 

		mWebView = new WebviewDisabled(getActivity());
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				String content = noteBean.getHtml();
				content = content.replace("'", "\\'");
				content = content.replace("\"","\\\"");
				String load = "javascript:document.getElementById('mce_0_ifr').contentWindow.document.body.innerHTML = \"" + content + "\"";
				mWebView.loadUrl(load);
				mWebView.loadUrl("javascript:document.getElementById('trick').click();");
				mWebView.setVisibility(View.VISIBLE);
			}
		});
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setLoadsImagesAutomatically(true);
		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		
		mWebView.getSettings().setBuiltInZoomControls(false);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.loadUrl("file:///android_asset/tinymce/wysiwyg.html");
		mWebView.requestFocus(View.FOCUS_DOWN);
		mWebView.setEnabled(false);
		mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

		return mWebView;
	}
	
	class MyJavaScriptInterface
	{
	    public void processHTML(String html)
	    {
	    	noteBean.setHtml(html);
	    	Log.d("Webview","save html: " + html);
			if(noteBean.getId() > -1) {
				noteRepo.update(noteBean);	
			} else {
				noteBean.setId((int)noteRepo.insert(noteBean));
			}
	    }
	}
	
	@Override
	public String getTitle() {
		return "Notes";
	}

	@Override
	public void launchEdit() {
		mWebView.setEnabled(true);
		mWebView.loadUrl("javascript:document.getElementById('mce_0_ifr').click();");
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mWebView, InputMethodManager.SHOW_FORCED);
		super.launchEdit();
	}

	@Override
	public void launchSave() {
		mWebView.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('mce_0_ifr').contentWindow.document.body.innerHTML);");
		mWebView.loadUrl("javascript:document.getElementById('trick').click();");
		mWebView.setEnabled(false);
		super.launchSave();
	}

	@Override
	public void launchCancel() {
		String content = noteBean.getHtml();
		content = content.replace("'", "\\'");
		content = content.replace("\"","\\\"");
		String load = "javascript:document.getElementById('mce_0_ifr').contentWindow.document.body.innerHTML = \"" + content + "\"";
		mWebView.loadUrl(load);
		mWebView.loadUrl("javascript:document.getElementById('trick').click();");
		mWebView.setEnabled(false);
		super.launchCancel();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}

	@Override
	public boolean isSavable() {
		return true;
	}
}

package kr.ac.hyu.kangdaecheol.calendar.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

@EActivity(resName = "activity_6_about_webview")
public class Activity_6_About_WebView extends Activity {
	
	@ViewById
	WebView webview;
	@ViewById
	ProgressBar progressBar;
	
	@SystemService
	InputMethodManager mInputMethodManager;

	@Extra
	String url;
	
	@SuppressLint("SetJavaScriptEnabled") 
	@AfterViews
	protected void init() {
		webview.setWebChromeClient(new webViewChrome());
		webview.setWebViewClient(new webViewClient());
		
		WebSettings mWebSettings = webview.getSettings();
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setSupportZoom(true);
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setUseWideViewPort(true);
		
		webview.setInitialScale(1);
		webview.loadUrl(url);
	}

	class webViewChrome extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress < 100) {
				progressBar.setProgress(newProgress);
			} else {
				progressBar.setVisibility(View.INVISIBLE);
				progressBar.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			}
		}
	}

	class webViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progressBar.setVisibility(View.VISIBLE);
			progressBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 15));
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

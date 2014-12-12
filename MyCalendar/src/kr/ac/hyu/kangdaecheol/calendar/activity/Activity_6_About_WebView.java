package kr.ac.hyu.kangdaecheol.calendar.activity;

import org.androidannotations.annotations.EActivity;

import kr.ac.hyu.kangdaecheol.calendar.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

	private WebView mWebView;
	private WebSettings mWebSettings;
	private ProgressBar mProgressBar;
	private InputMethodManager mInputMethodManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_6_about_webview);

		Intent i = getIntent();

		mWebView = (WebView) findViewById(R.id.webview);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

		mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		mWebView.setWebChromeClient(new webViewChrome());
		mWebView.setWebViewClient(new webViewClient());
		mWebSettings = mWebView.getSettings();
		mWebSettings.setBuiltInZoomControls(true);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(null);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.setInitialScale(1);

		mWebView.loadUrl(i.getStringExtra("url"));
	}

	class webViewChrome extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// 현제 페이지 진행사항을 ProgressBar를 통해 알린다.
			if (newProgress < 100) {
				mProgressBar.setProgress(newProgress);
			} else {
				mProgressBar.setVisibility(View.INVISIBLE);
				mProgressBar
						.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			}
		}
	}

	class webViewClient extends WebViewClient {

		// Loading이 시작되면 ProgressBar처리를 한다.
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			mProgressBar.setVisibility(View.VISIBLE);
			mProgressBar.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 15));
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mWebSettings.setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
		}
	}

	// http://를 체크하여 추가한다.
	private String httpInputCheck(String url) {
		if (url.isEmpty())
			return null;

		if (url.indexOf("http://") == ("http://").length())
			return url;
		else
			return "http://" + url;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

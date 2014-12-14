package kr.ac.hyu.kangdaecheol.calendar.activity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.setting.Setting_Variables;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;

@EActivity(resName = "activity_6_about")
public class Activity_6_About extends Activity {

	@Click(resName="phone")
	void onclickPhone(){
		Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Setting_Variables.Developer_Num));
		startActivity(i);
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="email")
	void onClickEmail(){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { Setting_Variables.Developer_Email });
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
		startActivity(Intent.createChooser(intent, getString(R.string.email_send)));
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="webview")
	void onClickWebview(){
		Activity_6_About_WebView_.intent(this).url(Setting_Variables.DCSLab).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Activity_Main_.intent(this).start();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

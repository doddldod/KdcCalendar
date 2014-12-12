package kr.ac.hyu.kangdaecheol.calendar.activity;

import org.androidannotations.annotations.EActivity;

import kdc.hanyang.setting.Setting_Variables;
import kr.ac.hyu.kangdaecheol.calendar.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@EActivity(resName = "activity_6_about")
public class Activity_6_About extends Activity {

	TextView About_TV_Developer_Num;
	TextView About_TV_Developer_Email;
	TextView About_TV_MRLab;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_6_about);

		About_TV_Developer_Num = (TextView) findViewById(R.id.About_TV_Developer_Num);
		About_TV_Developer_Email = (TextView) findViewById(R.id.About_TV_Developer_Email);
		About_TV_MRLab = (TextView) findViewById(R.id.About_TV_MRLab);

		About_TV_Developer_Num.setOnClickListener(Listener_About);
		About_TV_Developer_Email.setOnClickListener(Listener_About);
		
		About_TV_MRLab.setOnClickListener(Listener_About);

	}

	Button.OnClickListener Listener_About = new View.OnClickListener() {
		public void onClick(View v) {
			Intent i;
			switch (v.getId()) {
			case R.id.About_TV_Developer_Num:
				i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ Setting_Variables.Developer_Num));
				startActivity(i);
				break;
			case R.id.About_TV_Developer_Email:
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { Setting_Variables.Developer_Email });
				intent.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.email_subject));
				intent.putExtra(Intent.EXTRA_TEXT,
						getString(R.string.email_body));
				startActivity(Intent.createChooser(intent,
						getString(R.string.email_send)));
				break;
	
			 case R.id.About_TV_MRLab:
			 i = new Intent(Activity_6_About.this, Activity_6_About_WebView.class);
			 i.putExtra("url", Setting_Variables.DCSLab);
			 startActivity(i);
			 break;
			default:
				break;
			}

		}

	};

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public synchronized void onResume() {
		super.onResume();

	}

	private void setLayout() {

	}

	@Override
	public synchronized void onPause() {
		super.onPause();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Activity_Main_.intent(this).start();
			this.overridePendingTransition(R.anim.fade_in,
					R.anim.fade_out);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

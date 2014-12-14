package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.setting.Setting_Variables;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

@EActivity(resName = "activity_main")
public class Activity_Main extends Activity {

	private Handler mHandler;
	private boolean mFlag = false;
	private Toast mToast;

	@Override
	protected void onResume() {
		super.onResume();
		setLayout();
	}

	@SuppressLint("HandlerLeak") 
	public void setLayout() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					mFlag = false;
				}
			}
		};
	}

	
	@Click(resName="chooseCalendar")
	void onClickChooseCalendar() {
		Activity_1_ChooseCalendar_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="board")
	void onClickBoard() {
		Activity_4_Board_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="email")
	void onClickEmail() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { Setting_Variables.Developer_Email });
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
		startActivity(Intent.createChooser(intent, getString(R.string.email_send)));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="about")
	void onClickAbout() {
		Activity_6_About_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Click(resName="fileIO")
	void onClickFileIO() {
	}
	
	@Click(resName="upComing")
	void onClickUpComing() {
	}
	
	public void showToast(String mText) {
		if (mText != null) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT);
			} else {
				mToast.setText(mText);
			}
			mToast.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!mFlag) {
				showToast(getString(R.string.str_exit));
				mFlag = true;
				mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}

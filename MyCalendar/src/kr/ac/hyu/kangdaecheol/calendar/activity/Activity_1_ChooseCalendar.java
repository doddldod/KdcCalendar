package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.view.KeyEvent;

@EActivity(resName="activity_cal_main")
public class Activity_1_ChooseCalendar extends Activity {
	
	
	@Click(resName="monthly")
	void onMonth() {
		Activity_1_Monthly_.intent(this).start();
	}

	@Click(resName="weekly")
	void onWeekly() {
		Activity_1_Weekly_.intent(this).start();
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

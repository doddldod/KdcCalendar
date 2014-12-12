package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.view.KeyEvent;

@EActivity(resName="activity_cal_main")
public class Cal_MainActivity extends Activity {
	
	
	@Click(resName="monthly")
	void onMonth() {
		MonthlyActivity_.intent(this).start();
	}

	@Click(resName="weekly")
	void onWeekly() {
		WeeklyActivity_.intent(this).start();
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

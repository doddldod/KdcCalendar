package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
	
	
	@Click(R.id.monthly)
	void onMonth() {
		MonthlyActivity_.intent(this).start();
	}

	@Click(R.id.weekly)
	void onWeekly() {
		WeeklyActivity_.intent(this).start();
	}
	
}

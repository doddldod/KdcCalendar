package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

@EActivity(resName = "activity_cal_main")
public class Activity_1_ChooseCalendar extends Activity {
	@Bean
	DatabaseManager databaseManager;

	@Click(resName = "monthly")
	void onMonth() {
		Activity_1_Monthly_.intent(this).start();
	}

	@Click(resName = "weekly")
	void onWeekly() {
		Activity_1_Weekly_.intent(this).start();
	}

	@Click(resName = "repeat")
	void onRepeat() {
		Activity_1_AddSchedule_Repeat_.intent(this).start();
	}

	@Click(resName = "clear")
	void onClear() {
		List<Schedule> list = databaseManager
				.getAllScheduleList(new Date());
		for ( int i = 0 ; i < list.size(); i ++)
		{
			databaseManager.deleteSchedule(list.get(i));
		}
		Toast.makeText(getApplicationContext(), getString(R.string.dbclear),
				Toast.LENGTH_SHORT).show();
	}

	@Click(resName = "back")
	protected void onClickBack() {
		Activity_Main_.intent(this).start();
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Activity_Main_.intent(this).start();
			this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

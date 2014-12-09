package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.activity_schedule)
public class ScheduleActivity extends Activity {
	
	@Extra
	int year;
	@Extra
	int month;
	@Extra
	int day;
	
	@ViewById
	TextView title;
	@ViewById
	TextView empty;
	
	@ViewById
	ListView list;
	
	@AfterViews
	protected void init() {
		title.setText(String.format(getString(R.string.scheduleTitle), year, month+1, day));
	}
	
	@Click(R.id.back)
	protected void onClickBack() {
		finish();
	}
	
}

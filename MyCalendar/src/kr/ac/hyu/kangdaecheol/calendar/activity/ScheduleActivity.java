package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.adapter.ScheduleAdapter;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.View;
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
	ListView listView;
	
	@Bean
	DatabaseManager databaseManager;
	
	@Bean
	ScheduleAdapter scheduleAdapter;
	
	@AfterViews
	protected void init() {
		title.setText(String.format(getString(R.string.scheduleTitle), year, month+1, day));
		
		List<Schedule> list = databaseManager.getScheduleListByDate(getDate());
		if (list != null && list.size() > 0) {
			empty.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			
			scheduleAdapter.setScheduleList(list);
			listView.setAdapter(scheduleAdapter);
		} else {
			empty.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}
	}
	
	private Date getDate() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month, day, 0, 0);
		return cal.getTime();
	}
	
	@Click(R.id.back)
	protected void onClickBack() {
		finish();
	}
	
}

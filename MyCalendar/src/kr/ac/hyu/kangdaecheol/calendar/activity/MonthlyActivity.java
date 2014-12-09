package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.util.Calendar;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.adapter.CalendarAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.GridView;
import android.widget.TextView;

@EActivity(R.layout.activity_monthly)
public class MonthlyActivity extends Activity {
	
	@ViewById
	TextView monthText;
	@ViewById
	GridView calendar;
	
	@Bean
	CalendarAdapter calendarAdapter;

	private int year;
	private int month;
	
	@AfterViews
	protected void init() {
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		
		calendarAdapter.setDayList(year, month);
		calendar.setAdapter(calendarAdapter);
		setMonthText();
	}
	
	@Override
	protected void onResume() {
		calendarAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
	private void setMonthText() {
		monthText.setText(String.format(getString(R.string.monthText), year, month+1));
	}
	
	@Click(R.id.prevMonth)
	void onPrev() {
		if(month == 0){
			year--;
			month = 11;
		} else {
			month--;
		}
		calendarAdapter.setDayList(year, month);
		calendarAdapter.notifyDataSetChanged();
		setMonthText();
	}

	@Click(R.id.nextMonth)
	void onNext() {
		if(month == 11){
			year++;
			month = 0;
		} else {
			month++;
		}
		calendarAdapter.setDayList(year, month);
		calendarAdapter.notifyDataSetChanged();
		setMonthText();
	}
	
	@Click(R.id.add)
	void onAdd() {
		AddScheduleActivity_.intent(this).start();
	}
	
	@Click(R.id.back)
	protected void onClickBack() {
		finish();
	}
	
}

package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.adapter.ScheduleAdapter;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.eventbus.MyBus;
import kr.ac.hyu.kangdaecheol.calendar.eventbus.UpdateScheduleEvent;
import kr.ac.hyu.kangdaecheol.calendar.model.Day;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

@EActivity(resName="activity_weekly")
public class WeeklyActivity extends Activity {
	
	@ViewById
	TextView weekText;

	private int nowYear;
	private int nowMonth;
	private int nowDay;
	
	private Calendar cal;
	
	private List<Day> dayList;
	
	int nowWeekNum;
	int lastWeekNum;
	
	@ViewById
	TextView empty;
	@ViewById
	ListView listView;
	
	@Bean
	DatabaseManager databaseManager;
	@Bean
	ScheduleAdapter scheduleAdapter;
	@Bean
	MyBus bus;
	
	@AfterViews
	protected void init() {
		bus.register(this);
		Calendar cal = Calendar.getInstance();
		nowYear = cal.get(Calendar.YEAR);
		nowMonth = cal.get(Calendar.MONTH);
		nowDay = cal.get(Calendar.DAY_OF_MONTH);
		
		setCalendar();
		addFrontEmptyDay();
		addCalendarDay();
		addEndEmptyDay();
		findWeekNum();
		lastWeekNum();
		setList();
		setMonthText();
	}
	
	@Override
	protected void onResume() {
		scheduleAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		bus.unregister(this);
		super.onDestroy();
	}
	
	private void setList() {
		List<Schedule> list = databaseManager.getWeeklyScheduleList(getStartDate(), getEndDate());
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
	
	private void setMonthText() {
		weekText.setText(String.format(getString(R.string.weekText), nowYear, nowMonth+1, nowWeekNum));
	}
	
	private void setCalendar() {
		dayList = new ArrayList<>();
		cal = Calendar.getInstance();
		cal.set(nowYear, nowMonth, 1);
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
	}
	
	private int getDayofWeek() {
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	private int getLastDay() {
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	private void addCalendarDay() {
		for (int i = 1; i <= getLastDay(); i++) {
			dayList.add(new Day(nowYear, nowMonth, i));
		}
	}

	private void addFrontEmptyDay() {
		for (int i = 0; i < getDayofWeek(); i++) {
			dayList.add(new Day());
		}
	}
	
	private void addEndEmptyDay() {
		int remainCardsetCount = dayList.size() % 7;
		if (remainCardsetCount != 0) {
			for (int i = 0; i < 7 - remainCardsetCount; i++) {
				dayList.add(new Day());
			}
		}
	}
	
	private void findWeekNum() {
		for(int i=0; i<dayList.size(); i++) {
			Day day = dayList.get(i);
			if(day.getDay() == nowDay){
				nowWeekNum = i / 7 + 1;
			}
		}
	}
	
	private void lastWeekNum() {
		lastWeekNum = dayList.size() / 7;
	}
	
	private Date getStartDate() {
		for(int i=0; i<7; i++) {
			int day = dayList.get((nowWeekNum-1)*7+i).getDay();
			if(day != 0) {
				return getDate(day);
			}
		}
		return null;
	}
	
	private Date getEndDate() {
		for(int i=6; i>=0; i--) {
			int day = dayList.get((nowWeekNum-1)*7+i).getDay();
			if(day != 0) {
				return getDate(day);
			}
		}
		return null;
	}
	
	private Date getDate(int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(nowYear, nowMonth, day, 0, 0);
		return cal.getTime();
	}
	
	@Click(resName="prevWeek")
	void onPrev() {
		if(nowWeekNum == 1) {
			if(nowMonth == 0){
				nowYear--;
				nowMonth = 11;
			} else {
				nowMonth--;
			}
			setCalendar();
			addFrontEmptyDay();
			addCalendarDay();
			addEndEmptyDay();
			lastWeekNum();
			nowWeekNum = lastWeekNum;
		} else {
			nowWeekNum--;
		}
		setList();
		setMonthText();
	}

	@Click(resName="nextWeek")
	void onNext() {
		if(nowWeekNum == lastWeekNum) {
			if(nowMonth == 11){
				nowYear++;
				nowMonth = 0;
			} else {
				nowMonth++;
			}
			nowWeekNum = 1;
			setCalendar();
			addFrontEmptyDay();
			addCalendarDay();
			addEndEmptyDay();
			lastWeekNum();
		} else {
			nowWeekNum++;
		}
		setList();
		setMonthText();
	}
	
	@Click(resName="add")
	void onAdd() {
		AddScheduleActivity_.intent(this).start();
	}
	
	@Click(resName="back")
	protected void onClickBack() {
		finish();
	}
	
	@Subscribe
	public void onUpdateSchedule(UpdateScheduleEvent event) {
		setList();
	}
	
}

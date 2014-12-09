package kr.ac.hyu.kangdaecheol.calendar.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.adapter.view.CalendarItemView;
import kr.ac.hyu.kangdaecheol.calendar.adapter.view.CalendarItemView_;
import kr.ac.hyu.kangdaecheol.calendar.model.Day;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class CalendarAdapter extends BaseAdapter {

	@RootContext
	protected Context context;

	List<Day> dayList;

	private Calendar cal;

	private int year;
	private int month;
	
	public void setDayList(int year, int month) {
		this.year = year;
		this.month = month;
		dayList = new ArrayList<>();
		
		setCalendar();
		addFrontEmptyDay();
		addCalendarDay();
		addEndEmptyDay();
	}
	
	private void setCalendar() {
		cal = Calendar.getInstance();
		cal.set(year, month, 1);
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
			dayList.add(new Day(year, month, i));
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

	@Override
	public int getCount() {
		return dayList.size();
	}

	@Override
	public Day getItem(int position) {
		return dayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarItemView calendarItemView;
		if (convertView == null) {
			calendarItemView = CalendarItemView_.build(context);
		} else {
			calendarItemView = (CalendarItemView) convertView;
		}

		calendarItemView.bind(getItem(position));

		return calendarItemView;
	}

}

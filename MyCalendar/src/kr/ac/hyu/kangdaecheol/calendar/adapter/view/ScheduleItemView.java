package kr.ac.hyu.kangdaecheol.calendar.adapter.view;

import java.util.Date;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.schedule_item)
public class ScheduleItemView extends LinearLayout {

	@ViewById
	TextView startDate;
	@ViewById
	TextView endDate;
	@ViewById
	TextView contents;
	
	@Bean
	DatabaseManager databaseManager;
	
	Context context;

	public ScheduleItemView(Context context) {
		super(context);
		this.context = context;
	}

	public void bind(Schedule schedule) {
		startDate.setText(getStringFormatedDate(schedule.getStartDate()));
		endDate.setText(getStringFormatedDate(schedule.getEndDate()));
		contents.setText(schedule.getContents());
	}
	
	private CharSequence getStringFormatedDate(Date date) {
		return DateFormat.format("yyyy. MM. dd hh:mm", date);
	}

}
package kr.ac.hyu.kangdaecheol.calendar.adapter.view;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(R.layout.schedule_item)
public class ScheduleItemView extends LinearLayout {

	@ViewById
	TextView date;
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
		date.setText(context.getString(R.string.scheduleDate, 
				schedule.getYear(), 
				schedule.getMonth(), 
				schedule.getDay(),
				schedule.getHour(),
				schedule.getMinute()));
		contents.setText(schedule.getContents());
	}

}
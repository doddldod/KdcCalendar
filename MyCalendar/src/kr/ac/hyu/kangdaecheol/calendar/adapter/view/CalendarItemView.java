package kr.ac.hyu.kangdaecheol.calendar.adapter.view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.activity.*;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.MyDate;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(resName="calendar_item")
public class CalendarItemView extends LinearLayout {

	@ViewById
	TextView dayText;
	@ViewById
	TextView status;
	
	@Bean
	DatabaseManager databaseManager;

	public CalendarItemView(Context context) {
		super(context);
	}

	public void bind(final MyDate day) {
		int dayInt = day.getDay();
		if (dayInt != 0) {
			dayText.setText("" + dayInt);
			setBackgroundResource(R.drawable.calendar_datebox_selector);
			
			setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Activity_1_ScheduleList_.intent(getContext())
						.year(day.getYear())
						.month(day.getMonth())
						.day(day.getDay())
						.start();
				}
			});
			
			List<Schedule> list = databaseManager.getScheduleListByDate(getDate(day));
			if (list != null && list.size() > 0) {
				status.setText("v");
			} else {
				status.setText("");
			}
		} else {
			dayText.setText("");
			status.setText("");
			setBackgroundResource(R.drawable.datebox_default_off);
		}
	}
	
	private Date getDate(MyDate day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(day.getYear(), day.getMonth(), day.getDay(), 0, 0);
		return cal.getTime();
	}

}
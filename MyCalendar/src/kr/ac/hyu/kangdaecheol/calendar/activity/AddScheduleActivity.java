package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.util.Calendar;
import java.util.Date;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

@EActivity(R.layout.activity_add_schedule)
public class AddScheduleActivity extends Activity {
	
	@ViewById
	EditText editText;
	@ViewById
	DatePicker startDatePicker;
	@ViewById
	TimePicker startTimePicker;
	@ViewById
	DatePicker endDatePicker;
	@ViewById
	TimePicker endTimePicker;
	
	@Bean
	DatabaseManager databaseManager;
	
	@Click(R.id.back)
	protected void onClickBack() {
		finish();
	}
	
	@Click(R.id.add)
	protected void onAdd() {
		if (isTextLenNotZero()){
			if(isValidStartEndDate()) {
				addSchedule();
			} else {
				Toast.makeText(this, getString(R.string.scheduleStartOverEnd), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.scheduleTextNull), Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean isTextLenNotZero() {
		return editText.getText().length() > 0;
	}
	
	private boolean isValidStartEndDate() {
		long startTimeMil = getStartDate().getTime() + getStartTime().getTime();
		long endTimeMil = getEndDate().getTime() + getEndTime().getTime();
		return startTimeMil < endTimeMil;
	}
	
	private void addSchedule() {
		Schedule schedule = new Schedule();
		schedule.setStartDate(getStartDate());
		schedule.setStartTime(getStartTime());
		schedule.setEndDate(getEndDate());
		schedule.setEndTime(getEndTime());
		schedule.setContents(editText.getText().toString());
		databaseManager.addSchedule(schedule);
		Toast.makeText(this, getString(R.string.addedMsg), Toast.LENGTH_SHORT).show();
		finish();
	}
	
	private Date getStartDate() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(startDatePicker.getYear(), 
				startDatePicker.getMonth(), 
				startDatePicker.getDayOfMonth(), 
				0, 0);
		return cal.getTime();
	}
	
	private Date getStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(0, 0, 0, 
				startTimePicker.getCurrentHour(), 
				startTimePicker.getCurrentMinute());
		return cal.getTime();
	}

	private Date getEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(endDatePicker.getYear(), 
				endDatePicker.getMonth(), 
				endDatePicker.getDayOfMonth(), 
				0, 0);
		return cal.getTime();
	}
	
	private Date getEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(0, 0, 0, 
				endTimePicker.getCurrentHour(), 
				endTimePicker.getCurrentMinute());
		return cal.getTime();
	}
	
}

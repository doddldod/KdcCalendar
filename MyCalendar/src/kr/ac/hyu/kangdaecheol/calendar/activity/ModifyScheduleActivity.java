package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.util.Calendar;
import java.util.Date;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@EActivity(resName="activity_modify_schedule")
public class ModifyScheduleActivity extends Activity {

	@Extra
	int id;

	@ViewById
	EditText editText;
	@ViewById
	TextView startDate;
	@ViewById
	TextView startTime;
	@ViewById
	TextView endDate;
	@ViewById
	TextView endTime;

	@Bean
	DatabaseManager databaseManager;
	
	private Schedule schedule;

	@AfterViews
	protected void init() {
		schedule = databaseManager.getScheduleByid(id);
		editText.setText(schedule.getContents());
		startDate.setText(getStringFormatedDate(schedule.getStartDate()));
		startTime.setText(getStringFormatedTime(schedule.getStartTime()));
		endDate.setText(getStringFormatedDate(schedule.getEndDate()));
		endTime.setText(getStringFormatedTime(schedule.getEndTime()));
	}

	private CharSequence getStringFormatedDate(Date date) {
		return DateFormat.format("yyyy. MM. dd", date);
	}

	private CharSequence getStringFormatedTime(Date date) {
		return DateFormat.format("hh:mm", date);
	}

	@Click(resName="back")
	protected void onClickBack() {
		finish();
	}

	@Click(resName="modify")
	protected void onModify() {
		if (isTextLenNotZero()) {
			if (isValidStartEndDate()) {
				mpdifySchedule();
			} else {
				Toast.makeText(this, getString(R.string.scheduleStartOverEnd), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.scheduleTextNull), Toast.LENGTH_SHORT).show();
		}
	}

	@Click(resName="startDate")
	protected void onStartDate() {
		showDateDialog(schedule.getStartDate(), myStartDateListener);
	}
	
	@Click(resName="endDate")
	protected void onEndDate() {
		showDateDialog(schedule.getEndDate(), myEndDateListener);
	}
	
	@Click(resName="startTime")
	protected void onStartTime() {
		showTimeDialog(schedule.getStartTime(), myStartTimeSetListener);
	}

	@Click(resName="endTime")
	protected void onEndTime() {
		showTimeDialog(schedule.getStartTime(), myEndTimeSetListener);
	}
	
	private boolean isTextLenNotZero() {
		return editText.getText().length() > 0;
	}

	private boolean isValidStartEndDate() {
		long startTimeMil = schedule.getStartDate().getTime() + schedule.getStartTime().getTime();
		long endTimeMil = schedule.getEndDate().getTime() + schedule.getEndTime().getTime();
		return startTimeMil < endTimeMil;
	}

	private void mpdifySchedule() {
		schedule.setContents(editText.getText().toString());
		databaseManager.updateSchedule(schedule);
		Toast.makeText(this, getString(R.string.modifyMsg), Toast.LENGTH_SHORT).show();
		finish();
	}
	
	private void showDateDialog(Date date, DatePickerDialog.OnDateSetListener onDateSetListener) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		
		Dialog dlgDate = new DatePickerDialog(this, onDateSetListener, 
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH));
		dlgDate.show();
	}

	private void showTimeDialog(Date date, TimePickerDialog.OnTimeSetListener myTimeSetListener) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		
		Dialog dlgTime = new TimePickerDialog(this, myTimeSetListener, 
				cal.get(Calendar.HOUR),
				cal.get(Calendar.MINUTE), 
                false);
        dlgTime.show();
	}

	private Date getDate(int year, int monthOfYear, int dayOfMont) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, monthOfYear, dayOfMont, 0, 0);
		return cal.getTime();
	}

	private Date getTime(int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(0, 0, 0, hourOfDay, minute);
		return cal.getTime();
	}

	private DatePickerDialog.OnDateSetListener myStartDateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			schedule.setStartDate(getDate(year, monthOfYear, dayOfMonth));
			startDate.setText(getStringFormatedDate(schedule.getStartDate()));
		}
	};
	
	private DatePickerDialog.OnDateSetListener myEndDateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			schedule.setEndDate(getDate(year, monthOfYear, dayOfMonth));
			endDate.setText(getStringFormatedDate(schedule.getEndDate()));
		}
	};
	
	private TimePickerDialog.OnTimeSetListener myStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			schedule.setStartTime(getTime(hourOfDay, minute));
			startTime.setText(getStringFormatedTime(schedule.getStartTime()));
		}
	};

	private TimePickerDialog.OnTimeSetListener myEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			schedule.setEndTime(getTime(hourOfDay, minute));
			endTime.setText(getStringFormatedTime(schedule.getEndTime()));
		}
	};

}

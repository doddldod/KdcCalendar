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

@EActivity(resName = "activity_add_schedule")
public class Activity_1_AddSchedule extends Activity {

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

	int start_year;
	int start_monthOfYear;
	int start_dayOfMonth;
	int start_hourOfDay;
	int start_minute;

	int end_year;
	int end_monthOfYear;
	int end_dayOfMonth;
	int end_hourOfDay;
	int end_minute;

	@Bean
	DatabaseManager databaseManager;

	private Schedule schedule;

	@AfterViews
	protected void init() {
		schedule = new Schedule();
		schedule.setStartDate(getInitDate());
		schedule.setEndDate(getInitDate());
		// schedule.setStartTime(getInitTime());
		// schedule.setEndTime(getInitTime());
	}

	@Click(resName = "back")
	protected void onClickBack() {
		finish();
	}

	@Click(resName = "add")
	protected void onAdd() {
		if (isTextLenNotZero()) {
			if (isAllDateNotNull()) {
				if (isValidStartEndDate()) {
					addSchedule();
				} else {
					Toast.makeText(this,
							getString(R.string.scheduleStartOverEnd),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, getString(R.string.scheduleDateNull),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, getString(R.string.scheduleTextNull),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Click(resName = "startDate")
	protected void onStartDate() {
		showDateDialog(schedule.getStartDate(), myStartDateListener);
	}

	@Click(resName = "endDate")
	protected void onEndDate() {
		showDateDialog(schedule.getEndDate(), myEndDateListener);
	}

	@Click(resName = "startTime")
	protected void onStartTime() {
		showTimeDialog(schedule.getStartDate(), myStartTimeSetListener);
	}

	@Click(resName = "endTime")
	protected void onEndTime() {
		showTimeDialog(schedule.getEndDate(), myEndTimeSetListener);
	}

	private Date getInitDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), 0, 0);
		return cal.getTime();
	}

	private boolean isTextLenNotZero() {
		return editText.getText().length() > 0;
	}

	private boolean isAllDateNotNull() {
		if (schedule.getStartDate() == null || schedule.getEndDate() == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidStartEndDate() {
		long startTimeMil = schedule.getStartDate().getTime();
		long endTimeMil = schedule.getEndDate().getTime();
		return startTimeMil <= endTimeMil;
	}

	private void addSchedule() {
		schedule.setContents(editText.getText().toString());

		databaseManager.addSchedule(schedule);
		Toast.makeText(this, getString(R.string.addedMsg), Toast.LENGTH_SHORT)
				.show();
		finish();
	}

	private void showDateDialog(Date date,
			DatePickerDialog.OnDateSetListener onDateSetListener) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);

		Dialog dlgDate = new DatePickerDialog(this, onDateSetListener,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		dlgDate.show();
	}

	private void showTimeDialog(Date date,
			TimePickerDialog.OnTimeSetListener myTimeSetListener) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);

		Dialog dlgTime = new TimePickerDialog(this, myTimeSetListener,
				cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
		dlgTime.show();
	}

	private CharSequence getStringFormatedDate(Date date) {
		return DateFormat.format("yyyy. MM. dd", date);
	}

	private CharSequence getStringFormatedTime(Date date) {
		return DateFormat.format("hh:mm", date);
	}

	private Date getDate(int year, int monthOfYear, int dayOfMont,
			int hourOfDay, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, monthOfYear, dayOfMont, hourOfDay, minute);
		return cal.getTime();
	}

	private DatePickerDialog.OnDateSetListener myStartDateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			start_year = year;
			start_monthOfYear = monthOfYear;
			start_dayOfMonth = dayOfMonth;

			schedule.setStartDate(getDate(start_year, start_monthOfYear,
					start_dayOfMonth, start_hourOfDay, start_minute));
			startDate.setText(getStringFormatedDate(schedule.getStartDate()));

		}
	};

	private DatePickerDialog.OnDateSetListener myEndDateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			end_year = year;
			end_monthOfYear = monthOfYear;
			end_dayOfMonth = dayOfMonth;

			schedule.setEndDate(getDate(end_year, end_monthOfYear,
					end_dayOfMonth, end_hourOfDay, end_minute));
			endDate.setText(getStringFormatedDate(schedule.getEndDate()));

		}
	};

	private TimePickerDialog.OnTimeSetListener myStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			start_hourOfDay = hourOfDay;
			start_minute = minute;

			schedule.setStartDate(getDate(start_year, start_monthOfYear,
					start_dayOfMonth, start_hourOfDay, start_minute));
			startTime.setText(getStringFormatedTime(schedule.getStartDate()));
		}
	};

	private TimePickerDialog.OnTimeSetListener myEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			end_hourOfDay = hourOfDay;
			end_minute = minute;

			schedule.setEndDate(getDate(end_year, end_monthOfYear,
					end_dayOfMonth, end_hourOfDay, end_minute));

			endTime.setText(getStringFormatedTime(schedule.getEndDate()));
		}
	};

}

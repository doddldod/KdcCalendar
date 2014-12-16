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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@EActivity(resName = "activity_add_schedule_repeat")
public class Activity_1_AddSchedule_Repeat extends Activity {

	@ViewById
	EditText editText;
	@ViewById
	TextView startDate;
	@ViewById
	TextView endDate;

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

	int repeat_day = 0;

	@ViewById
	RadioButton sun;
	@ViewById
	RadioButton mon;
	@ViewById
	RadioButton tue;
	@ViewById
	RadioButton wed;
	@ViewById
	RadioButton thu;
	@ViewById
	RadioButton fri;
	@ViewById
	RadioButton sat;

	@Bean
	DatabaseManager databaseManager;

	private Schedule schedule;

	@AfterViews
	protected void init() {
		repeat_day = -1;

		schedule = new Schedule();
		schedule.setStartDate(getInitDate());
		schedule.setEndDate(getInitDate());

		sun.setChecked(false);
		mon.setChecked(false);
		tue.setChecked(false);
		wed.setChecked(false);
		thu.setChecked(false);
		fri.setChecked(false);
		sat.setChecked(false);

	}

	@Click(resName = "back")
	protected void onClickBack() {
		finish();
	}

	@Click(resName = "add")
	protected void onAdd() {
		if (repeat_day == -1) {
			Toast.makeText(this, getString(R.string.dayofweek),
					Toast.LENGTH_SHORT).show();
		} else {
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
	}

	@Click(resName = "sun")
	protected void onSun() {
		repeat_day = 0;
	}

	@Click(resName = "mon")
	protected void onMon() {
		repeat_day = 1;
	}

	@Click(resName = "tue")
	protected void onTue() {
		repeat_day = 2;
	}

	@Click(resName = "wed")
	protected void onWed() {
		repeat_day = 3;
	}

	@Click(resName = "Thu")
	protected void onThu() {
		repeat_day = 4;
	}

	@Click(resName = "fri")
	protected void onFri() {
		repeat_day = 5;
	}

	@Click(resName = "sat")
	protected void onSat() {
		repeat_day = 6;
	}

	@Click(resName = "startDate")
	protected void onStartDate() {
		showDateDialog(schedule.getStartDate(), myStartDateListener);
	}

	@Click(resName = "endDate")
	protected void onEndDate() {
		showDateDialog(schedule.getEndDate(), myEndDateListener);
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

	@SuppressWarnings("deprecation")
	private void addSchedule() {

		Schedule newSchedule = new Schedule();

		int tmp_start_dayOfMonth = start_dayOfMonth;
		Calendar start_cal = Calendar.getInstance();
		start_cal.clear();
		start_cal.set(start_year, start_monthOfYear, start_dayOfMonth,
				start_hourOfDay, start_minute);

		Calendar end_cal = Calendar.getInstance();
		end_cal.clear();
		end_cal.set(end_year, end_monthOfYear, end_dayOfMonth, end_hourOfDay,
				end_minute);

		while (start_cal.compareTo(end_cal) < 0) {
			if ((start_cal.get(Calendar.DAY_OF_WEEK) - 1) == repeat_day) {
				newSchedule.setContents("[Repeat] "
						+ editText.getText().toString());
				newSchedule.setStartDate(start_cal.getTime());
				newSchedule.setEndDate(start_cal.getTime());
				databaseManager.addSchedule(newSchedule);
			}
			start_cal.set(start_year, start_monthOfYear,
					++tmp_start_dayOfMonth, start_hourOfDay, start_minute);
		}

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


	private CharSequence getStringFormatedDate(Date date) {
		return DateFormat.format("yyyy. MM. dd", date);
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


}

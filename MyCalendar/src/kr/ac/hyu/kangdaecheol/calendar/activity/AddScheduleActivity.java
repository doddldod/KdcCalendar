package kr.ac.hyu.kangdaecheol.calendar.activity;

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
	DatePicker datePicker;
	@ViewById
	TimePicker timePicker;
	
	@Bean
	DatabaseManager databaseManager;
	
	@Click(R.id.back)
	protected void onClickBack() {
		finish();
	}
	
	@Click(R.id.add)
	protected void onAdd() {
		System.out.println(editText+"a");
		System.out.println(editText.getText()+"a");
		if (editText.getText().length() > 0){
			Schedule schedule = new Schedule();
			schedule.setContents(editText.getText().toString());
			schedule.setYear(datePicker.getYear());
			schedule.setMonth(datePicker.getMonth());
			schedule.setDay(datePicker.getDayOfMonth());
			schedule.setHour(timePicker.getCurrentHour());
			schedule.setMinute(timePicker.getCurrentMinute());
			databaseManager.addSchedule(schedule);
			finish();
		} else {
			Toast.makeText(this, getString(R.string.scheduleAdd), Toast.LENGTH_SHORT).show();
		}
	}
	
}

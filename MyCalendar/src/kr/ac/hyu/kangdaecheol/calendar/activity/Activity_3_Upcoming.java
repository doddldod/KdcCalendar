package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.util.Date;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.adapter.ScheduleAdapter;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.eventbus.MyBus;
import kr.ac.hyu.kangdaecheol.calendar.eventbus.UpdateScheduleEvent;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

@EActivity(resName = "activity_schedule")
public class Activity_3_Upcoming extends Activity {

	@ViewById
	TextView title;
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

	@Extra
	String howmany;

	@AfterViews
	protected void init() {
		bus.register(this);
		setTitle();
		setList();
	}

	private void setTitle() {
		title.setText(String.format(getString(R.string.upcomingTitle)
				+ "      Max : " + howmany));
	}

	private void setList() {
		List<Schedule> list = databaseManager.getUpcomingAllScheduleList(
				new Date(), Long.parseLong(howmany));
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

	@Override
	protected void onDestroy() {
		bus.unregister(this);
		super.onDestroy();
	}

	@Click(resName = "back")
	protected void onClickBack() {
		Activity_Main_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Activity_Main_.intent(this).start();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Subscribe
	public void onUpdateSchedule(UpdateScheduleEvent event) {
		setList();
	}

}

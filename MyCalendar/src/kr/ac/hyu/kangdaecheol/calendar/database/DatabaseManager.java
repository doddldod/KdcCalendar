package kr.ac.hyu.kangdaecheol.calendar.database;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.RootContext;

import android.content.Context;

@EBean(scope=Scope.Singleton)
public class DatabaseManager {
	
	@RootContext
	Context context;
	
	private DatabaseHelper helper;

	@AfterInject
	protected void init() {
		helper = new DatabaseHelper(context);
	}

	private DatabaseHelper getHelper() {
		return helper;
	}

	public List<Schedule> getScheduleListByDate(Date date) {
		try {
			return getHelper().getScheduleDao().queryBuilder().where()
					.le("startDate", date).and()
					.ge("endDate", date)
					.query();
		} catch (SQLException e) {
			return null;
		}
	}
	
	public void addSchedule(Schedule schedule) {
		try {
			getHelper().getScheduleDao().create(schedule);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateSchedule(Schedule schedule) {
		try {
			getHelper().getScheduleDao().update(schedule);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteSchedule(Schedule schedule) {
		try {
			getHelper().getScheduleDao().delete(schedule);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

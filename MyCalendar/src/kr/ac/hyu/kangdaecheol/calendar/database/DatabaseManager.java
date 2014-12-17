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

import com.j256.ormlite.table.TableUtils;

@EBean(scope = Scope.Singleton)
public class DatabaseManager {
	private static final String DATABASE_NAME = "ScheduleListDB.sqlite";
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

	public Schedule getScheduleByid(int id) {
		try {
			return getHelper().getScheduleDao().queryForId(id);
		} catch (SQLException e) {
			return null;
		}
	}

	public List<Schedule> getAllScheduleList(Date date) {
		try {
			return getHelper().getScheduleDao().queryBuilder()
					.orderBy("id", false).query();
		} catch (SQLException e) {
			return null;
		}
	}

	public List<Schedule> getUpcomingAllScheduleList(Date date, Long maxRow) {
		try {
			return getHelper().getScheduleDao().queryBuilder().limit(maxRow)
					.orderBy("endDate", false).where().ge("endDate", date)
					.query();
		} catch (SQLException e) {
			return null;
		}
	}

	public List<Schedule> getScheduleListByDate(Date date) {
		try {
			return getHelper().getScheduleDao().queryBuilder().where()
					.le("startDate", date).and().ge("endDate", date).query();
		} catch (SQLException e) {
			return null;
		}
	}

	public List<Schedule> getWeeklyScheduleList(Date startDate, Date endDate) {
		try {
			return getHelper().getScheduleDao().queryBuilder()
					.orderBy("endDate", false).where()
					.between("startDate", startDate, endDate).or()
					.between("endDate", startDate, endDate).query();
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

	public void deleteAllSchedule() throws SQLException {
		TableUtils
				.clearTable(getHelper().getConnectionSource(), Schedule.class);
	}
}

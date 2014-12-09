package kr.ac.hyu.kangdaecheol.calendar.adapter;

import java.util.List;

import kr.ac.hyu.kangdaecheol.calendar.adapter.view.ScheduleItemView;
import kr.ac.hyu.kangdaecheol.calendar.adapter.view.ScheduleItemView_;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class ScheduleAdapter extends BaseAdapter {

	@RootContext
	protected Context context;

	List<Schedule> scheduleList;
	
	public void setScheduleList(List<Schedule> scheduleList) {
		this.scheduleList = scheduleList;
	}
	
	@Override
	public int getCount() {
		return scheduleList.size();
	}

	@Override
	public Schedule getItem(int position) {
		return scheduleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ScheduleItemView scheduleItemView;
		if (convertView == null) {
			scheduleItemView = ScheduleItemView_.build(context);
		} else {
			scheduleItemView = (ScheduleItemView) convertView;
		}

		scheduleItemView.bind(getItem(position));

		return scheduleItemView;
	}

}

package kr.ac.hyu.kangdaecheol.calendar.adapter;

import java.util.ArrayList;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;

import kr.ac.hyu.kangdaecheol.calendar.adapter.view.BoardItemView;
import kr.ac.hyu.kangdaecheol.calendar.adapter.view.BoardItemView_;
import kr.ac.hyu.kangdaecheol.calendar.model.BoardInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class BoardAdapter extends BaseAdapter {
	
	@RootContext
	Context context;
	
	@SystemService
	LayoutInflater mInflater;
	
	private ArrayList<BoardInfo> boardList;
	
	public void setList(ArrayList<BoardInfo> boardList) {
		this.boardList = boardList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BoardItemView boardItemView;
		if (convertView == null) {
			boardItemView = BoardItemView_.build(context);
		} else {
			boardItemView = (BoardItemView) convertView;
		}

		boardItemView.bind(boardList.get(position));
		
		return boardItemView;
	}

	@Override
	public int getCount() {
		return boardList.size();
	}

	@Override
	public BoardInfo getItem(int position) {
		return boardList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}

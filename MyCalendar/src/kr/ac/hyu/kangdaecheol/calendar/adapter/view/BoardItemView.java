package kr.ac.hyu.kangdaecheol.calendar.adapter.view;


import kr.ac.hyu.kangdaecheol.calendar.model.BoardInfo;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

@EViewGroup(resName="fragment_board_onelineboard_listitem")
public class BoardItemView extends LinearLayout {

	@ViewById
	TextView Data_OneLineBoard_Content;
	@ViewById
	TextView Data_OneLineBoard_Author;
	@ViewById
	TextView Data_OneLineBoard_Date;
	
	public BoardItemView(Context context) {
		super(context);
	}

	public void bind(BoardInfo boardInfo) {
		Data_OneLineBoard_Content.setText(boardInfo.getContent());
		Data_OneLineBoard_Author.setText(boardInfo.getAuthor());
		Data_OneLineBoard_Date.setText(boardInfo.getDay() + ". " + boardInfo.getMonth() + ". " + boardInfo.getYear());
	}

}
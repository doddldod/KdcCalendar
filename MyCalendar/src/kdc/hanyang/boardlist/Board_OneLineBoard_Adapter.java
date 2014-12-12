package kdc.hanyang.boardlist;

import java.util.ArrayList;

import kr.ac.hyu.kangdaecheol.calendar.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Board_OneLineBoard_Adapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<String> mRowContent;
	private ArrayList<String> mRowAuthor;
	private ArrayList<String> mRowDate;
	private int resourceLayoutId;

	public Board_OneLineBoard_Adapter(Context context, int textViewResourceId,
			ArrayList<String> Content, ArrayList<String> Author,
			ArrayList<String> Date) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		resourceLayoutId = textViewResourceId;
		mRowContent = Content;
		mRowAuthor = Author;
		mRowDate = Date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(resourceLayoutId, parent, false);
		}

		TextView tvTextLabel1 = (TextView) convertView
				.findViewById(R.id.Data_OneLineBoard_Content);
		tvTextLabel1.setText(mRowContent.get(position));

		
		TextView tvTextLabel2 = (TextView) convertView
				.findViewById(R.id.Data_OneLineBoard_Author);
		tvTextLabel2.setText(mRowAuthor.get(position));
		
		TextView tvTextLabel3 = (TextView) convertView
				.findViewById(R.id.Data_OneLineBoard_Date);
		tvTextLabel3.setText(mRowDate.get(position));
		return convertView;
	}

	@Override
	public int getCount() {
		return mRowContent.size();
	}

	@Override
	public Object getItem(int position) {
		return mRowContent.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}

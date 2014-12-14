package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.adapter.BoardAdapter;
import kr.ac.hyu.kangdaecheol.calendar.http.HttpCon;
import kr.ac.hyu.kangdaecheol.calendar.model.BoardInfo;
import kr.ac.hyu.kangdaecheol.calendar.setting.Setting_Variables;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(resName = "activity_4_board")
public class Activity_4_Board extends Activity implements OnScrollListener {

	@ViewById
	ListView boardListView;
	@ViewById
	EditText boardEditText;

	@Bean
	BoardAdapter mAdapter;

	private View footer;
	private ArrayList<BoardInfo> boardList = new ArrayList<BoardInfo>();

	private boolean isListViewLocked;

	int listCount = 0;
	int listMax = 0;

	public static Toast mToast;
	public static Context con;

	@Override
	public void onResume() {
		super.onResume();

		mToast = null;
		con = getApplicationContext();

		getAccount();
		initBoard();
		getBoard();
	}
	
	private void getAccount() {
		AccountManager manager = AccountManager.get(this);
		Account[] accounts = manager.getAccounts();
		final int count = accounts.length;
		Account account = null;

		for (int i = 0; i < count; i++) {
			account = accounts[i];
			if (account.type.equals("com.google")) {
				Setting_Variables.User_Email = account.name;
			}
		}
	}

	private void setToast(String str) {
		if (mToast == null) {
			mToast = Toast.makeText(con, str, Toast.LENGTH_LONG);
		} else {
			mToast.setText(str);
		}
		mToast.show();
	}

	@UiThread
	public void initBoard() {
		boardEditText.setText("");
		listCount = 0;
		listMax = 0;
		if (footer != null) {
			boardListView.removeFooterView(footer);
			footer = null;
		}
		boardList.clear();
	}

	@SuppressLint("InflateParams")
	@UiThread
	public void setBoard() {
		isListViewLocked = true;
		mAdapter.setList(boardList);

		if (footer == null) {
			footer = Activity_4_Board.this.getLayoutInflater().inflate(
					R.layout.fragment_board_onelineboard_footer, null, false);
			boardListView.addFooterView(footer);
		}

		boardListView.setOnScrollListener(this);
		boardListView.setAdapter(mAdapter);

		addItems(10);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int count = totalItemCount - visibleItemCount;
		if (firstVisibleItem >= count && totalItemCount != 0
				&& isListViewLocked == false) {
			addItems(10);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	private void addItems(final int size) {
		isListViewLocked = true;
		listMax = listCount + size;

		Runnable run = new Runnable() {
			@Override
			public void run() {
				for (int i = listCount; i < listMax; i++) {

					if (i < boardList.size()) {
						listCount++;
					} else {
						boardListView.removeFooterView(footer);
						footer = null;
						boardList.clear();
					}
				}
				mAdapter.notifyDataSetChanged();
				isListViewLocked = false;
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(run, 1000);
	}

	@ItemClick
	protected void boardListViewItemClicked(BoardInfo clickedItem) {
		setToast(clickedItem.getContent());
	}

	@ItemLongClick
	protected void boardListViewItemLongClicked(BoardInfo clickedItem) {
		DialogSelectOption(clickedItem);
	}
	
	@Click(resName = "submit")
	protected void onClickSubmit() {
		if (Setting_Variables.User_Email.equals("") == true) {
			setToast(getString(R.string.NoLogin));
		} else if (boardEditText.getText().toString().equals("") == true) {
			setToast(getString(R.string.NothingInContent));
		} else {
			addBoard();
		}
	}

	private void DialogSelectOption(final BoardInfo boardInfo) {
		AlertDialog.Builder ab = new AlertDialog.Builder(Activity_4_Board.this);
		ab.setTitle(getString(R.string.Title_Delete));
		ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (Setting_Variables.User_Email.equals(boardInfo.getAuthor()) == true) {
					deleteBoard(boardInfo);
				} else {
					setToast(getString(R.string.BT_CantDelete));
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		ab.show();
	}

	@Background
	protected void getBoard() {
		String url = "http://" + Setting_Variables.URL_ServerAddr + "/"
				+ "storeGetList.do";
		String response = "false";
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("type", "OneLineBoard"));

		try {
			response = HttpCon.executeHttpPost(url, postParameters);
		} catch (Exception e) {
		}
		if (!response.equals("false")) {
			getBoardSuccess(response);
		}
	}

	private void getBoardSuccess(String response) {
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			parser.setInput(new StringReader(response));
			int parserEvent = parser.getEventType();
			String tag = "";

			boolean flag = false;
			BoardInfo ei = null;

			while (parserEvent != XmlPullParser.END_DOCUMENT) {
				switch (parserEvent) {
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					if (tag.compareTo("onelineboardlist") == 0) {
						flag = true;
					}
					if (tag.compareTo("onelineboarditem") == 0 && flag) {
						ei = new BoardInfo();
					}
					break;
				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.compareTo("onelineboardlist") == 0) {
						flag = false;
						break;
					}
					if (tag.compareTo("onelineboarditem") == 0) {
						boardList.add(ei);
					}
					break;
				case XmlPullParser.TEXT:
					String text = parser.getText();
					if (text.trim().length() == 0)
						break;
					if (tag.compareTo("id") == 0) {
						ei.setId(Integer.parseInt(text));
					} else if (tag.compareTo("author") == 0) {
						ei.setAuthor(text);
					} else if (tag.compareTo("content") == 0) {
						ei.setContent(text);
					} else if (tag.compareTo("day") == 0) {
						ei.setDay(text);
					} else if (tag.compareTo("month") == 0) {
						ei.setMonth(text);
					} else if (tag.compareTo("year") == 0) {
						ei.setYear(text);
					}
					break;
				}
				parserEvent = parser.next();
			}
		} catch (XmlPullParserException e) {
		} catch (IOException e) {
		}
		setBoard();
	}

	@Background
	protected void addBoard() {
		String url = "http://" + Setting_Variables.URL_ServerAddr + "/"
				+ "storeGetList.do";
		String author = Setting_Variables.User_Email;
		String content = boardEditText.getText().toString();

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("type", "newOneLineBoard"));
		postParameters.add(new BasicNameValuePair("author", author));
		postParameters.add(new BasicNameValuePair("content", content));
		try {
			HttpCon.executeHttpPost(url, postParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initBoard();
		getBoard();
	}

	@Background
	protected void deleteBoard(BoardInfo boardInfo) {
		String url = "http://" + Setting_Variables.URL_ServerAddr + "/"
				+ "storeGetList.do";

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters
				.add(new BasicNameValuePair("type", "deleteOneLineBoard"));
		postParameters.add(new BasicNameValuePair("id", String.valueOf(boardInfo.getId())));
		try {
			HttpCon.executeHttpPost(url, postParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		initBoard();
		getBoard();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Activity_Main_.intent(this).start();
			this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
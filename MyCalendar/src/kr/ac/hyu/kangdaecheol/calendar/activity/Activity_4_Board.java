package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import kdc.hanyang.boardlist.Board_OneLineBoard_Adapter;
import kdc.hanyang.dao.OneLineBoardInfo;
import kdc.hanyang.http.HttpCon;
import kdc.hanyang.setting.Setting_Variables;
import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(resName = "activity_4_board")
public class Activity_4_Board extends Activity implements OnScrollListener {

	LinearLayout Board_Right_Layout_OneLineBoard;
	@ViewById
	ListView Board_OneLineBoard_ListView;
	@ViewById
	EditText Board_OneLineBoard_Content;
	@ViewById
	Button Board_OneLineBoard_Submit;

	// ///////////
	private static final String LOG = "OneLineBoard";
	private Board_OneLineBoard_Adapter mAdapter;
	View footer;
	private ArrayList<String> mRowContent;
	private ArrayList<String> mRowAuthor;
	private ArrayList<String> mRowDate;
	private ArrayList<String> Str_Content;
	private ArrayList<String> Str_ID;
	private ArrayList<String> Str_Author;

	private boolean mLockListView;
	ArrayList<OneLineBoardInfo> OneLineBoard_List = new ArrayList<OneLineBoardInfo>();
	int count = 0;
	int max = 0;

	//
	//
	// TextView OneLineBoard_Right_Title;
	// View v = null;
	public static Toast mToast;
	public static Context con;
	public static int Delete_PS;

	public Activity_4_Board() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_4_board);
	}

	@Override
	public void onResume() {

		super.onResume();

		mToast = null;
		con = Activity_4_Board.this;

		// Board_OneLineBoard_ListView = (ListView)
		// findViewById(R.id.Board_OneLineBoard_ListView);
		// Board_OneLineBoard_Content = (EditText)
		// findViewById(R.id.Board_OneLineBoard_Content);
		// Board_OneLineBoard_Submit = (Button)
		// findViewById(R.id.Board_OneLineBoard_Submit);

		Board_OneLineBoard_ListView
				.setOnItemClickListener(Listner_OneLineBoard);
		Board_OneLineBoard_ListView
				.setOnItemLongClickListener(Listner_DeleteOneLineBoard);
		Board_OneLineBoard_Submit.setOnClickListener(Listner_newOneLineBoard);

		AccountManager manager = AccountManager.get(Activity_4_Board.this);
		Account[] accounts = manager.getAccounts();
		final int count = accounts.length;
		Account account = null;

		for (int i = 0; i < count; i++) {
			account = accounts[i];
			Log.d("HSGIL", "Account - name: " + account.name + ", type :"
					+ account.type);
			if (account.type.equals("com.google")) { // 이러면 구글 계정 구분 가능
				Setting_Variables.User_Email = account.name;
			}
		}

		Init_OneLineBoard();
		getHttp();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void Right_OneLineBoard_Visible() {

		Init_OneLineBoard();
		getHttp();

	}

	OnItemClickListener Listner_OneLineBoard = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parentView, View clickedView,
				int position, long id) {
			setToast(Str_Content.get(position));
			System.out.println(Str_Content.get(position));
		}
	};

	Button.OnClickListener Listner_newOneLineBoard = new View.OnClickListener() {
		public void onClick(View v) {

			if (Setting_Variables.User_Email.equals("") == true) {
				Toast.makeText(Activity_4_Board.this, R.string.NoLogin,
						Toast.LENGTH_SHORT).show();
			} else if (Board_OneLineBoard_Content.getText().toString()
					.equals("") == true) {
				Toast.makeText(Activity_4_Board.this,
						R.string.NothingInContent, Toast.LENGTH_SHORT).show();
			} else {
				newOneLineBoard();
			}
		}
	};

	public void Init_OneLineBoard() {
		count = 0;
		max = 0;
		if (footer != null) {
			Board_OneLineBoard_ListView.removeFooterView(footer);
			footer = null;
		}
		mRowContent = null;
		mRowAuthor = null;
		mRowDate = null;
		OneLineBoard_List.clear();

	}

	public void Set_OneLineBoard() {

		mRowContent = new ArrayList<String>();
		mRowAuthor = new ArrayList<String>();
		mRowDate = new ArrayList<String>();
		Str_Content = new ArrayList<String>();
		Str_ID = new ArrayList<String>();
		Str_Author = new ArrayList<String>();
		mLockListView = true;

		if (footer == null) {
			mAdapter = new Board_OneLineBoard_Adapter(Activity_4_Board.this,
					R.layout.fragment_board_onelineboard_listitem, mRowContent,
					mRowAuthor, mRowDate);

			footer = Activity_4_Board.this.getLayoutInflater().inflate(
					R.layout.fragment_board_onelineboard_footer, null, false);
			Board_OneLineBoard_ListView.addFooterView(footer);
		}

		Board_OneLineBoard_ListView.setOnScrollListener(this);
		Board_OneLineBoard_ListView.setAdapter(mAdapter);

		addItems(10);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int count = totalItemCount - visibleItemCount;

		if (firstVisibleItem >= count && totalItemCount != 0
				&& mLockListView == false) {
			Log.i(LOG, "Loading next items");
			addItems(10);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	private void addItems(final int size) {
		mLockListView = true;
		max = count + size;

		Runnable run = new Runnable() {
			@Override
			public void run() {
				for (int i = count; i < max; i++) {

					if (i < OneLineBoard_List.size()) {
						OneLineBoardInfo ei = OneLineBoard_List.get(i);
						Str_ID.add(String.valueOf(ei.getId()));
						mRowContent.add(ei.getContent());
						Str_Content.add(ei.getContent());
						mRowAuthor.add(ei.getAuthor());
						Str_Author.add(ei.getAuthor());
						mRowDate.add(ei.getDay() + ". " + ei.getMonth() + ". "
								+ ei.getYear());
						count++;
					} else {
						Board_OneLineBoard_ListView.removeFooterView(footer);
						footer = null;
						mRowContent = null;
						OneLineBoard_List.clear();
					}
				}

				mAdapter.notifyDataSetChanged();
				mLockListView = false;
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(run, 1000);
	}

	public void getHttp() {
		try {

			String url = "http://" + Setting_Variables.URL_ServerAddr + "/"
					+ "storeGetList.do";
			String[] urls = new String[2];
			urls[0] = url;
			urls[1] = "OneLineBoard";
			new httpTask().execute(urls);

		} catch (Exception e) {
			Toast.makeText(Activity_4_Board.this, e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void newOneLineBoard() {
		String content = Board_OneLineBoard_Content.getText().toString();
		String author = Setting_Variables.User_Email;

		System.out.println("Content : " + content + "\nAuthor : " + author);
		try {
			String[] urls = new String[3];
			String url = "http://" + Setting_Variables.URL_ServerAddr + "/"
					+ "storeGetList.do";
			urls[0] = url;
			urls[1] = author;
			urls[2] = content;

			new httpTasknewOneLineBoard().execute(urls);
		} catch (Exception e) {
			Toast.makeText(Activity_4_Board.this, e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	private class httpTasknewOneLineBoard extends
			AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */
		protected String doInBackground(String... urls) {
			String response = "false";

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters
					.add(new BasicNameValuePair("type", "newOneLineBoard"));
			postParameters.add(new BasicNameValuePair("author", urls[1]));
			postParameters.add(new BasicNameValuePair("content", urls[2]));
			try {
				response = HttpCon.executeHttpPost(urls[0], postParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		protected void onPostExecute(String response) {
			if (response.equals("newOneLineBoardSuccess")) {
				System.out.println("newOneLineBoard Success");
			} else {
				System.out.println("newOneLineBoard Fail");
			}
			Init_OneLineBoard();
			getHttp();
			Board_OneLineBoard_Content.setText("");
		}
	}

	private class httpTask extends AsyncTask<String, Void, String> {
		/**
		 * The system calls this to perform work in a worker thread and delivers
		 * it the parameters given to AsyncTask.execute()
		 */

		protected String doInBackground(String... urls) {
			String response = "false";
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("type", "OneLineBoard"));

			try {
				response = HttpCon.executeHttpPost(urls[0], postParameters);
			} catch (Exception e) {
				Log.e("Fragment_Board - Exception", e.toString());
			}
			return response;
		}

		/**
		 * The system calls this to perform work in the UI thread and delivers
		 * the result from doInBackground()
		 */
		protected void onPostExecute(String response) {
			if (!response.equals("false")) {
				try {

					XmlPullParserFactory parserFactory = XmlPullParserFactory
							.newInstance();
					XmlPullParser parser = parserFactory.newPullParser();
					parser.setInput(new StringReader(response));
					int parserEvent = parser.getEventType();
					String tag = "";

					boolean flag = false;
					OneLineBoardInfo ei = null;

					while (parserEvent != XmlPullParser.END_DOCUMENT) {
						switch (parserEvent) {
						case XmlPullParser.START_TAG:
							tag = parser.getName();
							if (tag.compareTo("onelineboardlist") == 0) {
								flag = true;
							}
							if (tag.compareTo("onelineboarditem") == 0 && flag) {
								ei = new OneLineBoardInfo();
							}
							break;
						case XmlPullParser.END_TAG:
							tag = parser.getName();
							if (tag.compareTo("onelineboardlist") == 0) {
								flag = false;
								break;
							}
							if (tag.compareTo("onelineboarditem") == 0) {
								OneLineBoard_List.add(ei);
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
					Log.e("Fragment_Board - XmlPullParserException",
							e.toString());
				} catch (IOException e) {
					Log.e("Fragment_Board - IOException", e.toString());
				}
				Set_OneLineBoard();

			}
		}
	}

	public static void setToast(String str) {
		if (mToast == null) {
			mToast = Toast.makeText(con, str, Toast.LENGTH_LONG);
		} else {
			mToast.setText(str);
		}
		mToast.show();
	}

	// /////////////////////////////////////////////

	OnItemLongClickListener Listner_DeleteOneLineBoard = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			Delete_PS = arg2;
			DialogSelectOption();
			return false;
		}
	};

	private void DialogSelectOption() {

		AlertDialog.Builder ab = new AlertDialog.Builder(Activity_4_Board.this);
		ab.setTitle(getString(R.string.Title_Delete));
		ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if (Setting_Variables.User_Email.equals(Str_Author
						.get(Delete_PS)) == true) {
					deleteOneLineBoard(Delete_PS);
				} else {
					setToast(getString(R.string.BT_CantDelete));
				}
				System.out.println(Str_Author.get(Delete_PS));
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		ab.show();
	}

	private void deleteOneLineBoard(int Delete_PS) {

		String id = Str_ID.get(Delete_PS);

		try {
			String[] urls = new String[2];
			String url = "http://" + Setting_Variables.URL_ServerAddr + "/"
					+ "storeGetList.do";
			urls[0] = url;
			urls[1] = id;

			new httpTaskdeleteOneLineBoard().execute(urls);
		} catch (Exception e) {
			Toast.makeText(Activity_4_Board.this, e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	private class httpTaskdeleteOneLineBoard extends
			AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			String response = "false";

			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("type",
					"deleteOneLineBoard"));
			postParameters.add(new BasicNameValuePair("id", urls[1]));
			try {
				response = HttpCon.executeHttpPost(urls[0], postParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		protected void onPostExecute(String response) {
			if (response.equals("deleteOneLineBoardSuccess")) {
				System.out.println("deleteOneLineBoard Success");
			} else {
				System.out.println("deleteOneLineBoard Fail");
			}
			Init_OneLineBoard();
			getHttp();
			Board_OneLineBoard_Content.setText("");
		}
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
package kr.ac.hyu.kangdaecheol.calendar.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import kr.ac.hyu.kangdaecheol.calendar.R;
import kr.ac.hyu.kangdaecheol.calendar.database.DatabaseManager;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;
import kr.ac.hyu.kangdaecheol.calendar.setting.Setting_Variables;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

@EActivity(resName = "activity_main")
public class Activity_Main extends Activity {

	@Bean
	DatabaseManager databaseManager;

	private Handler mHandler;
	private boolean mFlag = false;
	private Toast mToast;

	@Override
	protected void onResume() {
		super.onResume();
		setLayout();
	}

	@SuppressLint("HandlerLeak")
	public void setLayout() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					mFlag = false;
				}
			}
		};
	}

	@Click(resName = "chooseCalendar")
	void onClickChooseCalendar() {
		Activity_1_ChooseCalendar_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	@Click(resName = "fileIO")
	void onClickFileIO() {
		showFileIODialog();
	}

	void showFileIODialog() {
		final CharSequence[] items = { getString(R.string.save),
				getString(R.string.load) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getString(R.string.fileio)).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int index) {
						if (index == 0) {
							try {
								SaveToFile();
							} catch (IOException e) {
								e.printStackTrace();
								showToast(getString(R.string.fileError));
							}
						} else {
							try {
								LoadFromFile();
							} catch (ParseException | SQLException e) {
								e.printStackTrace();
								showToast(getString(R.string.fileError));
							}
						}
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Click(resName = "board")
	void onClickBoard() {
		Activity_4_Board_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	@Click(resName = "email")
	void onClickEmail() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { Setting_Variables.Developer_Email });
		intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
		startActivity(Intent.createChooser(intent,
				getString(R.string.email_send)));
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Click(resName = "about")
	void onClickAbout() {
		Activity_6_About_.intent(this).start();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	@Click(resName = "upComing")
	void onClickUpComing() {
		showUpComingDialog();
	}

	void showUpComingDialog() {
		final CharSequence[] items = { "10", "20", "50" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(getString(R.string.howmanyupcoming)).setItems(items,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int index) {
						Activity_3_Upcoming_.intent(Activity_Main.this)
								.howmany(String.valueOf(items[index])).start();
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						finish();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public void showToast(String mText) {
		if (mText != null) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), mText,
						Toast.LENGTH_SHORT);
			} else {
				mToast.setText(mText);
			}
			mToast.show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!mFlag) {
				showToast(getString(R.string.str_exit));
				mFlag = true;
				mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	void SaveToFile() throws IOException {
		List<Schedule> list = databaseManager.getAllScheduleList(new Date());
		if (list.size() > 0) {
			File file;
			file = new File(Setting_Variables.path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(Setting_Variables.path + File.separator
					+ "db_mycalendar.txt");

			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
				BufferedWriter buw = new BufferedWriter(new OutputStreamWriter(
						fos, "UTF8"));

				for (int i = 0; i < list.size(); i++) {

					onTextWriting(fos, buw, list.get(i).getId(), list.get(i)
							.getContents(), list.get(i).getStartDate(), list
							.get(i).getEndDate());

				}
				buw.close();
				fos.close();
				showToast(getString(R.string.saved));
			} catch (FileNotFoundException e) {
			}
		} else {
			showToast(getString(R.string.NothingInSchedule));
		}
	}

	private void onTextWriting(FileOutputStream fos, BufferedWriter buw,
			int id, String contents, Date startdate, Date enddate) {
		try {
			buw.write(id + "\n");
			buw.write(contents + "\n");
			buw.write(startdate.toString() + "\n");
			buw.write(enddate.toString() + "\n");
		} catch (IOException e) {
		}
	}

	void LoadFromFile() throws ParseException, SQLException {
		String strBuf = ReadTextFile();
		StringTokenizer st = new StringTokenizer(strBuf);

		String str_start_date;
		String str_end_date;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.US);

		// databaseManager.deleteAllSchedule();

		while (st.hasMoreTokens()) {
			Date start_date = null;
			Date end_date = null;
			Schedule sd = new Schedule();
			sd.setId(Integer.parseInt(st.nextToken("\n")));
			sd.setContents(st.nextToken("\n"));
			str_start_date = st.nextToken("\n");
			start_date = simpleDateFormat.parse(str_start_date);
			sd.setStartDate(start_date);
			str_end_date = st.nextToken("\n");
			end_date = simpleDateFormat.parse(str_end_date);
			sd.setEndDate(end_date);

			databaseManager.addSchedule(sd);
		}

		Toast.makeText(this, getString(R.string.loadfromfile),
				Toast.LENGTH_SHORT).show();
	}

	public String ReadTextFile() {
		String text = null;
		try {

			File file;
			file = new File(Setting_Variables.path);
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(Setting_Variables.path + File.separator
					+ "db_mycalendar.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));

			String line = "";
			StringBuilder txt = new StringBuilder();
			while ((line = br.readLine()) != null) {
				txt.append(line + "\n");
			}
			text = new String(txt);
			br.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return text;
	}

}

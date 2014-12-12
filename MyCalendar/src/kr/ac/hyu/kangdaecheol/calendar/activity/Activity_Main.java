package kr.ac.hyu.kangdaecheol.calendar.activity;

import kdc.hanyang.setting.Setting_Variables;
import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

@EActivity(resName = "activity_main")
public class Activity_Main extends Activity {

	private Handler mHandler;
	private boolean mFlag = false;
	private Toast mToast;
	
	@ViewById
	Button BT_ViewEvents;
	@ViewById
	Button BT_AddEvents;
	@ViewById
	Button BT_UpComing;
	@ViewById
	Button BT_Board;
	@ViewById
	Button BT_Email;
	@ViewById
	Button BT_About;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		System.out.println("Asdfasdfasdfasdfasdf");
		setLayout();

	}

	public void setLayout() {
//		BT_ViewEvents = (Button) findViewById(R.id.BT_ViewEvents);
//		BT_AddEvents = (Button) findViewById(R.id.BT_AddEvents);
//		BT_UpComing = (Button) findViewById(R.id.BT_UpComing);
//		BT_Board = (Button) findViewById(R.id.BT_Board);
//		BT_Email = (Button) findViewById(R.id.BT_Email);
//		BT_About = (Button) findViewById(R.id.BT_About);

//		BT_ViewEvents.setOnClickListener(Listener_BT);
//		BT_AddEvents.setOnClickListener(Listener_BT);
//		BT_UpComing.setOnClickListener(Listener_BT);
//		BT_Board.setOnClickListener(Listener_BT);
//		BT_Email.setOnClickListener(Listener_BT);
//		BT_About.setOnClickListener(Listener_BT);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					mFlag = false;
				}
			}
		};
	}

	
	@Click(resName="BT_ViewEvents")
	void onBT_ViewEvents() {
		Cal_MainActivity_.intent(this).start();
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="BT_Board")
	void onBT_Board() {
		Activity_4_Board_.intent(this).start();
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Click(resName="BT_Email")
	void onBT_Email() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { Setting_Variables.Developer_Email });
		intent.putExtra(Intent.EXTRA_SUBJECT,
				getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT,
				getString(R.string.email_body));
		startActivity(Intent.createChooser(intent,
				getString(R.string.email_send)));
	}
	
	@Click(resName="BT_About")
	void onBT_About() {
		Activity_6_About_.intent(this).start();
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
//	Button.OnClickListener Listener_BT = new View.OnClickListener() {
//		public void onClick(View v) {
//			Intent intent = null;
//			switch (v.getId()) {
//			case R.id.BT_ViewEvents:
//				intent = new Intent(
//						Activity_Main.this,
//						Cal_MainActivity.class);
//				startActivity(intent);
//				overridePendingTransition(
//						R.anim.move_lefttoright,
//						R.anim.move_lefttoright);
//				finish();
//
////				final CharSequence[] items = { getString(R.string.str_weekly),
////						getString(R.string.str_monthly) };
////
////				AlertDialog.Builder builder = new AlertDialog.Builder(
////						Activity_Main.this);
////
////				builder.setTitle(getString(R.string.str_viewevents)).setItems(
////						items, new DialogInterface.OnClickListener() {
////							public void onClick(DialogInterface dialog,
////									int index) {
////								if (index == 0) {
////
////									Intent intent = new Intent(
////											Activity_Main.this,
////											Activity_1_ViewEvents_Weekly.class);
////									startActivity(intent);
////									overridePendingTransition(
////											R.anim.move_lefttoright,
////											R.anim.move_lefttoright);
////									finish();
////								} else if (index == 1) {
////
////									Intent intent = new Intent(
////											Activity_Main.this,
////											Activity_1_ViewEvents_Weekly.class);
////									startActivity(intent);
////									overridePendingTransition(
////											R.anim.move_lefttoright,
////											R.anim.move_lefttoright);
////									finish();
////								}
////							}
////						});
////
////				AlertDialog dialog = builder.create();
////				dialog.show();
//
//				break;
//
//			case R.id.BT_Board:
////				intent = new Intent(Activity_Main.this,
////						Activity_4_Board.class);
////				startActivity(intent);
////				overridePendingTransition(R.anim.move_lefttoright,
////						R.anim.move_lefttoright);
////				finish();
//				break;
//			
//			case R.id.BT_Email:
//				intent = new Intent(Intent.ACTION_SEND);
//				intent.setType("message/rfc822");
//				intent.putExtra(Intent.EXTRA_EMAIL,
//						new String[] { Setting_Variables.Developer_Email });
//				intent.putExtra(Intent.EXTRA_SUBJECT,
//						getString(R.string.email_subject));
//				intent.putExtra(Intent.EXTRA_TEXT,
//						getString(R.string.email_body));
//				startActivity(Intent.createChooser(intent,
//						getString(R.string.email_send)));
//				break;
//			case R.id.BT_About:
////				intent = new Intent(Activity_Main.this,
////						Activity_6_About.class);
////				startActivity(intent);
////				overridePendingTransition(R.anim.move_lefttoright,
////						R.anim.move_lefttoright);
////				finish();
//				break;
//			}
//
//		}
//
//	};

	//
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.activity__intro, menu);
	// return true;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}

	public void showToast(String mText) {
		if (mText != null) {
			if (mToast == null)
				mToast = Toast.makeText(getApplicationContext(), mText,
						Toast.LENGTH_SHORT);
			else
				mToast.setText(mText);
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

}

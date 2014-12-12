package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

@EActivity(resName = "activity_intro")
public class Activity_Intro extends Activity {
	@ViewById
	ImageView Intro_Img;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		new Thread(new Runnable() {

			public void run() {
				try {
					Intro_Img.setBackgroundResource(R.drawable.img_intro_01);
					Animation alphaAnim = AnimationUtils.loadAnimation(
							Activity_Intro.this, R.anim.fade_intro);
					Intro_Img.startAnimation(alphaAnim);
					Thread.sleep(1800);

					isIntro();
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void isIntro() {
		Activity_Main_.intent(this).start();
		finish();
	}
}

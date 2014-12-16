package kr.ac.hyu.kangdaecheol.calendar.activity;

import kr.ac.hyu.kangdaecheol.calendar.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@EActivity(resName = "activity_intro")
public class Activity_Intro extends Activity {
	
	@ViewById
	ImageView introImage;

	@AfterViews
	protected void init() {
		startIntroAnimation();
	}

	@Background
	protected void startIntroAnimation() {
		introImage.setBackgroundResource(R.drawable.img_intro);
		introImage.setScaleType(ScaleType.FIT_CENTER);
		Animation alphaAnim = AnimationUtils.loadAnimation(this, R.anim.fade_intro);
		introImage.startAnimation(alphaAnim);
		try {
			Thread.sleep(1800);
		} catch (Exception e) {
		}
		isIntro();
	}

	private void isIntro() {
		Activity_Main_.intent(this).start();
		finish();
	}
	
}

package com.example.viewanimation.basic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.helloanimation.R;

public class BasicAnimationDemoActivity extends Activity {

	private RotateAnimation mTurnupAnimation;
	private RotateAnimation mTurndownAnimation;
	private TranslateAnimation mTranslateAnimationOne;
	private TranslateAnimation mTranslateAnimationTwo;
	private AlphaAnimation mAlphaAnimationOne;
	private AlphaAnimation mAlphaAnimationTwo;
	private AnimationSet mAlphaAnimationSet;
	private ScaleAnimation mScaleAnimation;

	private View mStartButton = null;
	private View mTargetView = null;
	private int mStateId = 0;
	private int mStateCount = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_view_animation);
		initAnimations();
		mStartButton = findViewById(R.id.startButton);
		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				playAnimations();
			}
		});
		mTargetView = findViewById(R.id.target);
	}

	private void initAnimations() {
		mTurnupAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mTurnupAnimation.setInterpolator(new LinearInterpolator());
		mTurnupAnimation.setDuration(500);
		mTurnupAnimation.setFillAfter(true);

		mTurndownAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mTurndownAnimation.setInterpolator(new LinearInterpolator());
		mTurndownAnimation.setDuration(500);
		mTurndownAnimation.setFillAfter(true);

		mTranslateAnimationOne = new TranslateAnimation(0, 100, 0, 100);
		mTranslateAnimationOne
				.setInterpolator(new AccelerateDecelerateInterpolator());
		mTranslateAnimationOne.setDuration(1000);
		mTranslateAnimationOne.setFillAfter(true);

		mTranslateAnimationTwo = new TranslateAnimation(100, 100, 0, 100);
		mTranslateAnimationTwo
				.setInterpolator(new AccelerateDecelerateInterpolator());
		mTranslateAnimationTwo.setDuration(1000);
		mTranslateAnimationTwo.setFillAfter(true);

		mAlphaAnimationOne = new AlphaAnimation(1, 0);
		mAlphaAnimationOne.setDuration(500);
		mAlphaAnimationOne.setFillAfter(true);

		mAlphaAnimationTwo = new AlphaAnimation(0, 1);
		mAlphaAnimationTwo.setDuration(1000);
		mAlphaAnimationTwo.setStartOffset(500);
		mAlphaAnimationTwo.setFillAfter(true);

		mAlphaAnimationSet = new AnimationSet(false);
		mAlphaAnimationSet.addAnimation(mAlphaAnimationOne);
		mAlphaAnimationSet.addAnimation(mAlphaAnimationTwo);
		mAlphaAnimationSet.setDuration(5000);

		mAlphaAnimationSet.setFillAfter(true);

		mScaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f);
		mScaleAnimation.setDuration(1000);
		mScaleAnimation.setFillAfter(true);

	}

	private void playAnimations() {
		switch (mStateId) {
		case 0:
			mTargetView.startAnimation(mTurndownAnimation);
			break;
		case 1:
			mTargetView.startAnimation(mTurnupAnimation);
			break;
		case 2:
			mTargetView.startAnimation(mTranslateAnimationOne);
			break;
		case 3:
			mTargetView.startAnimation(mTranslateAnimationTwo);
			break;
		case 4:
			mTargetView.startAnimation(mAlphaAnimationSet);
			break;
		case 5:
			mTargetView.startAnimation(mScaleAnimation);
			break;

		default:
			break;
		}

		mStateId = (++mStateId) % mStateCount;

	}
}

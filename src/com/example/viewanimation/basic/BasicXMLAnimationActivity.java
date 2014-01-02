package com.example.viewanimation.basic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.helloanimation.R;

public class BasicXMLAnimationActivity extends Activity {

	private Animation mTurnupAnimation;
	private Animation mTurndownAnimation;
	private Animation mTranslateAnimationOne;
	private Animation mTranslateAnimationTwo;
	private Animation mAlphaAnimationSet;
	private Animation mScaleAnimation;

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
		mTurnupAnimation = AnimationUtils
				.loadAnimation(this, R.anim.rotate_one);

		mTurndownAnimation = AnimationUtils.loadAnimation(this,
				R.anim.rotate_two);

		mTranslateAnimationOne = AnimationUtils.loadAnimation(this,
				R.anim.translate_one);

		mTranslateAnimationTwo = AnimationUtils.loadAnimation(this,
				R.anim.translate_two);

		mAlphaAnimationSet = AnimationUtils.loadAnimation(this,
				R.anim.alpha_ani_set);

		mScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_one);

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

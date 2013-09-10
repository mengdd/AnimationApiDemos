package com.example.viewflipper;

import com.example.helloanimation.R;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ViewAnimator;
//The codes are from the book:
//Android 3.0 Animations

public class BookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.book_layout);

		final ViewAnimator pages = (ViewAnimator) findViewById(R.id.pages);
		Button prev = (Button) findViewById(R.id.prev);
		Button next = (Button) findViewById(R.id.next);
		prev.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pages.showPrevious();
			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pages.showNext();
			}
		});

		// 使用代码中定义的动画来覆盖xml文件中的动画
		useCodeAnimation(pages);

		// ObjectAnimator动画
		// 让小球滚动的动画
		View rollingBall = findViewById(R.id.rollingball);
		ObjectAnimator ballRoller = ObjectAnimator.ofFloat(rollingBall,
				"TranslationX", 0, 400);
		ballRoller.setDuration(2000);
		ballRoller.setRepeatMode(ObjectAnimator.REVERSE);
		ballRoller.setRepeatCount(ObjectAnimator.INFINITE);
		ballRoller.start();

		// ValueAnimator的使用
		// 第二页，让小球上下移动的动画
		final View bouncingBall = findViewById(R.id.bouncingball);
		final ValueAnimator ballBouncer = ValueAnimator.ofInt(0, 40);
		ballBouncer.setDuration(2000);
		ballBouncer.setRepeatMode(ValueAnimator.REVERSE);
		ballBouncer.setRepeatCount(ValueAnimator.INFINITE);

		// change interpolator
		// ballBouncer.setInterpolator(new BounceInterpolator());
		ballBouncer.setInterpolator(new DecelerateInterpolator());

		ValueAnimator.setFrameDelay(50);

		ballBouncer.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				final int animatedValue = (Integer) ballBouncer
						.getAnimatedValue();

				bouncingBall.post(new Runnable() {

					@Override
					public void run() {
						bouncingBall.setPadding(bouncingBall.getPaddingLeft(),
								40 - animatedValue,
								bouncingBall.getPaddingRight(), animatedValue);
						bouncingBall.invalidate();
					}
				});

			}
		});

		ballBouncer.start();
	}

	private void useCodeAnimation(ViewAnimator pages) {
		// 用代码定义一个动画
		AnimationSet slideAnimationSet = new AnimationSet(true);

		// 平移动画
		TranslateAnimation slide = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1f, Animation.RELATIVE_TO_PARENT,
				0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);

		// 缩放动画
		ScaleAnimation scale = new ScaleAnimation(10, 1, 10, 1);
		// 把平移和缩放动画加入动画集合
		slideAnimationSet.addAnimation(slide);
		slideAnimationSet.addAnimation(scale);

		// 持续时间设置为1000ms
		slideAnimationSet.setDuration(1000);

		// 设置动画
		pages.setInAnimation(slideAnimationSet);
	}

}

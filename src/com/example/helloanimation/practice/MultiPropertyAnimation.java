/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.helloanimation.practice;

import android.animation.*;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

import com.example.helloanimation.R;
import com.example.helloanimation.demo.ShapeHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

public class MultiPropertyAnimation extends Activity {

	private static final int DURATION = 1500;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_multi_property);
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		final MyAnimationView animView = new MyAnimationView(this);
		container.addView(animView);

		Button starter = (Button) findViewById(R.id.startButton);
		starter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				animView.startAnimation();

			}
		});

	}

	public class MyAnimationView extends View implements
			ValueAnimator.AnimatorUpdateListener {

		private static final float BALL_SIZE = 100f;

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
		AnimatorSet animation = null;
		Animator bounceAnim = null;
		ShapeHolder ball = null;

		public MyAnimationView(Context context) {
			super(context);
			addBall(50, 0);
			addBall(150, 0);
			addBall(250, 0);
			addBall(350, 0);
		}

		private void createAnimation() {
			if (bounceAnim == null) {
				ShapeHolder ball;

				// ============================================================
				// 第一个小球：弹跳效果
				ball = balls.get(0);
				ObjectAnimator yBouncer = ObjectAnimator.ofFloat(ball, "y",
						ball.getY(), getHeight() - BALL_SIZE).setDuration(
						DURATION);
				yBouncer.setInterpolator(new BounceInterpolator());
				yBouncer.addUpdateListener(this);

				// ============================================================
				// 第二个小球：加速下落并且alpha变化
				ball = balls.get(1);

				// 利用ofFloat工厂方法构造PropertyValuesHolder类型对象，控制y属性
				PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y",
						ball.getY(), getHeight() - BALL_SIZE);
				// 利用ofFloat工厂方法构造另一个PropertyValuesHolder类型对象，控制alpha属性
				PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat(
						"alpha", 1.0f, 0f);
				// 利用ofPropertyValuesHolder方法来构造ObjectAnimator对象
				// 把多个属性变化结合到一个动画中去
				ObjectAnimator yAlphaBouncer = ObjectAnimator
						.ofPropertyValuesHolder(ball, pvhY, pvhAlpha)
						.setDuration(DURATION / 2);
				yAlphaBouncer.setInterpolator(new AccelerateInterpolator());
				yAlphaBouncer.setRepeatCount(1);
				yAlphaBouncer.setRepeatMode(ValueAnimator.REVERSE);

				// ============================================================
				// 第三个小球：宽度，高度，x，y同时变化
				ball = balls.get(2);
				PropertyValuesHolder pvhW = PropertyValuesHolder.ofFloat(
						"width", ball.getWidth(), ball.getWidth() * 2);
				PropertyValuesHolder pvhH = PropertyValuesHolder.ofFloat(
						"height", ball.getHeight(), ball.getHeight() * 2);
				PropertyValuesHolder pvTX = PropertyValuesHolder.ofFloat("x",
						ball.getX(), ball.getX() - BALL_SIZE / 2f);
				PropertyValuesHolder pvTY = PropertyValuesHolder.ofFloat("y",
						ball.getY(), ball.getY() - BALL_SIZE / 2f);
				// 利用ofPropertyValuesHolder方法来构造ObjectAnimator对象
				// 因为是可变参数，所以PropertyValuesHolder对象的个数不限
				ObjectAnimator whxyBouncer = ObjectAnimator
						.ofPropertyValuesHolder(ball, pvhW, pvhH, pvTX, pvTY)
						.setDuration(DURATION / 2);
				whxyBouncer.setRepeatCount(1);
				whxyBouncer.setRepeatMode(ValueAnimator.REVERSE);

				// ============================================================
				// 第四个小球：利用关键帧实现曲线运动
				ball = balls.get(3);
				// 属性1：Y坐标运动：下落
				pvhY = PropertyValuesHolder.ofFloat("y", ball.getY(),
						getHeight() - BALL_SIZE);
				float ballX = ball.getX();
				// 三个关键帧
				Keyframe kf0 = Keyframe.ofFloat(0f, ballX);
				Keyframe kf1 = Keyframe.ofFloat(.5f, ballX + 100f);
				Keyframe kf2 = Keyframe.ofFloat(1f, ballX + 50f);
				// 属性2：X坐标运动：曲折
				// 用三个关键帧构造PropertyValuesHolder对象
				PropertyValuesHolder pvhX = PropertyValuesHolder.ofKeyframe(
						"x", kf0, kf1, kf2);

				// 再用两个PropertyValuesHolder对象构造一个ObjectAnimator对象
				ObjectAnimator yxBouncer = ObjectAnimator
						.ofPropertyValuesHolder(ball, pvhY, pvhX).setDuration(
								DURATION / 2);
				yxBouncer.setRepeatCount(1);
				yxBouncer.setRepeatMode(ValueAnimator.REVERSE);

				// ===========================================================
				// 所有小球动画的集合
				bounceAnim = new AnimatorSet();
				((AnimatorSet) bounceAnim).playTogether(yBouncer,
						yAlphaBouncer, whxyBouncer, yxBouncer);
			}
		}

		public void startAnimation() {
			createAnimation();
			bounceAnim.start();
		}

		private ShapeHolder addBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(BALL_SIZE, BALL_SIZE);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x);
			shapeHolder.setY(y);
			int red = (int) (100 + Math.random() * 155);
			int green = (int) (100 + Math.random() * 155);
			int blue = (int) (100 + Math.random() * 155);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			Paint paint = drawable.getPaint();
			int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue
					/ 4;
			RadialGradient gradient = new RadialGradient(37.5f, 12.5f, 50f,
					color, darkColor, Shader.TileMode.CLAMP);
			paint.setShader(gradient);
			shapeHolder.setPaint(paint);
			balls.add(shapeHolder);
			return shapeHolder;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			for (ShapeHolder ball : balls) {
				canvas.translate(ball.getX(), ball.getY());
				ball.getShape().draw(canvas);
				canvas.translate(-ball.getX(), -ball.getY());
			}
		}

		public void onAnimationUpdate(ValueAnimator animation) {
			invalidate();
		}

	}
}
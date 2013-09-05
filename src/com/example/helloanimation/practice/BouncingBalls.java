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
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;

import com.example.helloanimation.R;
import com.example.helloanimation.demo.ShapeHolder;

public class BouncingBalls extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bouncing_balls);
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		container.addView(new MyAnimationView(this));
	}

	public class MyAnimationView extends View {

		private static final int RED = 0xffFF8080;
		private static final int BLUE = 0xff8080FF;
		private static final int CYAN = 0xff80ffff;
		private static final int GREEN = 0xff80ff80;

		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();

		public MyAnimationView(Context context) {
			super(context);

			// Animate background color
			// Note that setting the background color will automatically
			// invalidate the
			// view, so that the animated color, and the bouncing balls, get
			// redisplayed on
			// every frame of the animation.
			ValueAnimator colorAnim = ObjectAnimator.ofInt(this,
					"backgroundColor", RED, BLUE);
			colorAnim.setDuration(3000);
			colorAnim.setEvaluator(new ArgbEvaluator());// 颜色用的:ArgbEvaluator
			colorAnim.setRepeatCount(ValueAnimator.INFINITE);// 无限重复这个动画
			colorAnim.setRepeatMode(ValueAnimator.REVERSE);
			colorAnim.start();
		}

		// View的点击事件，每点击一下或者滑动，就生成一个小球
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() != MotionEvent.ACTION_DOWN
					&& event.getAction() != MotionEvent.ACTION_MOVE) {
				return false;
			}
			// 在点击的地方生成一个新的小球对象，并加入到小球对象的列表中去
			// 之后的所有动画都是作用于这个小球对象
			ShapeHolder newBall = addBall(event.getX(), event.getY());

			// Bouncing animation with squash and stretch
			float startY = newBall.getY();
			float endY = getHeight() - 50f;
			float h = (float) getHeight();
			float eventY = event.getY();
			// 根据高度计算出一个动画时间
			int duration = (int) (500 * ((h - eventY) / h));
			// ==============================================================
			// 加速下落动画
			ValueAnimator bounceAnim = ObjectAnimator.ofFloat(newBall, "y",
					startY, endY);
			bounceAnim.setDuration(duration);
			bounceAnim.setInterpolator(new AccelerateInterpolator());
			// ==============================================================
			// 撞击动画由两部分组成：左移25，加宽50，这样就好像从水平中心向两边加宽
			// 撞击动画组成部分1：x坐标左移25
			ValueAnimator squashAnim1 = ObjectAnimator.ofFloat(newBall, "x",
					newBall.getX(), newBall.getX() - 25f);
			squashAnim1.setDuration(duration / 4);
			squashAnim1.setRepeatCount(1);
			squashAnim1.setRepeatMode(ValueAnimator.REVERSE);
			squashAnim1.setInterpolator(new DecelerateInterpolator());
			// 撞击动画组成部分2：球的宽度增加50
			ValueAnimator squashAnim2 = ObjectAnimator.ofFloat(newBall,
					"width", newBall.getWidth(), newBall.getWidth() + 50);
			squashAnim2.setDuration(duration / 4);
			squashAnim2.setRepeatCount(1);
			squashAnim2.setRepeatMode(ValueAnimator.REVERSE);
			squashAnim2.setInterpolator(new DecelerateInterpolator());

			// ==============================================================
			// 压缩动画：向下移动+高度减小
			// 压缩动画1：y坐标向下移动25
			ValueAnimator stretchAnim1 = ObjectAnimator.ofFloat(newBall, "y",
					endY, endY + 25f);
			stretchAnim1.setDuration(duration / 4);
			stretchAnim1.setRepeatCount(1);
			stretchAnim1.setInterpolator(new DecelerateInterpolator());
			stretchAnim1.setRepeatMode(ValueAnimator.REVERSE);
			// 压缩动画2：球的高度减少25
			ValueAnimator stretchAnim2 = ObjectAnimator.ofFloat(newBall,
					"height", newBall.getHeight(), newBall.getHeight() - 25);
			stretchAnim2.setDuration(duration / 4);
			stretchAnim2.setRepeatCount(1);
			stretchAnim2.setInterpolator(new DecelerateInterpolator());
			stretchAnim2.setRepeatMode(ValueAnimator.REVERSE);

			// ==============================================================
			// 回弹动画
			ValueAnimator bounceBackAnim = ObjectAnimator.ofFloat(newBall, "y",
					endY, startY);
			bounceBackAnim.setDuration(duration);
			bounceBackAnim.setInterpolator(new DecelerateInterpolator());

			// ==============================================================
			// 将小球动作的所有动画组合起来
			// Sequence the down/squash&stretch/up animations
			// 1.下落动画 2.两个撞击动画+两个压缩动画 3.回弹动画
			AnimatorSet bouncer = new AnimatorSet();
			bouncer.play(bounceAnim).before(squashAnim1);
			bouncer.play(squashAnim1).with(squashAnim2);
			bouncer.play(squashAnim1).with(stretchAnim1);
			bouncer.play(squashAnim1).with(stretchAnim2);
			bouncer.play(bounceBackAnim).after(stretchAnim2);

			// ==============================================================
			// 消失动画
			// Fading animation - remove the ball when the animation is done
			ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha",
					1f, 0f);
			fadeAnim.setDuration(250);
			// 消失动画加上动作监听
			fadeAnim.addListener(new AnimatorListenerAdapter() {// 用了适配器类，只覆写要用的方法
				@Override
				public void onAnimationEnd(Animator animation) {
					// 动画结束的时候移除这个对象
					balls.remove(((ObjectAnimator) animation).getTarget());

				}
			});

			// Sequence the two animations to play one after the other
			AnimatorSet animatorSet = new AnimatorSet();
			// 将小球弹跳的动画和消失的动画组合起来
			animatorSet.play(bouncer).before(fadeAnim);

			// Start the animation
			animatorSet.start();

			return true;
		}

		private ShapeHolder addBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(50f, 50f);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x - 25f);
			shapeHolder.setY(y - 25f);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int color = 0xff000000 | red << 16 | green << 8 | blue;
			Paint paint = drawable.getPaint(); // new
												// Paint(Paint.ANTI_ALIAS_FLAG);
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
			for (int i = 0; i < balls.size(); ++i) {
				ShapeHolder shapeHolder = balls.get(i);
				canvas.save();
				canvas.translate(shapeHolder.getX(), shapeHolder.getY());
				shapeHolder.getShape().draw(canvas);
				canvas.restore();
			}
		}
	}
}
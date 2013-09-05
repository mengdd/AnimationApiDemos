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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;

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
import android.widget.Button;
import android.widget.LinearLayout;

public class CustomEvaluator extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animator_custom_evaluator);
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

	// 自定义一个数据结构，包含两个坐标值
	public class XYHolder {
		private float mX;
		private float mY;

		public XYHolder(float x, float y) {
			mX = x;
			mY = y;
		}

		public float getX() {
			return mX;
		}

		public void setX(float x) {
			mX = x;
		}

		public float getY() {
			return mY;
		}

		public void setY(float y) {
			mY = y;
		}
	}

	// 实现自己的TypeEvaluator
	// 传入fraction和两个XYHolder类型的对象
	// 返回一个XYHolder对象，其坐标值为两个输入参数的相应值线性插值的结果
	public class XYEvaluator implements TypeEvaluator {
		public Object evaluate(float fraction, Object startValue,
				Object endValue) {
			XYHolder startXY = (XYHolder) startValue;
			XYHolder endXY = (XYHolder) endValue;
			return new XYHolder(startXY.getX() + fraction
					* (endXY.getX() - startXY.getX()), startXY.getY()
					+ fraction * (endXY.getY() - startXY.getY()));
		}
	}

	// 定义一个物体对象类型，包装了ShapeHolder，使用XYHolder作为一个参数
	public class BallXYHolder {

		private ShapeHolder mBall;

		public BallXYHolder(ShapeHolder ball) {
			mBall = ball;
		}

		public void setXY(XYHolder xyHolder) {
			mBall.setX(xyHolder.getX());
			mBall.setY(xyHolder.getY());
		}

		public XYHolder getXY() {
			return new XYHolder(mBall.getX(), mBall.getY());
		}
	}

	public class MyAnimationView extends View implements
			ValueAnimator.AnimatorUpdateListener {

		ValueAnimator bounceAnim = null;
		ShapeHolder ball = null;
		BallXYHolder ballHolder = null;

		public MyAnimationView(Context context) {
			super(context);

			// 先创建一个球，然后用一个BallXYHolder把它包装起来
			ball = createBall(25, 25);
			ballHolder = new BallXYHolder(ball);
		}

		private void createAnimation() {
			if (bounceAnim == null) {
				XYHolder startXY = new XYHolder(0f, 0f);
				XYHolder endXY = new XYHolder(300f, 500f);

				// ObjectAnimator的构造使用了ofObject形式的工厂方法，传入了自定义的evaluator对象
				bounceAnim = ObjectAnimator.ofObject(ballHolder, "xY",
						new XYEvaluator(), startXY, endXY);
				// 也可以不提供初始值，如下写：
				// bounceAnim = ObjectAnimator.ofObject(ballHolder, "xY",
				// new XYEvaluator(), endXY);
				bounceAnim.setDuration(1500);
				bounceAnim.addUpdateListener(this);
			}
		}

		public void startAnimation() {
			createAnimation();
			bounceAnim.start();
		}

		private ShapeHolder createBall(float x, float y) {
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
			return shapeHolder;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.save();
			canvas.translate(ball.getX(), ball.getY());
			ball.getShape().draw(canvas);
			canvas.restore();
		}

		public void onAnimationUpdate(ValueAnimator animation) {
			invalidate();
		}

	}
}
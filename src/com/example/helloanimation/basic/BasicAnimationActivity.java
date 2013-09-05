package com.example.helloanimation.basic;

import java.util.ArrayList;

import com.example.helloanimation.R;
import com.example.helloanimation.demo.ShapeHolder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

public class BasicAnimationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置布局，布局xml中只包含了一个线性布局和一个Button
		setContentView(R.layout.animation_basic);

		LinearLayout container = (LinearLayout) findViewById(R.id.container);

		// 将自定义的View加入到线性布局中
		final MyAnimationView animView = new MyAnimationView(this);
		container.addView(animView);

		// Button的点击事件即动画开始
		Button starter = (Button) findViewById(R.id.startButton);
		starter.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				animView.startAnimation();
			}
		});
	}

	/**
	 * 自定义的View类
	 * 其中包含了一系列的球形对象
	 * 
	 */
	public class MyAnimationView extends View implements
			ValueAnimator.AnimatorUpdateListener {

		// 圆形球
		public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();

		// 总的动画集合
		AnimatorSet animation = null;

		// 屏幕密度
		private float mDensity;

		public MyAnimationView(Context context) {
			super(context);

			// 得到密度值
			mDensity = getContext().getResources().getDisplayMetrics().density;

			addBall(50f, 25f);
			addBall(150f, 25f);
			addBall(250f, 25f);
			addBall(350f, 25f);
			addBall(450f, 25f);
		}

		private void createAnimation() {

			if (animation == null) {
				// ===============================================
				// 第1个球球的动画效果：用ObjectAnimator
				// 用工厂方法构造对象：用ofFloat()因为属性值是float类型
				// 第1个参数为目标对象：balls.get(0)
				// 第2个参数为属性名：y 这里要求目标对象要有“set属性名()”的方法。
				// 后面是可变参数，表明属性目标值，一个参数表明是终止值（对象要有get属性方法）
				// 可变参数的个数为2时，表明第一个是起始值，第二个是终止值；更多个参数时，动画属性值会逐个经过这些值

				ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0),
						"y", 0f, getHeight() - balls.get(0).getHeight())
						.setDuration(500);

				// ===============================================
				// 第二个球球的动画效果：clone动画效果1，但是重新设置目标物体
				ObjectAnimator anim2 = anim1.clone();
				anim2.setTarget(balls.get(1));
				anim1.addUpdateListener(this);
				// 因为前两个动画完全相同，所以设置刷新监听的时候就只设置了一个（它们刷新的是同一个View）

				// ===============================================
				// 第三个球球的动画效果：先加速下落，再减速上升
				ShapeHolder ball2 = balls.get(2);
				// 动画效果：落下效果
				ObjectAnimator animDown = ObjectAnimator.ofFloat(ball2, "y",
						0f, getHeight() - ball2.getHeight()).setDuration(500);
				// 落下效果改变了Interpolator,设置为加速
				animDown.setInterpolator(new AccelerateInterpolator());
				// 动画效果：上升效果
				ObjectAnimator animUp = ObjectAnimator.ofFloat(ball2, "y",
						getHeight() - ball2.getHeight(), 0f).setDuration(500);
				// 上升效果设置为减速上升
				animUp.setInterpolator(new DecelerateInterpolator());

				// 用一个AnimatorSet对象将下落效果和上升效果顺序播放
				AnimatorSet s1 = new AnimatorSet();
				s1.playSequentially(animDown, animUp);// 顺序播放效果，参数个数可变

				// 下落动画刷新View
				animDown.addUpdateListener(this);
				// 上升动画刷新View
				animUp.addUpdateListener(this);
				// ===============================================
				// 第四个球球的动画效果
				// 另一个AnimatorSet克隆了上一个set，更换了对象
				AnimatorSet s2 = (AnimatorSet) s1.clone();
				s2.setTarget(balls.get(3));

				// ===============================================
				// 第五个球球的动画效果:使用ValueAnimator
				final ShapeHolder ball5 = balls.get(4);
				ValueAnimator valueAnimator5 = ValueAnimator.ofFloat(0f,
						getHeight() - ball5.getHeight());
				valueAnimator5.setDuration(500);
				valueAnimator5.addUpdateListener(new AnimatorUpdateListener() {

					// ValueAnimator需要自己在监听处理中设置对象参数
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// 用animation.getAnimatedValue()得到当前的属性值，设置进动画对象中
						ball5.setY((Float) animation.getAnimatedValue());

						// 记得要刷新View否则不会调用重新绘制
						invalidate();
					}
				});

				// =============================================================
				// 用一个总的AnimatorSet对象管理以上所有动画
				animation = new AnimatorSet();
				animation.playTogether(anim1, anim2, s1);// 并行
				animation.playSequentially(s1, s2, valueAnimator5);// 串行
			}
		}

		// 在指定位置加上球形
		private ShapeHolder addBall(float x, float y) {
			OvalShape circle = new OvalShape();
			circle.resize(50f * mDensity, 50f * mDensity);
			ShapeDrawable drawable = new ShapeDrawable(circle);
			ShapeHolder shapeHolder = new ShapeHolder(drawable);
			shapeHolder.setX(x - 25f);
			shapeHolder.setY(y - 25f);
			int red = (int) (100 + Math.random() * 155);
			int green = (int) (100 + Math.random() * 155);
			int blue = (int) (100 + Math.random() * 155);
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
			// 遍历并绘制每一个球形对象
			for (int i = 0; i < balls.size(); ++i) {
				ShapeHolder shapeHolder = balls.get(i);
				canvas.save();
				canvas.translate(shapeHolder.getX(), shapeHolder.getY());
				shapeHolder.getShape().draw(canvas);
				canvas.restore();
			}
		}

		public void startAnimation() {
			createAnimation();
			animation.start();// 这里开始播放动画
		}

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			// 在参数更新的时候invalidate，刷新整个View的绘制
			// 否则onDraw不会被调用，即看不到View外观的改变
			invalidate();
		}

	}
}

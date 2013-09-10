package com.example.orrery;

import com.example.helloanimation.R;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
//The codes are from the book:
//Android 3.0 Animations

public class OrreryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orrery_main);

		ImageView orrery = (ImageView) findViewById(R.id.orrery);
		OrreryDrawable myOrreryDrawable = OrreryDrawable.Create();
		orrery.setImageDrawable(myOrreryDrawable);

		// ================================================================
		// 分别控制两种属性的动画
		// PropertyValuesHolder earthPositionValues = PropertyValuesHolder
		// .ofFloat("EarthPosition", 0, (float) (2 * Math.PI));
		// PropertyValuesHolder moonPositionValues =
		// PropertyValuesHolder.ofFloat(
		// "MoonPosition", 0, (float) (2 * Math.PI * 13));
		// ObjectAnimator orreryAnimator =
		// ObjectAnimator.ofPropertyValuesHolder(
		// myOrreryDrawable, earthPositionValues, moonPositionValues);

		// ================================================================
		// 使用新的数据结构，同时控制地球和月球
		OrreryDrawable.SolarSystemData startSolarSystemData = new OrreryDrawable.SolarSystemData();
		startSolarSystemData.rotationEarth = 0;
		startSolarSystemData.rotationMoon = 0;
		OrreryDrawable.SolarSystemData endSolarSystemData = new OrreryDrawable.SolarSystemData();
		endSolarSystemData.rotationEarth = (float) (2 * Math.PI);
		endSolarSystemData.rotationMoon = (float) (2 * Math.PI * 13);
		// 使用自定义的Evaluator
		OrreryEvaluator orreryEvaluator = new OrreryEvaluator();
		// ObjectAnimator orreryAnimator = ObjectAnimator.ofObject(
		// myOrreryDrawable, "SolarSystemData", orreryEvaluator,
		// startSolarSystemData, endSolarSystemData);

		// ================================================================
		// 尝试一下Keyframe
		Keyframe startFrame = Keyframe.ofObject(0, startSolarSystemData);
		Keyframe endFrame = Keyframe.ofObject(1, endSolarSystemData);
		PropertyValuesHolder solarSystemFrames = PropertyValuesHolder
				.ofKeyframe("SolarSystemData", startFrame, endFrame);
		solarSystemFrames.setEvaluator(orreryEvaluator);

		ObjectAnimator orreryAnimator = ObjectAnimator.ofPropertyValuesHolder(
				myOrreryDrawable, solarSystemFrames);

		// Default value is 10
		Log.i("FrameDelay", "delay: " + ValueAnimator.getFrameDelay());
		ValueAnimator.setFrameDelay(50);

		orreryAnimator.setDuration(60000);
		orreryAnimator.setInterpolator(new LinearInterpolator());
		orreryAnimator.setRepeatCount(ValueAnimator.INFINITE);
		orreryAnimator.setRepeatMode(ValueAnimator.RESTART);

		orreryAnimator.start();

		// add the fragment:
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		ft.setCustomAnimations(R.anim.fade_in, android.R.animator.fade_out);
		ft.add(R.id.frame, new OrreryInfo());
		ft.commit();
	}

	private class OrreryEvaluator implements TypeEvaluator {
		public Object evaluate(float fraction, Object start, Object end) {
			OrreryDrawable.SolarSystemData startSolarSystemData = (OrreryDrawable.SolarSystemData) start;
			OrreryDrawable.SolarSystemData endSolarSystemData = (OrreryDrawable.SolarSystemData) end;
			OrreryDrawable.SolarSystemData result = new OrreryDrawable.SolarSystemData();
			result.rotationEarth = startSolarSystemData.rotationEarth
					+ fraction
					* (endSolarSystemData.rotationEarth - startSolarSystemData.rotationEarth);
			result.rotationMoon = startSolarSystemData.rotationMoon
					+ fraction
					* (endSolarSystemData.rotationMoon - startSolarSystemData.rotationMoon);
			return result;
		}
	}

}

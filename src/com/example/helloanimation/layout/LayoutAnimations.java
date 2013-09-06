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

package com.example.helloanimation.layout;

import com.example.helloanimation.R;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * This application demonstrates how to use LayoutTransition to automate
 * transition animations
 * as items are removed from or added to a container.
 */
public class LayoutAnimations extends Activity {

	private int numButtons = 1;
	ViewGroup container = null;

	// 默认的动画
	Animator defaultAppearingAnim, defaultDisappearingAnim;
	Animator defaultChangingAppearingAnim, defaultChangingDisappearingAnim;

	// 自定义动画
	Animator customAppearingAnim, customDisappearingAnim;
	Animator customChangingAppearingAnim, customChangingDisappearingAnim;

	// 当前动画
	Animator currentAppearingAnim, currentDisappearingAnim;
	Animator currentChangingAppearingAnim, currentChangingDisappearingAnim;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_animations);

		container = new FixedGridLayout(this);
		container.setClipChildren(false);
		((FixedGridLayout) container).setCellHeight(90);
		((FixedGridLayout) container).setCellWidth(100);

		// ViewGroup布局变换动画
		final LayoutTransition transitioner = new LayoutTransition();

		// 把LayoutTransition对象transitioner设置给这个外层容器
		container.setLayoutTransition(transitioner);

		// 默认系列
		defaultAppearingAnim = transitioner
				.getAnimator(LayoutTransition.APPEARING);
		defaultDisappearingAnim = transitioner
				.getAnimator(LayoutTransition.DISAPPEARING);
		defaultChangingAppearingAnim = transitioner
				.getAnimator(LayoutTransition.CHANGE_APPEARING);
		defaultChangingDisappearingAnim = transitioner
				.getAnimator(LayoutTransition.CHANGE_DISAPPEARING);

		// 创建自定义动画
		createCustomAnimations(transitioner);

		// 先将默认的作为当前的
		currentAppearingAnim = defaultAppearingAnim;
		currentDisappearingAnim = defaultDisappearingAnim;
		currentChangingAppearingAnim = defaultChangingAppearingAnim;
		currentChangingDisappearingAnim = defaultChangingDisappearingAnim;

		ViewGroup parent = (ViewGroup) findViewById(R.id.parent);

		// 加入自定义的FixedGridLayout
		parent.addView(container);
		parent.setClipChildren(false);

		Button addButton = (Button) findViewById(R.id.addNewButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Add Button按钮点击事件：新增按钮，按钮的点击事件是移除自身
				Button newButton = new Button(LayoutAnimations.this);
				newButton.setText(String.valueOf(numButtons++));
				newButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						container.removeView(v);
					}
				});
				// Math.min(1, container.getChildCount()的使用：
				// addView的index:最开始是0，后来都是1
				// 即第一个按钮是加在index为0位置，后续添加的按钮都是插入地加在index为1，即第二的位置
				container.addView(newButton,
						Math.min(1, container.getChildCount()));
			}
		});

		// 几个多选按钮的事件都一样
		CheckBox customAnimCB = (CheckBox) findViewById(R.id.customAnimCB);
		customAnimCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});

		// Check for disabled animations
		CheckBox appearingCB = (CheckBox) findViewById(R.id.appearingCB);
		appearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
		CheckBox disappearingCB = (CheckBox) findViewById(R.id.disappearingCB);
		disappearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
		CheckBox changingAppearingCB = (CheckBox) findViewById(R.id.changingAppearingCB);
		changingAppearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
		CheckBox changingDisappearingCB = (CheckBox) findViewById(R.id.changingDisappearingCB);
		changingDisappearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
	}

	// 多选按钮的选择变换后执行的事件方法
	// 根据多选按钮的状态来设置LayoutTransition transition的四个Animator
	private void setupTransition(LayoutTransition transition) {
		// 首先，把多选按钮全都获取一遍
		CheckBox customAnimCB = (CheckBox) findViewById(R.id.customAnimCB);
		CheckBox appearingCB = (CheckBox) findViewById(R.id.appearingCB);
		CheckBox disappearingCB = (CheckBox) findViewById(R.id.disappearingCB);
		CheckBox changingAppearingCB = (CheckBox) findViewById(R.id.changingAppearingCB);
		CheckBox changingDisappearingCB = (CheckBox) findViewById(R.id.changingDisappearingCB);

		// 然后，根据多选按钮的选择状态来setAnimator

		// 解释一下第一个：
		// appearingCB.isChecked()?
		// / \
		// true false -> transition.setAnimator(LayoutTransition.APPEARING,null)
		// |
		// customAnimCB.isChecked() ?
		// / \
		// true false ->
		// transition.setAnimator(LayoutTransition.APPEARING,defaultAppearingAnim)
		// |-->transition.setAnimator(LayoutTransition.APPEARING,customAppearingAnim)
		transition.setAnimator(LayoutTransition.APPEARING, appearingCB
				.isChecked() ? (customAnimCB.isChecked() ? customAppearingAnim
				: defaultAppearingAnim) : null);

		// 后面三个依次类推
		transition
				.setAnimator(
						LayoutTransition.DISAPPEARING,
						disappearingCB.isChecked() ? (customAnimCB.isChecked() ? customDisappearingAnim
								: defaultDisappearingAnim)
								: null);
		transition
				.setAnimator(
						LayoutTransition.CHANGE_APPEARING,
						changingAppearingCB.isChecked() ? (customAnimCB
								.isChecked() ? customChangingAppearingAnim
								: defaultChangingAppearingAnim) : null);
		transition
				.setAnimator(
						LayoutTransition.CHANGE_DISAPPEARING,
						changingDisappearingCB.isChecked() ? (customAnimCB
								.isChecked() ? customChangingDisappearingAnim
								: defaultChangingDisappearingAnim) : null);
	}

	// 创建自定义动画
	private void createCustomAnimations(LayoutTransition transition) {
		// Changing while Adding
		// 多个属性同时动画
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0,
				1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
				1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
				1f, 0f, 1f);

		// 自定义的ChangingAppearing动画
		// 感觉就是从无到有地缩放了一遍
		customChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(
				this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX,
				pvhScaleY).setDuration(
				transition.getDuration(LayoutTransition.CHANGE_APPEARING));
		customChangingAppearingAnim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// 自定义的ChangingDisappearing动画
		// 有一个平面旋转
		// Changing while Removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
				"rotation", kf0, kf1, kf2);
		customChangingDisappearingAnim = ObjectAnimator
				.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight,
						pvhBottom, pvhRotation)
				.setDuration(
						transition
								.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		customChangingDisappearingAnim
				.addListener(new AnimatorListenerAdapter() {
					public void onAnimationEnd(Animator anim) {
						View view = (View) ((ObjectAnimator) anim).getTarget();
						view.setRotation(0f);
					}
				});

		// 自定义的Appearing动画：纵向旋转（以Y为轴翻转）出现
		// Adding
		customAppearingAnim = ObjectAnimator
				.ofFloat(null, "rotationY", 90f, 0f).setDuration(
						transition.getDuration(LayoutTransition.APPEARING));
		customAppearingAnim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// 自定义的Disappearing动画：横向旋转（以X为轴翻转）消失
		// Removing
		customDisappearingAnim = ObjectAnimator.ofFloat(null, "rotationX", 0f,
				90f).setDuration(
				transition.getDuration(LayoutTransition.DISAPPEARING));
		customDisappearingAnim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}
}
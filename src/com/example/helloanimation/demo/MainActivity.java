package com.example.helloanimation.demo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.helloanimation.R;
import com.example.helloanimation.basic.AnimationFromXmlActivity;
import com.example.helloanimation.basic.BasicAnimationActivity;
import com.example.helloanimation.layout.LayoutAnimations;
import com.example.helloanimation.layout.LayoutAnimationsByDefault;
import com.example.helloanimation.layout.LayoutAnimationsHideShow;
import com.example.helloanimation.practice.BouncingBalls;
import com.example.helloanimation.practice.CustomEvaluator;
import com.example.helloanimation.practice.MultiPropertyAnimation;
import com.example.helloanimation.threed.Transition3d;
import com.example.orrery.OrreryActivity;
import com.example.viewanimation.basic.BasicAnimationDemoActivity;
import com.example.viewanimation.basic.BasicXMLAnimationActivity;
import com.example.viewflipper.BookActivity;

/**
 *
 * @ClassName: MainActivity
 * @Description: 入口，将各个Demo列在一个ListView中
 * @author Meng Dandan
 * @date 2013年9月4日
 *
 */
public class MainActivity extends ListActivity {

	private static Sample[] mSamples;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_animation);

		// Instantiate the list of samples.
		mSamples = new Sample[] {
				new Sample(R.string.basic, BasicAnimationActivity.class),
				new Sample(R.string.basic_xml, AnimationFromXmlActivity.class),
				new Sample(R.string.bouncing_balls, BouncingBalls.class),
				new Sample(R.string.custom_evaluator, CustomEvaluator.class),
				new Sample(R.string.multi_property,
						MultiPropertyAnimation.class),
				new Sample(R.string.layout_ani, LayoutAnimations.class),
				new Sample(R.string.layout_ani_default,
						LayoutAnimationsByDefault.class),
				new Sample(R.string.layout_ani_hide,
						LayoutAnimationsHideShow.class),
				new Sample(R.string.transition_3d, Transition3d.class),
				new Sample(R.string.book, BookActivity.class),
				new Sample(R.string.orrery, OrreryActivity.class),
				new Sample(R.string.view, BasicAnimationDemoActivity.class),
				new Sample(R.string.view_xml, BasicXMLAnimationActivity.class)

		};

		// 设置Adapter
		setListAdapter(new ArrayAdapter<Sample>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				mSamples));
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		// Launch the sample associated with this list position.
		startActivity(new Intent(MainActivity.this,
				mSamples[position].activityClass));
	}

	// 私有类，List中的每一个例子
	private class Sample {
		private CharSequence title;
		private Class<? extends Activity> activityClass;

		public Sample(int titleResId, Class<? extends Activity> activityClass) {
			this.activityClass = activityClass;
			this.title = getResources().getString(titleResId);
		}

		@Override
		public String toString() {
			return title.toString();
		}
	}

}

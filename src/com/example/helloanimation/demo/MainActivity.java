package com.example.helloanimation.demo;

import com.example.helloanimation.R;
import com.example.helloanimation.demo1.AnimationFromXmlActivity;
import com.example.helloanimation.demo1.BasicAnimationActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
				new Sample(R.string.demo1, BasicAnimationActivity.class),
				new Sample(R.string.demo2, AnimationFromXmlActivity.class)

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

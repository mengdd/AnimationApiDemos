package com.example.helloanimation.layout;

import com.example.helloanimation.R;

import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

/**
 * This application demonstrates how to use the animateLayoutChanges tag in XML
 * to automate
 * transition animations as items are removed from or added to a container.
 */
public class LayoutAnimationsByDefault extends Activity {

	private int numButtons = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_animations_by_default);

		// final GridLayout gridContainer = (GridLayout)
		// findViewById(R.id.gridContainer);
		// GridLayout是API Level 14才有的
		
		final LinearLayout gridContainer = (LinearLayout) findViewById(R.id.gridContainer);
		Button addButton = (Button) findViewById(R.id.addNewButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Button newButton = new Button(LayoutAnimationsByDefault.this);
				newButton.setText(String.valueOf(numButtons++));
				newButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						gridContainer.removeView(v);
					}
				});
				gridContainer.addView(newButton,
						Math.min(1, gridContainer.getChildCount()));
			}
		});
	}

}
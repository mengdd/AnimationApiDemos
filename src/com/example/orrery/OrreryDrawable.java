package com.example.orrery;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.widget.ImageView;
//The codes are from the book:
//Android 3.0 Animations

public class OrreryDrawable extends LayerDrawable {
	private static final int SPACE_HEIGHT = 150;
	private static final int RADIUS_SUN = 20;
	private static final int RADIUS_EARTH = 10;
	private static final int RADIUS_MOON = 3;
	private static final int ORBIT_EARTH = 50;
	private static final int ORBIT_MOON = 20;
	private static final int SPACE_ID = 0;
	private static final int SUN_ID = 1;
	private static final int EARTH_ID = 2;
	private static final int MOON_ID = 3;
	private float rotationEarth = 0;
	private float rotationMoon = 0;

	public static OrreryDrawable Create() {
		ShapeDrawable space = new ShapeDrawable(new RectShape());
		space.getPaint().setColor(Color.BLACK);
		space.setIntrinsicHeight(SPACE_HEIGHT);
		space.setIntrinsicWidth(SPACE_HEIGHT);
		ShapeDrawable sun = new ShapeDrawable(new OvalShape());
		sun.getPaint().setColor(Color.YELLOW);
		sun.setIntrinsicHeight(RADIUS_SUN * 2);
		sun.setIntrinsicWidth(RADIUS_SUN * 2);
		ShapeDrawable earth = new ShapeDrawable(new OvalShape());
		earth.getPaint().setColor(Color.BLUE);
		earth.setIntrinsicHeight(RADIUS_EARTH * 2);
		earth.setIntrinsicWidth(RADIUS_EARTH * 2);
		ShapeDrawable moon = new ShapeDrawable(new OvalShape());
		moon.getPaint().setColor(Color.LTGRAY);
		moon.setIntrinsicHeight(RADIUS_MOON * 2);
		moon.setIntrinsicWidth(RADIUS_MOON * 2);
		Drawable[] bodies = { space, sun, earth, moon };
		OrreryDrawable myOrrery = new OrreryDrawable(bodies);
		myOrrery.setEarthPosition(0);
		myOrrery.setMoonPosition(0);
		myOrrery.setLayerInset(SPACE_ID, 0, 0, 0, 0);
		myOrrery.setLayerInset(SUN_ID, SPACE_HEIGHT / 2 - RADIUS_SUN,
				SPACE_HEIGHT / 2 - RADIUS_SUN, SPACE_HEIGHT / 2 - RADIUS_SUN,
				SPACE_HEIGHT / 2 - RADIUS_SUN);
		return myOrrery;
	}

	private OrreryDrawable(Drawable[] bodies) {
		super(bodies);
	}

	private void setEarthPosition(float rotationEarth) {
		this.rotationEarth = rotationEarth;
		Point earthCenter = getEarthCenter();
		setLayerInset(EARTH_ID, (int) (earthCenter.x - RADIUS_EARTH),
				(int) (earthCenter.y - RADIUS_EARTH), (int) (SPACE_HEIGHT
						- earthCenter.x - RADIUS_EARTH), (int) (SPACE_HEIGHT
						- earthCenter.y - RADIUS_EARTH));
		this.onBoundsChange(getBounds());
	}

	private void setMoonPosition(float rotationMoon) {
		this.rotationMoon = rotationMoon;
		Point moonCenter = getMoonCenter();
		setLayerInset(MOON_ID, (int) (moonCenter.x - RADIUS_MOON),
				(int) (moonCenter.y - RADIUS_MOON), (int) (SPACE_HEIGHT
						- moonCenter.x - RADIUS_MOON), (int) (SPACE_HEIGHT
						- moonCenter.y - RADIUS_MOON));
		this.onBoundsChange(getBounds());
	}

	private Point getEarthCenter() {
		Point earthCenter = new Point();
		earthCenter.x = (int) (SPACE_HEIGHT / 2 + ORBIT_EARTH
				* Math.sin(rotationEarth));
		earthCenter.y = (int) (SPACE_HEIGHT / 2 + ORBIT_EARTH
				* Math.cos(rotationEarth));
		return earthCenter;
	}

	private Point getMoonCenter() {
		Point moonCenter = new Point();
		Point earthCenter = getEarthCenter();
		moonCenter.x = (int) (earthCenter.x + ORBIT_MOON
				* Math.sin(rotationMoon));
		moonCenter.y = (int) (earthCenter.y + ORBIT_MOON
				* Math.cos(rotationMoon));
		return moonCenter;
	}

	public void setContainer(ImageView orrery) {
		// container = orrery;
	}

	public static class SolarSystemData {
		public float rotationEarth;
		public float rotationMoon;
	}

	public void setSolarSystemData(SolarSystemData solarSystemData) {
		setEarthPosition(solarSystemData.rotationEarth);
		setMoonPosition(solarSystemData.rotationMoon);
	}
}
